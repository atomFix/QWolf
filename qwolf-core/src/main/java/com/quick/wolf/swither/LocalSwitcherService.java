package com.quick.wolf.swither;

import com.quick.wolf.core.extension.SpiMeta;

import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/20:34
 */
@SpiMeta(name = "localSwitcherService")
public class LocalSwitcherService implements SwitcherService {


    @Override
    public Switcher getSwitcher(String name) {
        return null;
    }

    @Override
    public List<Switcher> getAllSwitcher() {
        return null;
    }

    @Override
    public void initSwitcher(String name, boolean initialValue) {

    }

    @Override
    public boolean isOpen(String name) {
        return false;
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
