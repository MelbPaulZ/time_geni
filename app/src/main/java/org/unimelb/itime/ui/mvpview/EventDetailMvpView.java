package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.util.List;

/**
 * Created by Paul on 4/09/2016.
 */
public interface EventDetailMvpView extends TaskBasedMvpView<List<Event>>, ItimeCommonMvpView{
    void viewInCalendar();
    void viewInviteeResponse(Timeslot timeSlot);
    void gotoGridView();
    void onTimeslotClick(WrapperTimeSlot wrapper);
    void toResponse();
    void createEventFromThisTemplate(Event event);
}
