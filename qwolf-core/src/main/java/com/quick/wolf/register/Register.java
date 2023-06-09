package com.quick.wolf.register;

import com.quick.wolf.rpc.URL;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/17:51
 */
public interface Register extends RegisterService, DiscoveryService {

    URL getUrl();

}
