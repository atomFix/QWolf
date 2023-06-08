package com.quick.wolf.config;

import com.quick.wolf.config.annotation.ConfigDesc;

import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/09:31
 */
public class ProtocolConfig extends AbstractConfig {
    private static final long serialVersionUID = 6992763678447280564L;

    private String name;

    private String serialization;

    private String codec;

    private Integer ioThreads;

    protected Integer requestTimeout;

    protected Integer minClientConnection;

    protected Integer maxClientConnection;

    protected Integer minWorkThreads;

    protected Integer maxWorkThreads;

    protected Integer maxContentLength;

    protected Boolean poolLifo;

    protected Boolean lazyInit;

    protected String endpointFactory;

    protected String cluster;

    protected String loadbalance;

    /**
     * high available strategy
     */
    protected String haStrategy;

    protected Integer workQueueSize;

    /** server accept connection count */
    protected Integer accpectConnection;

    /**
     * proxy type, for example, jdk、javassist
     */
    protected String proxy;

    /** filter collection, use ',' split, if value is null or blank then show use default filter */
    protected String filters;

    protected Integer retries;

    protected Boolean async;

    private Boolean isDefault;

    private Map<String, String> parameters;

    @ConfigDesc(required = true, key = "protocol")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Integer getIoThreads() {
        return ioThreads;
    }

    public void setIoThreads(Integer ioThreads) {
        this.ioThreads = ioThreads;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getMinClientConnection() {
        return minClientConnection;
    }

    public void setMinClientConnection(Integer minClientConnection) {
        this.minClientConnection = minClientConnection;
    }

    public Integer getMaxClientConnection() {
        return maxClientConnection;
    }

    public void setMaxClientConnection(Integer maxClientConnection) {
        this.maxClientConnection = maxClientConnection;
    }

    public Integer getMinWorkThreads() {
        return minWorkThreads;
    }

    public void setMinWorkThreads(Integer minWorkThreads) {
        this.minWorkThreads = minWorkThreads;
    }

    public Integer getMaxWorkThreads() {
        return maxWorkThreads;
    }

    public void setMaxWorkThreads(Integer maxWorkThreads) {
        this.maxWorkThreads = maxWorkThreads;
    }

    public Integer getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(Integer maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public Boolean getPoolLifo() {
        return poolLifo;
    }

    public void setPoolLifo(Boolean poolLifo) {
        this.poolLifo = poolLifo;
    }

    public Boolean getLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(Boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getEndpointFactory() {
        return endpointFactory;
    }

    public void setEndpointFactory(String endpointFactory) {
        this.endpointFactory = endpointFactory;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getHaStrategy() {
        return haStrategy;
    }

    public void setHaStrategy(String haStrategy) {
        this.haStrategy = haStrategy;
    }

    public Integer getWorkQueueSize() {
        return workQueueSize;
    }

    public void setWorkQueueSize(Integer workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    public Integer getAccpectConnection() {
        return accpectConnection;
    }

    public void setAccpectConnection(Integer accpectConnection) {
        this.accpectConnection = accpectConnection;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
