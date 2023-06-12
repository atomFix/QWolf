package com.quick.wolf.transport;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/12:25
 */
public class TransportException extends IOException {

    private static final long serialVersionUID = 7057762354907226994L;

    private final InetSocketAddress localAddress;
    private final InetSocketAddress remoteAddress;

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public TransportException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message, Throwable cause) {
        super(message, cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

}
