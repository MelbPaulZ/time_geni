package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.User;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface CalendarPrefMvpView extends TaskBasedMvpView<User>, ItimeCommonMvpView{

    void toCalendarPage();
    void toAlertTimePage();
    void toImportPage();
}
