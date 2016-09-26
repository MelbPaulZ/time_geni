package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeSlotPresenter extends MvpBasePresenter<EventCreateNewTimeSlotMvpView> {
    private final String TAG = "EventCreateTimeSlotPresenter";
    private Context context;
    private LayoutInflater inflater;
    public EventCreateTimeSlotPresenter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }


    public LayoutInflater getInflater() {
        return inflater;
    }

    public void toInviteePicker(String tag){
        EventCreateNewTimeSlotMvpView view = getView();
        if (view!=null){
            view.toInviteePicker(tag);
        }
    }
}
