package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.User;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface CalendarImportMvpView extends TaskBasedMvpView<Calendar>, ItimeCommonMvpView{
    void toGoogleCal();
    void toUnimebCal();
}
