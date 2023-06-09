package com.quick.wolf.rpc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/05/21:33
 */
public class DefaultProvider<T> extends AbstractProvider<T> {

    private final T proxyImpl;

    public DefaultProvider(T proxyImpl, URL url, Class<T> clazz) {
        super(url, clazz);
        this.proxyImpl = proxyImpl;
    }

    @Override
    public T getProvider() {
        return proxyImpl;
    }

    @Override
    public Response invoke(Request request) {
        // TODO
        return null;
    }
}
