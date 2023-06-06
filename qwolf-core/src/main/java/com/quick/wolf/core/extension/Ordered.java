package com.quick.wolf.core.extension;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/10:16
 */
public interface Ordered {
    /**
     * Useful constant for the highest precedence value.
     * @see java.lang.Integer#MIN_VALUE
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Useful constant for the lowest precedence value.
     * @see java.lang.Integer#MAX_VALUE
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
}
