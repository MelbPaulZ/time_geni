package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.fragment.EventTimeSlotViewFragment;

/**
 * Created by Paul on 27/08/2016.
 */
public interface EventCreateNewTimeSlotMvpView extends MvpView {
    void toNewEventDetailBeforeSending();

}
