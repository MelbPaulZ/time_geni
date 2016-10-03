package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventDetailForHostMvpView;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailForHostPresenter extends MvpBasePresenter<EventDetailForHostMvpView> {
    private Context context;
    private LayoutInflater inflater;

    public EventDetailForHostPresenter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    public void toWeekView(Event event){
        EventDetailForHostMvpView view = getView();
        if (view!=null){
            Event oldEvent = DBManager.getInstance(context).getEvent(event.getEventUid());
            // here update EventManager
            EventManager.getInstance().updateEvent(oldEvent, event.getStartTime(), event.getEndTime());
            // update db or eventmanager?
            // here update DB
            oldEvent.setStartTime(event.getStartTime());
            oldEvent.setEndTime(event.getEndTime());
            view.toWeekView();
        }
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

    public void viewInCalendar(String tag){
        EventDetailForHostMvpView view = getView();
        if (view!=null){
            view.viewInCalendar(tag);
        }
    }

    public void confirmAndGotoWeekViewCalendar(Event event){
        EventDetailForHostMvpView view = getView();
        if (view!=null){
            view.confirmAndGotoWeekViewCalendar(event);
        }
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

}
