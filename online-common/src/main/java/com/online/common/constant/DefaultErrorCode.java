package com.online.common.constant;

import lombok.ToString;


@ToString
public class DefaultErrorCode extends BaseErrorCode {

    public static final BaseErrorCode SYSTEM_ERROR = new DefaultErrorCode(-1,"系统错误");

    public static final BaseErrorCode DB_ERROR = new DefaultErrorCode(1001,"数据库错误");
    public static final BaseErrorCode SYS_ERROR = new DefaultErrorCode(1002,"系统错误");
    public static final BaseErrorCode PARAM_ERROR =new DefaultErrorCode(1003,"参数异常");
    public static final BaseErrorCode DATA_NOT_FOUND = new DefaultErrorCode(1004,"数据未找到");

    public DefaultErrorCode(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
