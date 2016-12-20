package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewPresenter extends EventCommonPresenter<EventCreateNewMvpView>{

    private String TAG = "EventCreateNewPresenter";
    public EventCreateNewPresenter(Context context){
        super(context);
    }
}
