package com.quick.wolf;

import com.quick.wolf.config.MethodConfig;
import com.quick.wolf.config.Provider;
import com.quick.wolf.core.extension.ExtensionLoader;

import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/09:27
 */
public class Main {
    public static void main(String[] args) {
        List<Provider> extensions = ExtensionLoader.getExtensionLoader(Provider.class).getExtensions(null);
        for (Provider extension : extensions) {
            System.out.println(extension.getProvider());
        }
    }
}
