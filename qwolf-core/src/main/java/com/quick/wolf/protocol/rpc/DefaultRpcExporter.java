package com.quick.wolf.protocol.rpc;

import com.quick.wolf.rpc.AbstractExporter;
import com.quick.wolf.rpc.Provider;
import com.quick.wolf.rpc.URL;

import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/11:14
 */
public class DefaultRpcExporter<T> extends AbstractExporter<T> {

    private final Map<String, >


    public DefaultRpcExporter(Provider<T> provider, URL url) {
        super(provider, url);
    }

    @Override
    protected boolean doInit() {
        return false;
    }

    @Override
    public void unexport() {

    }

    @Override
    public void destroy() {

    }
}
