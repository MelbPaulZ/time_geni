package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

/**
 * Created by Paul on 25/08/2016.
 */
public interface EventCreateNewMvpView extends MvpView {
    void pickDate(EventCreateNewVIewModel.PickDateFromType pickDateFromType);
    void gotoWeekViewCalendar();
    void pickLocatioin();
    void pickAttendee();
}
