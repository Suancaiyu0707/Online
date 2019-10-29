package com.online.common.api.expcetion;

import org.springframework.validation.BindingResult;

public class ValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(Throwable cause, BindingResult bindingResult) {
        super(cause);
        this.bindingResult = bindingResult;
    }

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public final BindingResult getBindingResult() {
        return this.bindingResult;
    }
}
