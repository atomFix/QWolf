package com.quick.wolf.config;

import com.quick.wolf.config.annotation.ConfigDesc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/09:00
 */
public class MethodConfig extends AbstractConfig {

    /**  method name */
    private String name;
    /** request timeout */
    private Long timeout;
    /** request retries number */
    private Integer retries;
    /** Maximum concurrent calls */
    private Integer active;
    /** method argument types , split use ',' */
    private String argumentTypes;
    /** slow threshold */
    private Integer slowThreshold;

    @ConfigDesc(excluded = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @ConfigDesc(excluded = true)
    public String getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(String argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public Integer getSlowThreshold() {
        return slowThreshold;
    }

    public void setSlowThreshold(Integer slowThreshold) {
        this.slowThreshold = slowThreshold;
    }
}
