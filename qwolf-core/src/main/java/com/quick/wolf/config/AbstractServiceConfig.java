package com.quick.wolf.config;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/09:43
 */
public abstract class AbstractServiceConfig extends AbstractConfig {

    private static final long serialVersionUID = -6195423458485631662L;
    /**
     * one service : n protocol , every protocol can user self port, for example:
     * protocol:port,protocol:port,protocol:port
     */
    private String export;

    /**
     * auto set value, but if you have more than one ip, you want to use one of these ip
     * you can set an ip to use
     */
    private String host;

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
