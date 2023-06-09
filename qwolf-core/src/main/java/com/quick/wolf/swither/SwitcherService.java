package com.quick.wolf.swither;

import com.quick.wolf.core.extension.Scope;
import com.quick.wolf.core.extension.Spi;

import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/20:18
 */
@Spi(scope = Scope.SINGLETON)
public interface SwitcherService {

    Switcher getSwitcher(String name);

    List<Switcher> getAllSwitcher();

    void initSwitcher(String name, boolean initialValue);

    boolean isOpen(String name);

    boolean isOpen(String name, boolean defaultValue);

    void setValue(String name, boolean value);

    void registerListener(String name, SwitcherListener listener);

    void unRegisterListener(String name, SwitcherListener listener);

}
