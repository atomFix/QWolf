package com.quick.wolf.register.support;

import com.google.common.collect.Sets;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.register.Register;
import com.quick.wolf.rpc.URL;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/19:49
 */
@Slf4j
public abstract class AbstractRegister implements Register {

    private URL registerUrl;

    private final Set<URL> registeredSerivceUrls = Sets.newConcurrentHashSet();

    private String regiteryClassName = this.getClass().getSimpleName();

    protected String registryClassName = this.getClass().getSimpleName();

    public AbstractRegister(URL url) {
         this.registerUrl = url.copy();
    }

    @Override
    public void register(URL url) {
        if (url == null) {
            log.warn("[{}] register with malformed param, url is null", registryClassName);
            return;
        }
        log.info("will register! uri : {}", url.getUri());
        doRegister(removeUnnecessaryParams(url.copy()));
        registeredSerivceUrls.add(url);

    }

    private URL removeUnnecessaryParams(URL url) {
        url.getParameters().remove(URLParamType.codec.getName());
        return url;
    }

    protected abstract void doRegister(URL url);

}
