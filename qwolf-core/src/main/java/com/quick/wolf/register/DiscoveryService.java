package com.quick.wolf.register;

import com.quick.wolf.rpc.URL;

import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/17:59
 */
public interface DiscoveryService {

    void subscibe(URL url, NotifyListener listener);

    void unsubscibe(URL url, NotifyListener listener);

    List<URL> discover(URL url);

}
