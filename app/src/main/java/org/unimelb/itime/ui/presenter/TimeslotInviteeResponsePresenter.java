package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by yuhaoliu on 3/12/2016.
 */

public class TimeslotInviteeResponsePresenter extends MvpBasePresenter {
    private Context context;

    public TimeslotInviteeResponsePresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
