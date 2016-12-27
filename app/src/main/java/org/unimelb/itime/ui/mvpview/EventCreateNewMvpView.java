package org.unimelb.itime.ui.mvpview;

/**
 * Created by Paul on 25/08/2016.
 */
public interface EventCreateNewMvpView extends EventCommonMvpView{
    void gotoWeekViewCalendar();
    void pickLocation();
    void pickInvitee();
    void toCreateSoloEvent();
    void pickPhoto(String tag);
}
