package com.zxst.shoop.service.ex;
//删除异常
public class DeleteInfoException extends ServiceException{

    public DeleteInfoException() {
    }

    public DeleteInfoException(String message) {
        super(message);
    }

    public DeleteInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteInfoException(Throwable cause) {
        super(cause);
    }

    public DeleteInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
