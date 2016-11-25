package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class MainCalendarPresenter extends MvpBasePresenter<MainCalendarMvpView>{
    private static final String TAG = "LoginPresenter";
    private Context context;

    public MainCalendarPresenter(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
