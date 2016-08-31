package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingPresenter extends MvpBasePresenter<EventCreateDetailBeforeSendingMvpView> {
    private Context context;
    public EventCreateDetailBeforeSendingPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
