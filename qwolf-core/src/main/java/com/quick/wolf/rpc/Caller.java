package com.quick.wolf.rpc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/15:24
 */
public interface Caller<T> extends Node {

    Class<T> getInterface();

    Response call(Request request);
}
