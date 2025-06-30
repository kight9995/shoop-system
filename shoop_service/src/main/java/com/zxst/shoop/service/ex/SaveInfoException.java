package com.zxst.shoop.service.ex;

public class SaveInfoException extends ServiceException {
    public SaveInfoException() {
    }

    public SaveInfoException(String message) {
        super(message);
    }

    public SaveInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveInfoException(Throwable cause) {
        super(cause);
    }

    public SaveInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
