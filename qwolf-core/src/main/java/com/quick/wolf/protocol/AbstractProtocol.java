package com.quick.wolf.protocol;

import com.google.common.collect.Maps;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.rpc.*;
import com.quick.wolf.utils.WolfFrameworkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/11:28
 */
@Slf4j
public abstract class AbstractProtocol implements Protocol {

    private static final Map<String, Exporter<?>> EXPORTER_MAP = Maps.newConcurrentMap();

    @Override
    public <T> Exporter<T> export(Provider<T> provider, URL url) {
        if (url == null) {
            throw new WolfFrameworkException(this.getClass().getSimpleName() + " export Error: url is null");
        }

        if (provider == null) {
            throw new WolfFrameworkException(this.getClass().getSimpleName() + " export Error: provider is null, url=" + url);
        }

        String protocolKey = WolfFrameworkUtil.getProtocolKey(url);

        synchronized (EXPORTER_MAP) {
            Exporter<T> exporter = (Exporter<T>) EXPORTER_MAP.get(protocolKey);
            if (exporter != null) {
                throw new WolfFrameworkException();
            }

            exporter = createExporter(provider, url);
            exporter.init();

            protocolKey =  WolfFrameworkUtil.getProtocolKey(url);// rebuild protocolKey，maybe port change when using random port
            EXPORTER_MAP.put(protocolKey, exporter);

            log.info(this.getClass().getSimpleName() + " export Success: url=" + url);
            return exporter;
        }
    }

    protected abstract <T> Exporter<T> createExporter(Provider<T> provider, URL url);

    @Override
    public void destroy() {
        for (String key : EXPORTER_MAP.keySet()) {
            Node node = EXPORTER_MAP.remove(key);

            if (node != null) {
                try {
                    node.destroy();

                    log.info(this.getClass().getSimpleName() + " destroy node Success: " + node);
                } catch (Throwable t) {
                    log.error(this.getClass().getSimpleName() + " destroy Error", t);
                }
            }
        }
    }
}
