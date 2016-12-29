package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.List;

/**
 * Created by Paul on 10/09/2016.
 */
public interface EventDetailTimeSlotMvpVIew extends TimeslotCommonMvpView {
    void onClickBack();
    void onClickDone(Event event);
    void reloadTimeslot();
    void addTimeslot(Timeslot timeslot);
    void popupTimeSlotWindow(TimeSlotView timeSlotView);
    void onClickTimeSlotView(TimeSlotView timeSlotView);
    boolean isClickTSConfirm();
}
