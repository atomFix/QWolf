package com.quick.wolf;

import com.quick.wolf.config.Provider;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/05/21:35
 */
public class Main {
    public static void main(String[] args) {
        ServiceLoader<Provider> load = ServiceLoader.load(Provider.class);
        Iterator<Provider> iterator = load.iterator();
        while (iterator.hasNext()) {
            Provider provider = iterator.next();
            System.out.println(provider.getProvider());
        }
    }
}
