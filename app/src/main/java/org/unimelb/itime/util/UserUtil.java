package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.restfulresponse.UserLoginRes;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class UserUtil {
    private static UserUtil instance;

    private UserLoginRes userLoginRes;
    private Context context;

    private UserUtil(Context context){
        this.context = context;
    }

    public static UserUtil getInstance(Context context){
        if(instance == null){
            instance = new UserUtil(context);
            instance.init();
        }
        return instance;
    }

    /**
     * restore all persistent information to instance
     */
    private void init(){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        String userJson = sp.getString(C.spkey.USER_SHARED_PREFERENCES, "{}");
        Gson gson = new Gson();
        this.userLoginRes = gson.fromJson(userJson, UserLoginRes.class);
    }


    /** save the user info into shared preference, which can be used to fast login
     * **/
    public void login(UserLoginRes userLoginRes){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString(C.spkey.USER_SHARED_PREFERENCES, gson.toJson(userLoginRes));
        editor.apply();
    }

    /**
     *
     */
    public void logout(){

    }

    public String getUserUid(){
        return getUser().getUserUid();
    }

    public UserLoginRes getUserLoginRes() {
        return userLoginRes;
    }


    public User getUser(){
        return userLoginRes.getUser();
    }
}
