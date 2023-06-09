package com.quick.wolf.filter;

import com.quick.wolf.core.extension.Spi;
import com.quick.wolf.rpc.Caller;
import com.quick.wolf.rpc.Request;
import com.quick.wolf.rpc.Response;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/16:53
 */
@Spi
public interface Filter {

    Response filter(Caller<?> caller, Request request);

}
