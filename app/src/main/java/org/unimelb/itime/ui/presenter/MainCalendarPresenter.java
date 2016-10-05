package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class MainCalendarPresenter extends MvpBasePresenter<MainCalendarMvpView>{
    private static final String TAG = "LoginPresenter";
    private Context context;

    public MainCalendarPresenter(Context context){
        this.context = context;
    }

    public void testHttp(){

    }


    public void gotoEditEventActivity(ITimeEventInterface iTimeEventInterface){
        MainCalendarMvpView mvpView = getView();
        if (mvpView!=null){
            mvpView.startEditEventActivity(iTimeEventInterface);
        }
    }

    public void gotoCreateEventActivity(){
//        EventBus.getDefault().post(new MessageEvent("createNewActivity"));
        MainCalendarMvpView view = getView();
        if (view!=null){
            view.startCreateEventActivity();
        }
    }


    public Context getContext() {
        return context;
    }
}
