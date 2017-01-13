package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;

import java.util.List;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public interface MainCalendarMvpView extends TaskBasedMvpView<List<Event>>{
    void toEventCreatePage(long startTime);

}
