package org.unimelb.itime.ui.mvpview;

import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.vendor.listener.ITimeEventInterface;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public interface MainCalendarMvpView extends MvpView{
    void startCreateEventActivity();
    void backToGroupEvent();
    void startEditEventActivity(ITimeEventInterface iTimeEventInterface);
}
