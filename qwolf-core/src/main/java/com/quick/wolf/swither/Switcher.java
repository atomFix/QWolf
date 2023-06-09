package com.quick.wolf.swither;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/20:20
 */
public class Switcher {

    private boolean on;

    private final String name;

    public Switcher(boolean on, String name) {
        this.on = on;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return on;
    }

    public void onSwitcher() {
        this.on = true;
    }

    public void offSwitcher() {
        this.on = false;
    }
}
