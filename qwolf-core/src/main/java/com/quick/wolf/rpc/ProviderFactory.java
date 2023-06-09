package com.quick.wolf.rpc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/11:22
 */
public interface ProviderFactory {

    <T> Provider<T> getProvider(T proxyImpl, URL serviceUrl, Class<T> interfaceClass);

}
