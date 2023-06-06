package com.quick.wolf.exception;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/06/14:50
 */
public class WolfException extends RuntimeException {

    public WolfException() {
    }

    public WolfException(String message) {
        super(message);
    }

    public WolfException(String message, Throwable cause) {
        super(message, cause);
    }
}
