package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Calendar;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface SettingCalendarMvpView extends TaskBasedMvpView<Calendar>, ItimeCommonMvpView{
    void toAddCalendar();
    void toEditCalendar(Calendar calendar);
    void toDeleteCalendar(Calendar calendar);
}
