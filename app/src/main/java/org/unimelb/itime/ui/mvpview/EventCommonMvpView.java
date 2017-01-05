package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

import java.util.List;

/**
 * Created by yuhaoliu on 9/12/2016.
 */

public interface EventCommonMvpView extends ItimeCommonMvpView {

    void onTaskStart(int task);
    void onTaskError(int task, String errorMsg, int code);
    void onTaskComplete(int task, List<Event> dataList);
}
