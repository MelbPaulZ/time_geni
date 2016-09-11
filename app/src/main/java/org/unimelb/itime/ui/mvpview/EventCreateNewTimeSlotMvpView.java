package org.unimelb.itime.ui.mvpview;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 27/08/2016.
 */
public interface EventCreateNewTimeSlotMvpView extends MvpView {
    void toNewEventDetailBeforeSending(Bundle bundle);
    void toInviteePicker(String tag);

}
