package com.quick.wolf.register;

import com.quick.wolf.rpc.URL;

import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/18:00
 */
public interface NotifyListener {

    void notify(URL registerUrl, List<URL> urls);

}
