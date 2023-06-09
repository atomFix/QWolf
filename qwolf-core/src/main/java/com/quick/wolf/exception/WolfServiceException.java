package com.quick.wolf.exception;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/10:06
 */
public class WolfServiceException extends WolfAbstractException {
    private static final long serialVersionUID = 7178878625289660899L;

    public WolfServiceException() {
        super();
    }

    public WolfServiceException(WolfErrorMsg motanErrorMsg) {
        super(motanErrorMsg);
    }

    public WolfServiceException(String message) {
        super(message);
    }

    public WolfServiceException(String message, WolfErrorMsg motanErrorMsg) {
        super(message, motanErrorMsg);
    }

    public WolfServiceException(String message, WolfErrorMsg motanErrorMsg, boolean writableStackTrace) {
        super(message, motanErrorMsg, writableStackTrace);
    }

    public WolfServiceException(String message, Throwable cause, WolfErrorMsg motanErrorMsg, boolean writableStackTrace) {
        super(message, cause, motanErrorMsg, writableStackTrace);
    }

    public WolfServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WolfServiceException(String message, Throwable cause, WolfErrorMsg motanErrorMsg) {
        super(message, cause, motanErrorMsg);
    }

    public WolfServiceException(Throwable cause) {
        super(cause);
    }

    public WolfServiceException(Throwable cause, WolfErrorMsg motanErrorMsg) {
        super(cause, motanErrorMsg);
    }
}
