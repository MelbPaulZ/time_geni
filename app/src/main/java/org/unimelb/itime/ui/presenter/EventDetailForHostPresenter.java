package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.EventDetailForHostMvpView;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailForHostPresenter extends MvpBasePresenter<EventDetailForHostMvpView> {
    private Context context;
//    private LayoutInflater inflater;

    public EventDetailForHostPresenter(Context context) {
        this.context = context;
    }

    public void toWeekView(){
        EventDetailForHostMvpView view = getView();
        if (view!=null){
            view.toWeekView();
        }
    }

    public void toAttendeeView(Long time){
        EventDetailForHostMvpView view = getView();
        if (view!=null){
            view.toAttendeeView(time);
        }
    }

    public void toEditEvent(Event event){
        EventDetailForHostMvpView view = getView();
        if (view!=null){
            view.toEditEvent(event);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
