package com.quick.wolf.core.extension;

import java.util.Comparator;

/**
 * @author: liukairong1
 * @date: 2023/06/06/10:24
 */
public class ActivationComparator<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        Activation p1 = o1.getClass().getAnnotation(Activation.class);
        Activation p2 = o2.getClass().getAnnotation(Activation.class);
        if (p1 == null) {
            return 1;
        } else if (p2 == null) {
            return -1;
        } else {
            return p1.order() - p2.order();
        }
    }
}
