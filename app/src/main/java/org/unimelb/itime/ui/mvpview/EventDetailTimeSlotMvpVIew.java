package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.List;

/**
 * Created by Paul on 10/09/2016.
 */
public interface EventDetailTimeSlotMvpVIew extends EventCommonMvpView {
    void onClickBack();
    void onClickDone(Event event);
    void reloadTimeslot();
    void addTimeslot(WeekView.TimeSlotStruct timeSlotStruct);
    void popupTimeSlotWindow(TimeSlotView timeSlotView);
    void onClickTimeSlotView(TimeSlotView timeSlotView);
    void onRecommend(List<Timeslot> timeslotList);
    boolean isClickTSConfirm();
    boolean isFromEditFragment();
    boolean isFromInviteeFragment();
}
