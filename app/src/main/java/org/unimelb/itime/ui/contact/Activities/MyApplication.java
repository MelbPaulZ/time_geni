package org.unimelb.itime.ui.contact.Activities;


import android.app.Application;

/**
 * Created by 37925 on 2016/12/4.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance(){
        return instance;
    }
}
