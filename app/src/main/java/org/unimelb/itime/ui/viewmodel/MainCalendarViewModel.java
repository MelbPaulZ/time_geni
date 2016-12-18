package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends CommonViewModel{
    public final static String TAG = "MainCalendarViewModel";
    private MainCalendarPresenter presenter;

    private String toolbarTitle;
    private MainCalendarMvpView mvpView;


    public MainCalendarViewModel(MainCalendarPresenter presenter) {
        super(presenter);
        this.presenter = presenter;
        mvpView = (MainCalendarMvpView) presenter.getView();
        toolbarTitle = initToolBarTitle();
    }

    public String getToday(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH) + "";
    }

    public String initToolBarTitle(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int delta = -(calendar.get(Calendar.DAY_OF_WEEK)-1);
        calendar.add(Calendar.DATE,delta);

        return EventUtil.getMonth(getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
    }

    public Context getContext(){
        return presenter.getContext();
    }


    @Bindable
    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        notifyPropertyChanged(BR.toolbarTitle);
    }

    public View.OnClickListener gotoEventCreateActivity(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: " + System.currentTimeMillis());
                if (mvpView!=null){
                    mvpView.startCreateEventActivity();
                }
            }
        };
    }

    public View.OnClickListener onClickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mvpView!=null){
                    mvpView.backToGroupEvent();
                }
            }
        };
    }

    public void changeAllEvent(Event event){
        if (mvpView!=null){
            // need to consider move this event, or edit this event
            // to change all event, need to add until day to origin event, and create a new repeat event
            // first find the origin event
            Event orgEvent = EventManager.getInstance().findOrgByUUID(event.getEventUid());
            // then copy the origin event rule model to a new rule model
            Event cpyOrgEvent = EventManager.getInstance().copyCurrentEvent(orgEvent);
            // then add until day to the orgEvent
            orgEvent.getRule().setUntil(new Date(event.getStartTime()));
            orgEvent.setRecurrence(orgEvent.getRule().getRecurrence());
            // change origin event done

            // next create the new event
            // first get transfer exDates to the new event, calculate as
            ArrayList<Date> exDates = orgEvent.getRule().getEXDates();
            Date orgStartDate = new Date(orgEvent.getStartTime());
            ArrayList<Integer> gapDates = new ArrayList<>();
            for (Date exDate: exDates){
                int gap = EventUtil.getDayDifferent(orgStartDate.getTime(), exDate.getTime());
                gapDates.add(gap);
            }
            // use the old gap dates to calculate new exDates
            Date newStartDate = new Date(event.getStartTime());
            ArrayList<Date> newExDates = new ArrayList<>();
            long oneDay = 24 * 60 * 60 * 1000;
            for (Integer gap : gapDates){
                newExDates.add(new Date(newStartDate.getTime() + gap * oneDay));
            }

            // also need to calculate the old until, and if there is an old until, set to event
            Date oldUntil = cpyOrgEvent.getRule().getUntil();
            if (oldUntil!=null) {
                int gapUntil = EventUtil.getDayDifferent(orgStartDate.getTime(), oldUntil.getTime());
                Date newUntil = new Date(event.getStartTime() + gapUntil * oneDay);
                event.getRule().setUntil(newUntil);
            }
            // set untilDate and exDate for new event then generate its recurrence
            event.setRule(cpyOrgEvent.getRule());
            event.getRule().setEXDates(newExDates);
            event.setRecurrence(event.getRule().getRecurrence());
            // regeneate Uids so it will be a new event
            EventUtil.regenerateRelatedUid(event);
            presenter.updateAndInsertEvent(orgEvent, event);
        }
    }

}
