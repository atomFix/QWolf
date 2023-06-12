package com.quick.wolf.transport;

import com.quick.wolf.rpc.Request;
import com.quick.wolf.rpc.Response;
import com.quick.wolf.rpc.URL;

import java.net.InetSocketAddress;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/12:11
 */
public interface Channel {

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    Response request(Request request) throws TransportException;

    boolean open();

    void close();

    void close(int timeout);

    boolean isClosed();

    boolean isAvaliable();

    URL getUrl();
}
