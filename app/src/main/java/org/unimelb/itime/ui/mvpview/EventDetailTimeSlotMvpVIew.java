package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 10/09/2016.
 */
public interface EventDetailTimeSlotMvpVIew extends MvpView {
    void onClickBack();
    void onClickDone();
    void reloadTimeslot();
}
