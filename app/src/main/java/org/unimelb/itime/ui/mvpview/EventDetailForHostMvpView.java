package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 4/09/2016.
 */
public interface EventDetailForHostMvpView extends MvpView {
    void toWeekView();
    void toAttendeeView(long time);
    void toEditEvent(Event event);
    void viewInCalendar(String tag);
}
