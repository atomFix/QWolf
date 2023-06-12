package com.quick.wolf.rpc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/17:11
 */
public interface Node {

    void init();

    void destroy();

    boolean isAvailable();

    String desc();

    URL getUrl();

}
