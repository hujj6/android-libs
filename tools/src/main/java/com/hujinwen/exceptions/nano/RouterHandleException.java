package com.hujinwen.exceptions.nano;

/**
 * Created by hu-jinwen on 12/17/20
 * <p>
 * 处理处理异常
 */
public class RouterHandleException extends RuntimeException {

    public RouterHandleException() {
    }

    public RouterHandleException(String message) {
        super(message);
    }

    public RouterHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterHandleException(Throwable cause) {
        super(cause);
    }

    public RouterHandleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
