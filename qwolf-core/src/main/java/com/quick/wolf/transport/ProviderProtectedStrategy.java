package com.quick.wolf.transport;

import com.quick.wolf.core.extension.Scope;
import com.quick.wolf.core.extension.Spi;
import com.quick.wolf.rpc.Provider;
import com.quick.wolf.rpc.Request;
import com.quick.wolf.rpc.Response;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/14:11
 */
@Spi(scope = Scope.PROTOTYPE)
public interface ProviderProtectedStrategy {

    Response call(Request request, Provider<?> provider);

    void setMethodCounter(AtomicInteger methodCounter);

}
