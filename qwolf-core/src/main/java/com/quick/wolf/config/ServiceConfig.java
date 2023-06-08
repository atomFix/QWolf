package com.quick.wolf.config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.config.annotation.ConfigDesc;
import com.quick.wolf.exception.WolfException;
import com.quick.wolf.rpc.Provider;
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

    private List<Provider<T>> exporters = Lists.newCopyOnWriteArrayList();

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

    }

    private void afterExport() {


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

    public List<Provider<T>> getExporters() {
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
