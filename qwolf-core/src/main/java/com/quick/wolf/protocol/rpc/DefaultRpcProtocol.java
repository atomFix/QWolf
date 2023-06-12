package com.quick.wolf.protocol.rpc;

import com.quick.wolf.protocol.AbstractProtocol;
import com.quick.wolf.rpc.Exporter;
import com.quick.wolf.rpc.Provider;
import com.quick.wolf.rpc.URL;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/11:37
 */
public class DefaultRpcProtocol extends AbstractProtocol {



    @Override
    protected <T> Exporter<T> createExporter(Provider<T> provider, URL url) {
        return new DefaultRpcExporter<>();
    }
}
