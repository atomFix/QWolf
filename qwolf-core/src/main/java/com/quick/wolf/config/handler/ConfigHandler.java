package com.quick.wolf.config.handler;

import com.quick.wolf.core.extension.Scope;
import com.quick.wolf.core.extension.Spi;
import com.quick.wolf.rpc.Exporter;
import com.quick.wolf.rpc.URL;

import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/10:51
 */
@Spi(scope = Scope.SINGLETON)
public interface ConfigHandler {

    <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<URL> registers, URL refUrl);

    <T> void unexport(List<Exporter<T>> exporters, Collection<URL> registryUrls);

}
