package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class LocationPresenter<V extends TaskBasedMvpView<AutocompletePrediction>> extends MvpBasePresenter<V> {

    private Context context;

    public LocationPresenter(Context context){
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }

}
