package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 29/08/2016.
 */
public interface EventDetailForInviteeMvpView extends MvpView {
    void gotoWeekViewCalendar();
    void confirmAndGotoWeekViewCalendar(Event event,boolean[]suggestTimeSlotConfirmArray);

}
