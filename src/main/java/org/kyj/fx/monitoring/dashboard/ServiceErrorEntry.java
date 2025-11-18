package org.kyj.fx.monitoring.dashboard;

public class ServiceErrorEntry {
    private String errorCode;
    private String errorMsg;
    private String errorDesc;
    private int count;
    private String errorLog;

    public ServiceErrorEntry(String errorCode, String errorMsg, String errorDesc, int count, String errorLog) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorDesc = errorDesc;
        this.count = count;
        this.errorLog = errorLog;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }
}
