package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

import java.util.List;

/**
 * Created by yuhaoliu on 9/12/2016.
 */

public interface EventCommonMvpView extends MvpView {

    void onTaskStart();
    void onTaskError(Throwable e);
    void onTaskComplete(List<Event> dataList);
    void onTaskComplete(Event data);
}
