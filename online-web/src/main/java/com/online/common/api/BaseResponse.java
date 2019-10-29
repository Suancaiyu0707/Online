package com.online.common.api;

import com.online.common.constant.BaseApiStatus;
import com.online.common.model.domain.BaseDO;
import lombok.Getter;

public abstract class BaseResponse extends BaseDO {

    @Getter
    protected int status = BaseApiStatus.SUCCESS;

    @Getter
    protected String error = "";

    protected BaseResponse(int status, String error) {
        this.status = status;
        this.error = error;
    }

    public abstract static class Builder<R extends BaseResponse, B extends Builder<R, B>> {

        private B theBuilder;

        protected Integer status;
        protected String error;

        public Builder () {
            theBuilder = getThis();
        }

        protected abstract B getThis();

        public B status(Integer status) {
            this.status = status;
            return theBuilder;
        }

        public B error(String error) {
            this.error = error;
            return theBuilder;
        }

        public abstract R build();

    }


}
