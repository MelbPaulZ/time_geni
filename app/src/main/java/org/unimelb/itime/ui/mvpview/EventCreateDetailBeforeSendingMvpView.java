package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 31/08/2016.
 */
public interface EventCreateDetailBeforeSendingMvpView extends MvpView {
    void sendEvent(Event event);
    void backToTimeSlotView();
    void changeLocation(String tag);
    void changeEndRepeatDate(String tag);
}
