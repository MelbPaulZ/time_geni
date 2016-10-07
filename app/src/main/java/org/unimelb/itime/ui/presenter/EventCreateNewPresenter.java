package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.Calendar;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewPresenter extends MvpBasePresenter<EventCreateNewMvpView>{

    private Context context;
    public EventCreateNewPresenter(Context context){
        this.context = context;
    }
    public Context getContext() {
        return context;
    }

    public void pickPhoto(String tag){
        EventCreateNewMvpView view = getView();
        if (view!=null){
            view.pickPhoto(tag);
        }
    }

    public void initNewEvent(){
        // initial default values for new event
        Event event = new Event();
        event.setEventUid(EventUtil.generateUid());
        event.setHostUserUid(UserUtil.getUserUid());
        long startTime = CalendarUtil.getInstance().getNowCalendar().getTimeInMillis();
        long endTime = CalendarUtil.getInstance().getNowCalendar().getTimeInMillis() + 3600 * 1000;
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        EventManager.getInstance().setCurrentEvent(event);
    }

    public void addSoloEvent(){
        Event event = EventManager.getInstance().getCurrentEvent();
        EventManager.getInstance().addEvent(event);
        DBManager.getInstance(getContext()).insertEvent(event);
        event.update();
    }


}
