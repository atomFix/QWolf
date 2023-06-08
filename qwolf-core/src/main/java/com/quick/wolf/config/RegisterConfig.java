package com.quick.wolf.config;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.config.annotation.ConfigDesc;
import com.quick.wolf.rpc.URL;
import com.quick.wolf.utils.NetUtils;
import com.quick.wolf.utils.StringUtils;
import com.quick.wolf.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/09:32
 */
@Slf4j
public class RegisterConfig extends AbstractConfig {
    private static final long serialVersionUID = -2089059670359899011L;

    private String name;

    private String registerProtocol;

    private String address;

    private Integer port;

    /** 注册中心请求超时时间*/
    private Integer requestTimeout;

    /** 注册中心连接超时时间*/
    private Integer connectTimeout;

    private Integer registerSessionTimeout;

    private Integer registerRetryPeriod;

    /** 启动时检查注册中心是否存在*/
    private String check;

    /** 在注册中心上服务是否暴露*/
    private Boolean register;

    /** 在注册中心上是否饮用*/
    private Boolean subscribe;

    private Boolean isDefault;

    /** 被代理的注册中心*/
    private RegisterConfig proxyRegister;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ConfigDesc(key = "protocol")
    public String getRegisterProtocol() {
        return registerProtocol;
    }

    public void setRegisterProtocol(String registerProtocol) {
        this.registerProtocol = registerProtocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getProt() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getRegisterSessionTimeout() {
        return registerSessionTimeout;
    }

    public void setRegisterSessionTimeout(Integer registerSessionTimeout) {
        this.registerSessionTimeout = registerSessionTimeout;
    }

    public Integer getRegisterRetryPeriod() {
        return registerRetryPeriod;
    }

    public void setRegisterRetryPeriod(Integer registerRetryPeriod) {
        this.registerRetryPeriod = registerRetryPeriod;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public RegisterConfig getProxyRegister() {
        return proxyRegister;
    }

    public void setProxyRegister(RegisterConfig proxyRegister) {
        this.proxyRegister = proxyRegister;
    }

    public List<URL> toUrls() {
        String address = getAddress();
        if (Strings.isNullOrEmpty(address)) {
            address = StringUtils.join(NetUtils.LOCALHOST, WolfConstants.PARAM_SEPARATOR, WolfConstants.DEFAULT_INT_VALUE);
        }
        Map<String, String> params = Maps.newHashMap();
        params.putAll(getAddressParam());
        appendConfigParams(params);
        params.put(URLParamType.path.getName(), RegisterConfig.class.getName());
        params.put(URLParamType.refreshTimestamp.getName(), String.valueOf(System.currentTimeMillis()));
        if (!params.containsKey(URLParamType.protocol.getName())) {
            if (address.contains("://")) {
                params.put(URLParamType.protocol.getName(), address.substring(0, address.indexOf("://")));
            } else {
                params.put(URLParamType.protocol.getName(), WolfConstants.REGISTRY_PROTOCOL_LOCAL);
            }
        }

        if (proxyRegister != null) {
            String proxyRegisterUrl = UrlUtils.urlsToString(proxyRegister.toUrls());
            if (!Strings.isNullOrEmpty(proxyRegisterUrl)) {
                params.put(URLParamType.proxyRegistryUrlString.getName(), proxyRegisterUrl);
            } else {
                log.warn("proxy register url is empty!");
            }
        }

        return UrlUtils.parseURLs(address, params);
    }

    public Map<String, String> getAddressParam() {
        if (!Strings.isNullOrEmpty(address)) {
            int index = address.indexOf("?");
            if (index > -1) {
                int end = address.length();
                if (address.contains(WolfConstants.COMMA_SEPARATOR)) {
                    end = address.indexOf(WolfConstants.COMMA_SEPARATOR);
                }
                return UrlUtils.parserQueryParam(address.substring(index + 1, end));
            }
        }
        return Maps.newHashMap();
    }
}
