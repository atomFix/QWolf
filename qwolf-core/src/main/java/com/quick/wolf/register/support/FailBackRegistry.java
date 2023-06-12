package com.quick.wolf.register.support;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.register.NotifyListener;
import com.quick.wolf.rpc.URL;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/09:54
 */
@Slf4j
public abstract class FailBackRegistry extends AbstractRegister {

    private final Set<URL> failedRegistered = Sets.newConcurrentHashSet();

    private final Set<URL> failedUnregistered = Sets.newConcurrentHashSet();

    private final Map<URL, Set<NotifyListener>> failedSubscribed = Maps.newConcurrentMap();

    private final Map<URL, Set<NotifyListener>> failedUnsbscribed = Maps.newConcurrentMap();

    private static final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1);

    static {
        // TODO tomcat 注销操作
    }


    public FailBackRegistry(URL url) {
        super(url);
        int retryPeriod = url.getIntParameter(URLParamType.registryRetryPeriod.getName(), URLParamType.registryRetryPeriod.getIntValue());

        retryExecutor.scheduleAtFixedRate(() -> {
            try {
                retry();
            } catch (Exception e) {
                log.warn("[{}] False when retry in failBack registry", e.getMessage(), e);
            }
        }, retryPeriod, retryPeriod, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void doRegister(URL url) {
        failedRegistered.remove(url);
        failedUnregistered.remove(url);

        try {
            super.register(url);
        } catch (Exception e) {
            if (!isCheckingUrls(getUrl(), url)) {
                throw e;
            }
            failedRegistered.add(url);
        }
    }

    @Override
    protected void doUnregister(URL url) {
        failedRegistered.remove(url);
        failedUnregistered.remove(url);

        try {
            super.unregister(url);
        } catch (Exception e) {
            if (!isCheckingUrls(getUrl(), url)) {
                throw e;
            }
            failedUnregistered.add(url);
        }
    }

    @Override
    protected void doSubscribe(URL url, NotifyListener listener) {
        removeForFailedSubAndUnsub(url, listener);

        try {
            super.subscribe(url, listener);
        } catch (Exception e) {
            List<URL> cacheUrls = getCacheUrls(url);
            if (cacheUrls != null && cacheUrls.size() > 0) {
                listener.notify(getUrl(), cacheUrls);
            } else if (isCheckingUrls(getUrl(), url)) {
                log.warn(String.format("[%s] false to subscribe %s from %s", registryClassName, url, getUrl()), e);
                throw new WolfFrameworkException(String.format("[%s] false to subscribe %s from %s", registryClassName, url, getUrl()), e);
            }
            addToFailedMap(failedSubscribed, url, listener);
        }
    }

    @Override
    protected void doUnsubscribe(URL url, NotifyListener listener) {
        removeForFailedSubAndUnsub(url, listener);

        try {
            super.unsubscibe(url, listener);
        } catch (Exception e) {
            List<URL> cachedUrls = getCacheUrls(url);
            if (cachedUrls != null && cachedUrls.size() > 0) {
                listener.notify(getUrl(), cachedUrls);
            } else if (isCheckingUrls(getUrl(), url)) {
                log.warn(String.format("[%s] false to subscribe %s from %s", registryClassName, url, getUrl()), e);
                throw new WolfFrameworkException(String.format("[%s] false to subscribe %s from %s", registryClassName, url, getUrl()), e);
            }
            addToFailedMap(failedSubscribed, url, listener);
        }
    }

    private void addToFailedMap(Map<URL, Set<NotifyListener>> failedSubscribed, URL url, NotifyListener listener) {
        Set<NotifyListener> listeners = failedSubscribed.get(url);
        if (listeners == null) {
            failedSubscribed.putIfAbsent(url, Sets.newConcurrentHashSet());
            listeners = failedSubscribed.get(url);
        }
        listeners.add(listener);
    }

    private void retry() {
        retryFailRegistered();
        retryFailUnregistered();
        retryFailSubscribed();
        retryFailUnsubscribed();
    }

    private void retryFailUnsubscribed() {
        if (!failedUnsbscribed.isEmpty()) {
            HashMap<URL, Set<NotifyListener>> failed = Maps.newHashMap(failedUnsbscribed);
            for (Map.Entry<URL, Set<NotifyListener>> entry : Maps.newHashMap(failed).entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    failed.remove(entry.getKey());
                }
            }

            if (!failed.isEmpty()) {
                for (Map.Entry<URL, Set<NotifyListener>> entry : failed.entrySet()) {
                    URL url = entry.getKey();
                    Set<NotifyListener> listeners = entry.getValue();
                    Iterator<NotifyListener> iterator = listeners.iterator();
                    while (iterator.hasNext()) {
                        super.unsubscibe(url, iterator.next());
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void retryFailSubscribed() {
        if (!failedSubscribed.isEmpty()) {
            HashMap<URL, Set<NotifyListener>> failed = Maps.newHashMap(failedSubscribed);
            for (Map.Entry<URL, Set<NotifyListener>> entry : Maps.newHashMap(failed).entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    failed.remove(entry.getKey());
                }
            }

            if (!failed.isEmpty()) {
                for (Map.Entry<URL, Set<NotifyListener>> entry : failed.entrySet()) {
                    URL url = entry.getKey();
                    Set<NotifyListener> listeners = entry.getValue();
                    Iterator<NotifyListener> iterator = listeners.iterator();
                    while (iterator.hasNext()) {
                        super.subscribe(url, iterator.next());
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void retryFailUnregistered() {
        if (!failedUnregistered.isEmpty()) {
            Set<URL> failed = Sets.newHashSet(failedUnregistered);
            try {
                for (URL url : failed) {
                    super.unregister(url);
                    failedUnregistered.remove(url);
                }
            } catch (Exception e) {
                log.error("");
            }
        }
    }

    private void retryFailRegistered() {
        if (!failedRegistered.isEmpty()) {
            Set<URL> failed = Sets.newHashSet(failedRegistered);
            try {
                for (URL url : failed) {
                    super.register(url);
                    failedRegistered.remove(url);
                }
            } catch (Exception e) {
                log.error("");
            }
        }
    }

    private boolean isCheckingUrls(URL... urls) {
        for (URL url : urls) {
            if (!Boolean.parseBoolean(url.getParameter(URLParamType.check.getName(), URLParamType.check.getValue()))) {
                return false;
            }
        }
        return true;
    }

    private void removeForFailedSubAndUnsub(URL url, NotifyListener listener) {
        Set<NotifyListener> listeners = failedSubscribed.get(url);
        if (listeners != null) {
            listeners.remove(listener);
        }
        listeners = failedSubscribed.get(url);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
}
