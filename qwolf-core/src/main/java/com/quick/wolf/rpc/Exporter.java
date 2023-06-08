package com.quick.wolf.rpc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/17:14
 */
public interface Exporter<T> extends Node {

    Provider<T> getProvider();

    void unexport();

}
