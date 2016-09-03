package org.unimelb.itime.ui.presenter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.fragment.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;

import java.util.zip.Inflater;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeSlotPresenter extends MvpBasePresenter<EventCreateNewTimeSlotMvpView> {
    private final String TAG = "EventCreateTimeSlotPresenter";
    private Context context;
    private LayoutInflater inflater;
    public EventCreateTimeSlotPresenter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    public Context getContext() {
        return context;
    }


    public LayoutInflater getInflater() {
        return inflater;
    }
}
