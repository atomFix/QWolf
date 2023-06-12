package com.quick.wolf.rpc;

import java.net.http.HttpHeaders;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/11:12
 */
public abstract class AbstractExporter<T> extends AbstractNode implements Exporter<T>  {

    protected Provider<T> provider;

    public AbstractExporter(Provider<T> provider, URL url) {
        super(url);
        this.provider = provider;
    }

    @Override
    public Provider<T> getProvider() {
        return provider;
    }

    @Override
    public String desc() {
        return "[" + this.getClass().getSimpleName() + "] url=" + url;
    }


    protected void updateRealServerPort(int port){
        getUrl().setPort(port);
    }
}
