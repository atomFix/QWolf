package com.quick.wolf.core.extension;

import java.lang.annotation.*;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/10:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Activation {

    /**
     * The smaller the seq number, the higher the position in the returned list<Instance>
     */
    int order() default 0;

    /**
     * spi key, When getting the spi list, match according to the key. When there is a search-key to be filtered in the key, the match is successful
     */
    String[] key() default "";

    /**
     * support retry
     */
    boolean retry() default true;

}
