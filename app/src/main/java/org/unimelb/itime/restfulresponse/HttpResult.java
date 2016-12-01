package org.unimelb.itime.restfulresponse;

/**
 * Created by yinchuandong on 29/09/2016.
 */
public class HttpResult <T> {
    /**
     * T can be an object or a List<T>
     */
    private T data;
    /**
     * the detailed information of error or success
     */
    private String info;
    /**
     * success: 1
     * failed: <0, e.g., -1, -2, ..
     */
    private int status;

    private String syncToken;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }
}
