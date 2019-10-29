package com.online.common.model.result;


import com.online.common.constant.BaseErrorCode;
import com.online.common.model.domain.BaseDO;

public class ResultSupport extends BaseDO {
    private boolean success = true;
    private int resultCode;
    private String resultMsg;
    
    public ResultSupport(){
    	
    }
    
    public ResultSupport(boolean success,int resultCode,String resultMsg){
    	this.success = success;
    	this.resultCode = resultCode;
    	this.resultMsg  = resultMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public void setErrorCode(BaseErrorCode errorCode) {
        this.resultCode = errorCode.getErrorCode();
        this.resultMsg = errorCode.getErrorMsg();
        this.success = false;
    }
}
