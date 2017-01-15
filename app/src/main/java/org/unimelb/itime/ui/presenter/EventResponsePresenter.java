package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.restfulapi.EventResponseApi;
import org.unimelb.itime.ui.mvpview.EventResponseMvpView;
import org.unimelb.itime.util.HttpUtil;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class EventResponsePresenter extends MvpBasePresenter<EventResponseMvpView> {

    private Context context;
    private final static String TAG = "EventResponsePresenter ";
    private EventResponseApi api;

    public EventResponsePresenter(Context context){
        this.context = context;
        this.api = HttpUtil.createService(context, EventResponseApi.class);
    }

    public Context getContext(){
        return this.context;
    }



}
