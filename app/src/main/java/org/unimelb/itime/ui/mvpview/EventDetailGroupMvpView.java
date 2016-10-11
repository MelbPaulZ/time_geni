package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Timeslot;

/**
 * Created by Paul on 4/09/2016.
 */
public interface EventDetailGroupMvpView extends MvpView {
    void toCalendar();
    void toEditEvent();
    void viewInCalendar();
    void viewInviteeResponse(Timeslot timeSlot);
}
