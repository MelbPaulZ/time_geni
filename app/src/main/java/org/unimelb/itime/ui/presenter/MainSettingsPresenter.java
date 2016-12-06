package org.unimelb.itime.ui.presenter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.C;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.mvpview.MainSettingsMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.AuthUtil;

import static android.R.attr.delay;

/**
 * Created by Paul on 3/10/16.
 */
public class MainSettingsPresenter extends MvpBasePresenter<MainSettingsMvpView> {
    private String TAG = "MainSettingPresenter";
    Context context;

    public MainSettingsPresenter(Context context) {
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    public void logOut(){
        if(getView() != null){
            getView().logOut();
        }
    }

}
