package com.quick.wolf.config.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/18:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ConfigDesc {

    String key() default "";

    boolean excluded() default false;

}
