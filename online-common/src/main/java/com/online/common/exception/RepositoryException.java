package com.online.common.exception;

public class RepositoryException extends RuntimeException{


    private static final long serialVersionUID = -6438755184394143413L;

    protected int exceptionCode = -1;

    public int getExceptionCode() {
        return this.exceptionCode;
    }

    public RepositoryException(int exceptionCode,String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public RepositoryException(int exceptionCode,String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}
