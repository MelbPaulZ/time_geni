package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
    public static void login(Context ctx, UserLoginRes userLoginRes){
        SharedPreferences sp = AppUtil.getSharedPreferences(ctx);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString(C.spkey.USER_SHARED_PREFERENCES, gson.toJson(userLoginRes));
        editor.apply();
    }

    public static void logot(Context ctx){

    }

    /** read user info from preference and also set info into userUtil for later usage
     * */
    public static String getUserIdFromPreference(Context context){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        String userStr = sp.getString(C.spkey.USER_SHARED_PREFERENCES,"");
        Gson gson = new Gson();
        UserLoginRes loginRes = gson.fromJson(userStr, UserLoginRes.class);
        userLoginRes = loginRes;
        if (loginRes.getUser()==null){
            Log.i("UserUtil", "getUserIdFromPreference: " + "is null");
        }else{
            Log.i("UserUtil", "getUserIdFromPreference: " + loginRes.getUser().getUserUid());
        }
        return loginRes.getUser().getUserUid();
    }


    public static String getUserUid(){
            return UserUtil.getInstance().getUser().getUserUid();
    }

    public static UserLoginRes getUserLoginRes() {
        return userLoginRes;
    }

    /**
     * todo: save it to db
     * @param loginRes
     */
    public static void setUserLoginRes(Context context, UserLoginRes loginRes) {
        login(context, loginRes);
        userLoginRes = loginRes;
    }


    public User getUser(){
        return this.userLoginRes.getUser();
    }
}
