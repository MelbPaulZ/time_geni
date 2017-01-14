package org.unimelb.itime.restfulresponse;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;

import java.io.Serializable;

/**
 * Created by Paul on 23/09/2016.
 */
public class UserLoginRes implements Serializable{

    User user;
    Setting setting;
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

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
