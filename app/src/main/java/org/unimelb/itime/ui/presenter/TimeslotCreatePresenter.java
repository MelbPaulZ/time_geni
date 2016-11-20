package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.ui.mvpview.TimeslotCreateMvpView;

/**
 * Created by Paul on 20/11/16.
 */
public class TimeslotCreatePresenter extends MvpBasePresenter<TimeslotCreateMvpView> {
    private Context context;

    public TimeslotCreatePresenter(Context context) {
        this.context = context;
    }

    public Context getContext(){
        return context;
    }
}
