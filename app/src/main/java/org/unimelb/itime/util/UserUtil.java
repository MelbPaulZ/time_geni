package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.managers.SettingManager;
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


    /** save the user info into shared preference, which can be used to fast signIn
     * **/
    public void login(UserLoginRes userLoginRes){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString(C.spkey.USER_SHARED_PREFERENCES, gson.toJson(userLoginRes));
        editor.apply();
        this.userLoginRes = userLoginRes;
    }

    /**
     *
     */
    public void logout(){
        instance = null;
    }

    public String getUserUid(){
        return instance.getUser().getUserUid();
    }

    public Setting getSetting(){
        return userLoginRes.getSetting();
    }

    public void setSetting(Setting setting){
        this.userLoginRes.setSetting(setting);
    }

    public UserLoginRes getUserLoginRes() {
        return userLoginRes;
    }


    public User getUser(){
        return userLoginRes.getUser();
    }

    /**
     * not delete DB data
     */
    public void clearAccount(){
        UserUtil user = UserUtil.getInstance(context);
        user.logout();

        SettingManager stManager = SettingManager.getInstance(context);
        stManager.clear();

        CalendarUtil.getInstance(context).clear();
        EventManager.getInstance(context).clear();

        AuthUtil.clearJwtToken(context);
        SharedPreferences sp = AppUtil.getTokenSaver(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

    /**
     * delete everything
     */
    public void clearAccountWithDB(){
        UserUtil user = UserUtil.getInstance(context);
        user.logout();

        SettingManager stManager = SettingManager.getInstance(context);
        stManager.clear();

        AuthUtil.clearJwtToken(context);
        SharedPreferences sp = AppUtil.getTokenSaver(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, "");
        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, "");
        editor.apply();

        DBManager.getInstance(context).deleteAllMessages();
        DBManager.getInstance(context).clearDB();
        EventManager.getInstance(context).clear();
    }
}
