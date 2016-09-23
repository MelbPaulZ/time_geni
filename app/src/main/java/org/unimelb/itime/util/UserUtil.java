package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.restfulresponse.UserLoginRes;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class UserUtil {
    private static UserUtil instance;

    private UserLoginRes userLoginRes;

    private UserUtil(){
    }

    public static UserUtil getInstance(){
        if(instance == null){
            instance = new UserUtil();
        }
        return instance;
    }

    public static void login(Context ctx){

    }

    public static void logot(Context ctx){

    }


    public static String getUserUid(){
        return "1";
    }

    public UserLoginRes getUserLoginRes() {
        return userLoginRes;
    }

    public void setUserLoginRes(UserLoginRes userLoginRes) {
        this.userLoginRes = userLoginRes;
    }
}
