package com.quick.wolf.core.extension;

import java.lang.annotation.*;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/14:04
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Spi {

    Scope scope() default Scope.PROTOTYPE;

}
