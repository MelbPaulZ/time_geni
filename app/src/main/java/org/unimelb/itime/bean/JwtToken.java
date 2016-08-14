package org.unimelb.itime.bean;

/**
 * Created by yinchuandong on 12/08/2016.
 */
public class JwtToken {
    private String token;
    private String error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
