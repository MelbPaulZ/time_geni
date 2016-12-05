package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulresponse.UserLoginRes;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class UserUtil {
    private static UserUtil instance;

    private static UserLoginRes userLoginRes;

    private UserUtil(){
    }

    public static UserUtil getInstance(){
        if(instance == null){
            instance = new UserUtil();
        }
        return instance;
    }


    /** save the user info into shared preference, which can be used to fast login
     * **/
    public static void login(Context ctx, User user){
        SharedPreferences sp = AppUtil.getSharedPreferences(ctx);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString(C.spkey.USER_SHARED_PREFERENCES, gson.toJson(user));
        editor.apply();
    }

    public static void logot(Context ctx){

    }

    public static String getUserIdFromPreference(Context context){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        String userStr = sp.getString(C.spkey.USER_SHARED_PREFERENCES,"");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        return user.getUserUid();
    }


    public static String getUserUid(){
        return UserUtil.getInstance().getUser().getUserUid();
    }

    public static UserLoginRes getUserLoginRes() {
        return userLoginRes;
    }

    /**
     * todo: save it to db
     * @param userLoginRes
     */
    public void setUserLoginRes(Context context, UserLoginRes userLoginRes) {
        login(context, userLoginRes.getUser());
        this.userLoginRes = userLoginRes;
    }


    public User getUser(){
        return this.userLoginRes.getUser();
    }
}
