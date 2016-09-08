package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.EventDetailForInviteeMvpView;

/**
 * Created by Paul on 29/08/2016.
 */
public class EventDetailForInviteePresenter extends MvpBasePresenter<EventDetailForInviteeMvpView>{

    private Context context;
    private LayoutInflater inflater;

    public EventDetailForInviteePresenter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    public void gotoWeekViewCalendar(){
        EventDetailForInviteeMvpView view = getView();
        if (view!=null){
            view.gotoWeekViewCalendar();
        }
    }

    public void confirmAndGotoWeekViewCalendar(Event event,boolean[]suggestTimeslotConfirmArray){
        EventDetailForInviteeMvpView view = getView();
        if (view!=null){
            view.confirmAndGotoWeekViewCalendar(event,suggestTimeslotConfirmArray);
        }
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}
