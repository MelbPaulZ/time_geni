package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.vendor.unitviews.DraggableTimeSlotView;

/**
 * Created by Paul on 10/09/2016.
 */
public interface EventDetailTimeSlotMvpVIew extends MvpView{
    void onClickBack();
    void onClickDone(Event event);
    void reloadTimeslot();
    void addTimeslot(Timeslot timeslot);
    void popupTimeSlotWindow(DraggableTimeSlotView timeSlotView);
    void onClickTimeSlotView(DraggableTimeSlotView timeSlotView);
    boolean isClickTSConfirm();
}
