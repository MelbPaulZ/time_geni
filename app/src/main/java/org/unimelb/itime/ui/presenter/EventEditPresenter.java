package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditPresenter extends MvpBasePresenter<EventEditMvpView> {
    private Context context;

    public EventEditPresenter(Context context) {
        this.context = context;
    }

    public void toHostEventDetail(Event event){
        EventEditMvpView view = getView();
        if (view != null){
            view.toHostEventDetail(event);
        }
    }

    public void changeLocation(){
        EventEditMvpView view = getView();
        if (view != null){
            view.changeLocation();
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
