package com.online.common.api.expcetion;

public class UnCheckedWebException extends RuntimeException {

    private static final long serialVersionUID = -6438755184394143413L;

    protected int exceptionCode = -1;

    public int getExceptionCode() {
        return this.exceptionCode;
    }

    public UnCheckedWebException(int exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
    }

    public UnCheckedWebException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public UnCheckedWebException(int exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}