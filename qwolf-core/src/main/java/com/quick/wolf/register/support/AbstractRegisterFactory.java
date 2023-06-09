package com.quick.wolf.register.support;

import com.google.common.collect.Maps;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.register.Register;
import com.quick.wolf.register.RegisterFactory;
import com.quick.wolf.rpc.URL;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/19:39
 */
public abstract class AbstractRegisterFactory implements RegisterFactory {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final Map<String, Register> registers = Maps.newConcurrentMap();

    @Override
    public Register getRegister(URL url) {
        String registerUri = getRegisterUri(url);
        lock.lock();
        try {
            Register register = registers.get(registerUri);
            if (register != null) {
                return register;
            }
            register = createRegister(url);
            if (register == null) {
                throw new WolfFrameworkException("Create registry false for url:" + url);
            }

            registers.put(registerUri, register);
            return register;
        } catch (Exception e) {
            throw new WolfFrameworkException("Create registry false for url:" + url + " , " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    private String getRegisterUri(URL url) {
        return url.getUri();
    }

    protected abstract Register createRegister(URL url);
}
