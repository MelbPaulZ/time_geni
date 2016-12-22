package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.MainSettingsMvpView;

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
