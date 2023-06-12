package com.quick.wolf.rpc;

import com.quick.wolf.exception.WolfFrameworkException;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/11:07
 */
@Slf4j
public abstract class AbstractNode implements Node {

    protected URL url;

    protected volatile boolean init = false;
    protected volatile boolean available = false;


    public AbstractNode(URL url) {
        this.url = url;
    }

    @Override
    public void init() {
        if (init) {
            log.warn(this.getClass().getSimpleName() + " node already init: " + desc());
            return;
        }

        boolean result = doInit();

        if (!result) {
            log.error(this.getClass().getSimpleName() + " node init Error: " + desc());
            throw new WolfFrameworkException(this.getClass().getSimpleName() + " node init Error: " + desc());
        } else {
            log.info(this.getClass().getSimpleName() + " node init Success: " + desc());

            init = true;
            available = true;
        }
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    protected abstract boolean doInit();
}
