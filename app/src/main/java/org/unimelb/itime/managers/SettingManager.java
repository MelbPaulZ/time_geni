package org.unimelb.itime.managers;

import android.content.Context;

import com.google.gson.Gson;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 28/12/2016.
 */

public class SettingManager {
    private static SettingManager instance;
    private Context context;
    private Setting setting;

    private SettingManager(Context context){
        this.context = context;
    }

    public static SettingManager getInstance(Context context){
        if (instance==null){
            instance = new SettingManager(context);
        }

        instance.init();
        return instance;
    }

    private void init(){
        if (instance.setting==null){
            instance.setting = new Setting();
        }
        if (!instance.setting.hasUser()){
            instance.setting.setUser(UserUtil.getInstance(context).getUser());
        }
        if (!instance.setting.hasCalendar()){
            instance.setting.setCalendars(CalendarUtil.getInstance(context).getCalendar());
        }
    }

    public void saveSettings(){
        // TODO: 28/12/2016 inset setting to db 
    }

    public Context getContext() {
        return instance.context;
    }

    public Setting getSetting() {
        return instance.setting;
    }

    public void setSetting(Setting setting) {
        instance.setting = setting;
    }

    /**
     *
     * @param setting input setting and copy another one
     * @return
     */
    public Setting copySetting(Setting setting){
        Gson gson = new Gson();
        String str = gson.toJson(setting);
        return gson.fromJson(str, Setting.class);
    }

    /**
     *  this method is called for getting a copy of setting
     * @return
     */
    public Setting copySetting(){
        return copySetting(this.setting);
    }

    public void clear(){
        instance = null;
    }
}