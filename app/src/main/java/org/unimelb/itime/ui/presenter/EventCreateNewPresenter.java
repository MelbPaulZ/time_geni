package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewPresenter extends MvpBasePresenter<EventCreateNewMvpView>{

    private Context context;
    public EventCreateNewPresenter(Context context){
        this.context = context;
    }

    public void submit(Event event){


    }


    public void pickDate(String tag){
        EventCreateNewMvpView view = getView();
        if (view!=null)
            view.pickDate(tag);
    }


    public Context getContext() {
        return context;
    }

    public void gotoWeekViewCalendar(){
        EventCreateNewMvpView view = getView();
        if (view!=null)
            view.gotoWeekViewCalendar();
    }

    public void pickLocation(String tag){
        EventCreateNewMvpView view = getView();
        if (view!=null){
            view.pickLocatioin(tag);
        }
    }

    public void pickAttendee(){
        EventCreateNewMvpView view = getView();
        if (view!=null){
            view.pickAttendee();
        }
    }

    public void toCreateSoloEvent(Event event){
        EventCreateNewMvpView view = getView();
        if (view!=null){
            // here to do http request to create solo event





            view.toCreateSoloEvent(event);
        }
    }

    public void pickPhoto(String tag){
        EventCreateNewMvpView view = getView();
        if (view!=null){
            view.pickPhoto(tag);
        }
    }


}
