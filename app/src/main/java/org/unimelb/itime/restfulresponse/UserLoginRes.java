package org.unimelb.itime.restfulresponse;

import org.unimelb.itime.bean.User;

/**
 * Created by Paul on 23/09/2016.
 */
public class UserLoginRes {

    User user;
    String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
