package com.quick.wolf.register;

import com.quick.wolf.rpc.URL;

import java.util.List;

public interface NotifyListener {

    void notify(URL url, List<URL> urls);
    
}
