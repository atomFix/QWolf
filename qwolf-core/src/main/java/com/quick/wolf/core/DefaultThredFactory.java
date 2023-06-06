package com.quick.wolf.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: Thread Factory
 * @author: liukairong1
 * @date: 2023/06/06/10:32
 */
public class DefaultThredFactory implements ThreadFactory {

    /**
     * thread group
     **/
    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("QWolf");
    /**
     * thread counter
     **/
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String prefix;
    private final boolean daemon;

    /**
     * use static method 'create' replace
     *
     * @param prefix thread name prefix
     * @param daemon thread is it a daemon
     */
    private DefaultThredFactory(String prefix, boolean daemon) {
        this.prefix = prefix;
        this.daemon = daemon;
    }

    public static ThreadFactory create(String prefix, boolean daemon) {
        return new DefaultThredFactory(prefix, daemon);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(THREAD_GROUP, r, THREAD_GROUP.getName() + "-" + prefix + "-" + threadNumber.getAndIncrement());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
