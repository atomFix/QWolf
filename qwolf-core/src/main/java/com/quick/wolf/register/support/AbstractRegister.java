package com.quick.wolf.register.support;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.register.NotifyListener;
import com.quick.wolf.register.Register;
import com.quick.wolf.rpc.URL;
import com.quick.wolf.utils.WolfSwitcherUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/19:49
 */
@Slf4j
public abstract class AbstractRegister implements Register {

    private static final Map<URL, Map<String, List<URL>>> subscribedCategoryResponse = Maps.newConcurrentMap();

    private URL registerUrl;

    private final Set<URL> registeredServiceUrls = Sets.newConcurrentHashSet();

    private final String registerClassName = this.getClass().getSimpleName();

    protected String registryClassName = this.getClass().getSimpleName();

    public AbstractRegister(URL url) {
        this.registerUrl = url.copy();

        WolfSwitcherUtils.initSwitcher(WolfConstants.REGISTRY_HEARTBEAT_SWITCHER, false);
        WolfSwitcherUtils.registerSwitcherListener(WolfConstants.REGISTRY_HEARTBEAT_SWITCHER, (key, value) -> {
            if (key != null && value != null) {
                if (value) {
                    available(null);
                } else {
                    unAvaliable(null);
                }
            }
        });
    }

    @Override
    public void register(URL url) {
        if (url == null) {
            log.warn("[{}] register with malformed param, url is null", registryClassName);
            return;
        }
        log.info("will register! uri : {}", url.getUri());
        doRegister(removeUnnecessaryParams(url.copy()));
        registeredServiceUrls.add(url);
        if (WolfSwitcherUtils.isOpen(WolfConstants.REGISTRY_HEARTBEAT_SWITCHER)) {
            available(url);
        }
    }

    @Override
    public void unregister(URL url) {
        if (url == null) {
            log.error("unregister value is null !");
            return;
        }
        log.info("wolf rpc service unregister ! url : {}", url.getIdentity());
        doUnregister(url);
        registeredServiceUrls.remove(url);
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            log.warn("[{}] subscribe with malformed param, url:{}, listener:{}", registerClassName, url, listener);
            return;
        }
        doSubscribe(url.copy(), listener);
    }

    @Override
    public void unsubscibe(URL url, NotifyListener listener) {
        if (url == null || listener == null) {
            log.warn("[{}] unsubscribe with malformed param, url:{}, listener:{}", registryClassName, url, listener);
            return;
        }
        doUnsubscribe(url, listener);
    }

    private URL removeUnnecessaryParams(URL url) {
        url.getParameters().remove(URLParamType.codec.getName());
        return url;
    }

    @Override
    public URL getUrl() {
        return registerUrl;
    }

    @Override
    public Collection<URL> getRegisteredServiceUrls() {
        return registeredServiceUrls;
    }

    @Override
    public void available(URL url) {
        if (url != null) {
            doAvailable(removeUnnecessaryParams(url));
        } else {
            doAvailable(null);
        }
    }

    @Override
    public void unAvaliable(URL url) {
        if (url != null) {
            doUnavailable(removeUnnecessaryParams(url));
        } else {
            doUnavailable(null);
        }
    }

    @Override
    public List<URL> discover(URL url) {
        if (url == null) {
            log.warn("[{}] discover with malformed param, refUrl is null", registryClassName);
            return Collections.emptyList();
        }
        url = url.copy();
        List<URL> urls = Lists.newArrayList();
        subscribedCategoryResponse.


        return null;
    }

    protected abstract void doRegister(URL url);

    protected abstract void doUnregister(URL url);

    protected abstract void doSubscribe(URL copy, NotifyListener listener);

    protected abstract void doUnsubscribe(URL url, NotifyListener listener);

    protected abstract void doAvailable(URL url);

    protected abstract void doUnavailable(URL url);


}
