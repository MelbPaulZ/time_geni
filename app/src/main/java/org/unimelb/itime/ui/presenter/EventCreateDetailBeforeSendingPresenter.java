package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
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

    public void sendEvent(Event event){
        EventCreateDetailBeforeSendingMvpView view = getView();
        if (view!=null){
            view.sendEvent(event);
        }
    }

    public void backToTimeSlotView(){
        EventCreateDetailBeforeSendingMvpView view = getView();
        if (view!=null){
            view.backToTimeSlotView();
        }
    }

    public void changeLocation(String tag){
        EventCreateDetailBeforeSendingMvpView view = getView();
        if (view!=null){
            view.changeLocation(tag);
        }
    }

    public void pickEndRepeatDate(String tag){
        EventCreateDetailBeforeSendingMvpView view = getView();
        if (view!=null){
            view.changeEndRepeatDate(tag);
        }
    }
}
