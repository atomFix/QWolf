package com.quick.wolf.config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.config.annotation.ConfigDesc;
import com.quick.wolf.config.handler.ConfigHandler;
import com.quick.wolf.core.extension.ExtensionLoader;
import com.quick.wolf.exception.WolfException;
import com.quick.wolf.rpc.Exporter;
import com.quick.wolf.rpc.URL;
import com.quick.wolf.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/17:12
 */
@Slf4j
public class ServiceConfig<T> extends AbstractServiceConfig {
    private static final long serialVersionUID = -4657123661318258307L;

    static Set<String> existingService = Sets.newConcurrentHashSet();

    protected List<MethodConfig> methods;

    private T ref;

    private final List<Exporter<T>> exporters = Lists.newCopyOnWriteArrayList();

    private Class<T> interfaceClass;

    private BasicServicesInterfaceConfig basicService;

    private AtomicBoolean exported = new AtomicBoolean(false);


    public synchronized void export() {
        if (exported.get()) {
            log.warn(String.format("%s has already been exported, so ignore the export request!", interfaceClass.getName()));
            return;
        }

        checkInterfaceAndMethods(interfaceClass, methods);

        loadRegisterUrls();
        if (registerUrls == null || registerUrls.isEmpty()) {
            throw new IllegalStateException("Should set registry config for service:" + interfaceClass.getName());
        }

        Map<String, Integer> pp = getProtocolAndPort();
        for (ProtocolConfig protocolConfig : protocolConfigs) {
            Integer port = pp.get(protocolConfig.getId());
            if (port == null) {
                throw new WolfException(String.format("Unknown port in service:%s, protocol:%s", interfaceClass.getName(),
                        protocolConfig.getId()));
            }
            doExport(protocolConfig, port);
        }

        afterExport();
    }

    public synchronized void unexport() {
        if (!exported.get()) {
            return;
        }

        try {
            ConfigHandler handler = ExtensionLoader.getExtensionLoader(ConfigHandler.class).getExtension(WolfConstants.STR_DEFAULT_VALUE);
            handler.unexport(exporters, registerUrls);
        } finally {
            afterUnexport();
        }
    }

    private void afterUnexport() {
        exported.set(false);
        for (Exporter<T> exporter : exporters) {
            existingService.remove(exporter.getProvider().getUrl().getIdentity());
        }
        exporters.clear();
    }

    private void doExport(ProtocolConfig protocolConfig, Integer port) {
        String protocolName = protocolConfig.getName();
        if (Strings.isNullOrEmpty(protocolName)) {
            protocolName = URLParamType.protocol.getName();
        }
        String hostAddress = host;
        if (Strings.isNullOrEmpty(hostAddress) && basicService != null) {
            hostAddress = basicService.getHost();
        }
        if (Strings.isNullOrEmpty(hostAddress)) {
            hostAddress = getLocalHostAddress();
        }
        Map<String, String> parmeter = Maps.newHashMap();

        parmeter.put(URLParamType.nodeType.getName(), WolfConstants.NODE_TYPE_SERVICE);
        parmeter.put(URLParamType.refreshTimestamp.getName(), String.valueOf(System.currentTimeMillis()));

        collectConfigParams(parmeter, protocolConfig, basicService, extConfig, this);
        collectMethodConfigParams(parmeter, methods);

        URL serviceUrl = new URL(protocolName, hostAddress, port, interfaceClass.getName(), parmeter);

        String groupString = serviceUrl.getParameter(URLParamType.group.getName(), "");
        String additionalGroup = System.getenv(WolfConstants.ENV_ADDITIONAL_GROUP);
        if (!Strings.isNullOrEmpty(additionalGroup)) {
            groupString = Strings.isNullOrEmpty(groupString) ? additionalGroup : groupString + "," + additionalGroup;
            serviceUrl.addParameter(URLParamType.group.getName(), groupString);
        }

        if (groupString.contains(WolfConstants.COMMA_SEPARATOR)) {
            String[] groups = groupString.split(WolfConstants.COMMA_SEPARATOR);
            for (String group : groups) {
                URL newGroupServiceUrl = serviceUrl.copy();
                newGroupServiceUrl.addParameter(URLParamType.group.getName(), group);
                exportService(hostAddress, protocolName, newGroupServiceUrl);
            }
        } else {
            exportService(hostAddress, protocolName, serviceUrl);
        }
    }

    private void exportService(String hostAddress, String protocolName, URL serviceUrl) {
        if (serviceExist(serviceUrl)) {
            log.warn("server already export ! hostAddress : {}, protocolName : {}, url : {}", hostAddress, protocolName, serviceUrl.getIdentity());
            return;
        }
        log.info("export service url : {}", serviceUrl.toFullString());

        List<URL> urls = Lists.newArrayList();
        if (WolfConstants.PROTOCOL_INJVM.equals(protocolName)) {
            URL localRegisterUrl = null;
            for (URL registerUrl : registerUrls) {
                if (WolfConstants.REGISTRY_PROTOCOL_LOCAL.equals(registerUrl.getProtocol())) {
                    localRegisterUrl = registerUrl.copy();
                    break;
                }
            }
            if (localRegisterUrl == null) {
                localRegisterUrl = new URL(WolfConstants.REGISTRY_PROTOCOL_LOCAL, hostAddress, WolfConstants.DEFAULT_INT_VALUE, RegisterConfig.class.getName());
            }
            urls.add(localRegisterUrl);
        } else {
            for (URL registerUrl : registerUrls) {
                urls.add(registerUrl.copy());
            }
        }

        ConfigHandler configHandler = ExtensionLoader.getExtensionLoader(ConfigHandler.class).getExtension(WolfConstants.STR_DEFAULT_VALUE);
        exporters.add(configHandler.export(interfaceClass, ref, urls, serviceUrl));
    }

    private void afterExport() {
        exported.set(true);
        for (Exporter<T> exporter : exporters) {
            existingService.add(exporter.getProvider().getUrl().getIdentity());
        }
    }


    private boolean serviceExist(URL serviceUrl) {
        return existingService.contains(serviceUrl.getIdentity());
    }


    public static Set<String> getExistingService() {
        return existingService;
    }

    public static void setExistingService(Set<String> existingService) {
        ServiceConfig.existingService = existingService;
    }

    public List<MethodConfig> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodConfig> methods) {
        this.methods = methods;
    }

    public void setMethod(MethodConfig method) {
        this.methods = Collections.singletonList(method);
    }

    public boolean hasMathod() {
        return methods != null && methods.size() > 0;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public List<Exporter<T>> getExporters() {
        return ImmutableList.copyOf(exporters);
    }


    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        if (interfaceClass != null && !interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        this.interfaceClass = interfaceClass;
    }

    @ConfigDesc(excluded = true)
    public BasicServicesInterfaceConfig getBasicService() {
        return basicService;
    }

    public void setBasicService(BasicServicesInterfaceConfig basicService) {
        this.basicService = basicService;
    }

    @Override
    @ConfigDesc(excluded = true)
    public String getHost() {
        return super.getHost();
    }

    public Map<String, Integer> getProtocolAndPort() {
        if (Strings.isNullOrEmpty(export)) {
            throw new WolfException("export should not empty in service config:" + interfaceClass.getName());
        }
        return UrlUtils.parserExport(export);
    }

}
