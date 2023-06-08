package com.quick.wolf.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.wolf.exception.WolfException;
import com.quick.wolf.rpc.URL;

import java.util.List;
import java.util.Map;

/**
 * 基础属性定义
 * 1、service 与 referer 相同的属性定义
 *
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/09:28
 */
public abstract class AbstractInterfaceConfig extends AbstractConfig {


    private static final long serialVersionUID = 8539708792446376754L;

    protected List<ProtocolConfig> protocolConfigs;

    protected List<RegisterConfig> registerConfigs;

    protected List<URL> registerUrls = Lists.newArrayList();

    protected String application;

    protected String module;

    protected String group;

    protected String version;

    protected String proxy;

    protected String filter;

    /**
     * 最大并发度
     */
    protected Integer actives;

    /**
     * 是否异步
     */
    protected Boolean async;

    /**
     * 服务端mock 类
     */
    protected String mockClass;

    protected Boolean throwException;

    protected Integer requestTimeout;

    protected Boolean register;

    protected Boolean accessLog;

    protected Integer retries;

    protected String codec;

    protected Integer slowThreshold;

    protected Integer connectTimeout;

    public List<ProtocolConfig> getProtocolConfigs() {
        return protocolConfigs;
    }

    public void setProtocolConfigs(List<ProtocolConfig> protocolConfigs) {
        this.protocolConfigs = protocolConfigs;
    }

    public List<RegisterConfig> getRegisterConfigs() {
        return registerConfigs;
    }

    public void setRegisterConfigs(List<RegisterConfig> registerConfigs) {
        this.registerConfigs = registerConfigs;
    }

    public List<URL> getRegisterUrls() {
        return registerUrls;
    }

    public void setRegisterUrls(List<URL> registerUrls) {
        this.registerUrls = registerUrls;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getActives() {
        return actives;
    }

    public void setActives(Integer actives) {
        this.actives = actives;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public String getMockClass() {
        return mockClass;
    }

    public void setMockClass(String mockClass) {
        this.mockClass = mockClass;
    }

    public Boolean getThrowException() {
        return throwException;
    }

    public void setThrowException(Boolean throwException) {
        this.throwException = throwException;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Boolean getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(Boolean accessLog) {
        this.accessLog = accessLog;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Integer getSlowThreshold() {
        return slowThreshold;
    }

    public void setSlowThreshold(Integer slowThreshold) {
        this.slowThreshold = slowThreshold;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean hasProtocol() {
        return protocolConfigs != null && protocolConfigs.size() > 0;
    }

    public boolean hasRegister() {
        return registerConfigs != null && registerConfigs.size() > 0;
    }

    protected void loadRegisterUrls() {
        registerUrls.clear();
        if (registerConfigs != null && !registerConfigs.isEmpty()) {
            for (RegisterConfig config : registerConfigs) {
                List<URL> urls = config.toUrls();
                if (urls != null && !urls.isEmpty()) {
                    registerUrls.addAll(urls);
                }
            }
        }
    }

    protected void checkInterfaceAndMethods(Class<?> interfaceClass, List<MethodConfig> methods) {
        if (interfaceClass == null) {
            throw new IllegalStateException("interfaceClass is null!");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
    }

    protected String getLocalHostAddress() {
        String localAddress = null;
        Map<String, Integer> registerHostPorts = Maps.newHashMap();
        for (URL url : registerUrls) {
            registerHostPorts.put(url.getHost(), url.getPort());
        }

    }

}
