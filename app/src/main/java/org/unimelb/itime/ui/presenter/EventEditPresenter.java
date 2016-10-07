package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
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

    public void changeLocation(){
        EventEditMvpView view = getView();
        if (view != null){
            view.changeLocation();
        }
    }

    public void updateEvent(Event newEvent){
        Event oldEvent = EventManager.getInstance().getCurrentEvent();
        // here update EventManager
        EventManager.getInstance().updateEvent(oldEvent, newEvent);
        // update db or eventmanager?
        // here update DB
        oldEvent.delete();
        DBManager.getInstance(context).insertEvent(newEvent);
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
