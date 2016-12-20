package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Timeslot;

/**
 * Created by Paul on 4/09/2016.
 */
public interface EventDetailGroupMvpView extends EventCommonMvpView{
    void toCalendar();
    void toEditEvent();
    void viewInCalendar();
    void viewInviteeResponse(Timeslot timeSlot);
}
