package com.quick.wolf.config;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/05/21:33
 */
public class DefaultProvider<T> implements Provider<T> {

    private T object;

    @Override
    public T getProvider() {
        return object;
    }
}
