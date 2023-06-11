package com.quick.wolf.swither;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.wolf.core.extension.SpiMeta;
import com.quick.wolf.exception.WolfFrameworkException;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/20:34
 */
@SpiMeta(name = "localSwitcherService")
public class LocalSwitcherService implements SwitcherService {

    private static Map<String, Switcher> switcherMap = Maps.newConcurrentMap();

    private Map<String, List<SwitcherListener>> listMap = Maps.newConcurrentMap();

    public static void putSwitcher(Switcher switcher) {
        if (switcher == null) {
            throw new WolfFrameworkException();
        }
        switcherMap.put(switcher.getName(), switcher);
    }


    @Override
    public Switcher getSwitcher(String name) {
        return switcherMap.get(name);
    }

    @Override
    public List<Switcher> getAllSwitcher() {
        return Lists.newArrayList(switcherMap.values());
    }

    @Override
    public void initSwitcher(String name, boolean initialValue) {
        setValue(name, initialValue);
    }

    @Override
    public boolean isOpen(String name) {
        Switcher switcher = switcherMap.get(name);
        return switcher != null && switcher.isOn();
    }

    @Override
    public boolean isOpen(String name, boolean defaultValue) {
        Switcher switcher = switcherMap.get(name);
        if (switcher == null) {
            switcherMap.putIfAbsent(name, new Switcher(defaultValue, name));
            return defaultValue;
        }
        return switcher.isOn();
    }

    @Override
    public void setValue(String name, boolean value) {
        putSwitcher(new Switcher(value, name));

        List<SwitcherListener> listeners = listMap.get(name);
        if (listeners != null) {
            for (SwitcherListener listener : listeners) {
                listener.onValueChange(name, value);
            }
        }
    }

    @Override
    public void registerListener(String name, SwitcherListener listener) {
        List<SwitcherListener> listListener = Collections.synchronizedList(Lists.newArrayList());
        List<SwitcherListener> listeners = listMap.putIfAbsent(name, listListener);
        if (listeners == null) {
            listListener.add(listener);
        } else {
            listeners.add(listener);
        }
    }

    @Override
    public void unRegisterListener(String name, SwitcherListener listener) {
        List<SwitcherListener> listeners = listMap.get(name);
        if (listener == null) {
            // keep empty listeners
            listeners.clear();
        } else {
            listeners.remove(listener);
        }
    }
}
