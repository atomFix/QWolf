package com.quick.wolf.rpc;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/15:24
 */
public interface Response {

    Object getValue();

    Exception getException();

    long getRequestId();

    long getProcessTime();

}
