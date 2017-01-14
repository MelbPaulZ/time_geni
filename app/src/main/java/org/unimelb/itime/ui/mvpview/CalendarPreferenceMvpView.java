package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Calendar;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface CalendarPreferenceMvpView extends TaskBasedMvpView<Calendar>, ItimeCommonMvpView{
    void toCalendarPage();
    void toAlertTimePage();
    void toImportPage();
}
