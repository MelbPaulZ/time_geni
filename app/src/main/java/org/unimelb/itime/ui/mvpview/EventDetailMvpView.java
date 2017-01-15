package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

/**
 * Created by Paul on 4/09/2016.
 */
public interface EventDetailMvpView extends EventCommonMvpView{
    void viewInCalendar();
    void viewInviteeResponse(Timeslot timeSlot);
    void gotoGridView();
    void reloadPage();
    void toResponse();
}
