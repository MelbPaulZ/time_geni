package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.EventDetailForSoloMvpView;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventDetailForSoloPresenter extends MvpBasePresenter<EventDetailForSoloMvpView> {
    private Context context;

    public EventDetailForSoloPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
