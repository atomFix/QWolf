package com.quick.wolf.swither;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.wolf.core.extension.SpiMeta;

import java.util.AbstractList;
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
        return false;
    }

    @Override
    public void setValue(String name, boolean value) {

    }

    @Override
    public void registerListener(String name, SwitcherListener listener) {

    }

    @Override
    public void unRegisterListener(String name, SwitcherListener listener) {

    }
}
