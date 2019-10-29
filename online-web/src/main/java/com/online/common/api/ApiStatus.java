package com.online.common.api;

import com.online.common.api.expcetion.UnCheckedWebException;
import com.online.common.constant.BaseApiStatus;
import com.online.common.model.result.BaseResultSupport;

public class ApiStatus extends BaseApiStatus {


    /**
     * ACCOUNT
     */
    public static final int AC_ILLGUAL_ACCOUNT = 20001;


    /**
     * USER
     */
    public static final int US_ILLGUAL_ACCOUNT = 30001;


    public static UnCheckedWebException wrapperException(BaseResultSupport resultSupport) {
        int code = resultSupport.getCode();
        String msg = resultSupport.getMsg();
        return new UnCheckedWebException(code,msg);
    }


    public static UnCheckedWebException wrapperException(RuntimeException ex) {
        return new UnCheckedWebException(SYST_SYSTEM_ERROR,FAIL_MESSAGE,ex);
    }


}