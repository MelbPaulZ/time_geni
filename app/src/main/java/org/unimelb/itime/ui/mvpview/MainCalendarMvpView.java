package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.vendor.listener.ITimeEventInterface;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public interface MainCalendarMvpView extends EventCommonMvpView{
    void startCreateEventActivity();
    void backToGroupEvent();
    void startEditEventActivity(ITimeEventInterface iTimeEventInterface);
}
