package com.quick.wolf.exception;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/07/09:14
 */
public class WolfFrameworkException extends WolfException {
    public WolfFrameworkException() {
        super();
    }

    public WolfFrameworkException(String message) {
        super(message);
    }

    public WolfFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
