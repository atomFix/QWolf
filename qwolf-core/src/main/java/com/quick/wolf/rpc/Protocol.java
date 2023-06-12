package com.quick.wolf.rpc;

import com.quick.wolf.core.extension.Scope;
import com.quick.wolf.core.extension.Spi;
import com.quick.wolf.rpc.Exporter;
import com.quick.wolf.rpc.Provider;
import com.quick.wolf.rpc.URL;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/11:14
 */
@Spi(scope = Scope.SINGLETON)
public interface Protocol {

    <T> Exporter<T> export(Provider<T> provider, URL url);


    /**
     * <pre>
     * 		1） exporter destroy
     * 		2） referer destroy
     * </pre>
     *
     */
    void destroy();
}
