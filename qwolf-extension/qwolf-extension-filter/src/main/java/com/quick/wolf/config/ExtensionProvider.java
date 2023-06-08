package com.quick.wolf.config;

import com.quick.wolf.rpc.Provider;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/09:30
 */
public class ExtensionProvider implements Provider<String> {

    @Override
    public String getProvider() {
        return "extension provider";
    }
}
