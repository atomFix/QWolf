package com.quick.wolf.transport;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/12:04
 */
public interface MessageHandler {

    Object handler(Channel channel, Object message);

}
