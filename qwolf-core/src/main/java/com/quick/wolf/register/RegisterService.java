package com.quick.wolf.register;

import com.quick.wolf.rpc.URL;

import java.util.Collection;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/17:58
 */
public interface RegisterService {

    void register(URL url);

    void unregister(URL url);

    void avaliable(URL url);

    void unAvaliable(URL url);

    Collection<URL> getRegisteredServiceUrls();

}
