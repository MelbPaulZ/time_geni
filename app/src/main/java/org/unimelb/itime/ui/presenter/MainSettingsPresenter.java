package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.ui.mvpview.SettingCommonMvpView_delete;

/**
 * Created by Paul on 3/10/16.
 */
public class MainSettingsPresenter<T extends SettingCommonMvpView_delete> extends CommonPresenter<T> {
    private String TAG = "MainSettingPresenter";
    private Context context;

    public MainSettingsPresenter(Context context) {
        super(context);
    }


    public Context getContext(){
        return context;
    }

}
