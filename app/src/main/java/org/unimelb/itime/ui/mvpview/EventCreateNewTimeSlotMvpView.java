package org.unimelb.itime.ui.mvpview;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 27/08/2016.
 */
public interface EventCreateNewTimeSlotMvpView extends MvpView {
    void onClickDone();
    void onClickBack();
    void initTimeSlots(Event event);

}
