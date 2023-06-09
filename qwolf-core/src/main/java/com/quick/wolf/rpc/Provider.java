package com.quick.wolf.rpc;

import com.quick.wolf.core.extension.Scope;
import com.quick.wolf.core.extension.Spi;

import java.lang.reflect.Method;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/05/21:32
 */
@Spi(scope = Scope.SINGLETON)
public interface Provider<T> extends Caller<T> {

    T getProvider();

    Method lookupMethod(String methodName, String methodDesc);

}
