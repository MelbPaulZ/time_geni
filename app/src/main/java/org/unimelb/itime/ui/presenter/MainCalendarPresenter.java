package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.List;

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
