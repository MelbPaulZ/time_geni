package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.EventDetailSoloMvpView;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventDetailSoloPresenter extends MvpBasePresenter<EventDetailSoloMvpView> {
    private Context context;

    public EventDetailSoloPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
