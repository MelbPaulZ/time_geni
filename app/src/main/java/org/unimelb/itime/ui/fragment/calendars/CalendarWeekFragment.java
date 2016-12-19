package org.unimelb.itime.ui.fragment.calendars;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.CalendarManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.eventview.DayDraggableEventView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarWeekFragment extends BaseUiFragment<EventCommonMvpView, EventCommonPresenter<EventCommonMvpView>> implements EventCommonMvpView {

    private View root;
    private WeekView weekView;
    private EventCommonPresenter presenter;
    private String TAG = "CalendarWeekFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_week_view, container, false);
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weekView = (WeekView) root.findViewById(R.id.week_view);
        weekView.setDayEventMap(EventManager.getInstance().getEventsPackage());
        weekView.setEventClassName(Event.class);
        weekView.setOnHeaderListener(new WeekView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                CalendarManager.getInstance().setCurrentShowCalendar(myCalendar.getCalendar());
                EventManager.getInstance().refreshRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
        weekView.setOnBodyOuterListener(new FlexibleLenViewBody.OnBodyListener() {
            @Override
            public boolean isDraggable(DayDraggableEventView dayDraggableEventView) {
                Event event = (Event) dayDraggableEventView.getEvent();
                if (event.getEventType().equals("solo")){
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void onEventCreate(DayDraggableEventView dayDraggableEventView) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dayDraggableEventView.getStartTimeM());
                ((MainActivity)getActivity()).startEventCreateActivity(calendar);
            }

            @Override
            public void onEventClick(DayDraggableEventView dayDraggableEventView) {
                EventUtil.startEditEventActivity(getContext(), getActivity(), dayDraggableEventView.getEvent());
            }

            @Override
            public void onEventDragStart(DayDraggableEventView dayDraggableEventView) {

            }

            @Override
            public void onEventDragging(DayDraggableEventView dayDraggableEventView, int i, int i1) {

            }

            @Override
            public void onEventDragDrop(final DayDraggableEventView dayDraggableEventView) {
                final Event orgEvent = (Event) dayDraggableEventView.getEvent();
                final Event event = EventManager.getInstance().copyCurrentEvent(orgEvent); // need to copy this event so wont change origin data
                if (orgEvent.getRecurrence().length>0){
                    // this is repeat event
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("this is a repeat event")
                            .setItems(EventUtil.getRepeatEventChangeOptions(getContext()), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0: {
                                            // update only this event
                                            // next find original event(the first event of repeat event)
                                            Toast.makeText(getContext(), "change only this event", Toast.LENGTH_SHORT).show();
                                            Calendar d = Calendar.getInstance();
                                            d.setTimeInMillis(orgEvent.getStartTime());
                                            Log.i(TAG, "orgEvent startTime: " + d.getTime());

                                            // here change the event as a new event
                                            Event newEvent = EventManager.getInstance().copyCurrentEvent(orgEvent);
                                            EventUtil.regenerateRelatedUid(newEvent);
                                            newEvent.setStartTime(dayDraggableEventView.getStartTimeM());
                                            newEvent.setEndTime(dayDraggableEventView.getEndTimeM());
                                            newEvent.setRecurringEventUid(orgEvent.getEventUid());
                                            newEvent.setRecurringEventId(orgEvent.getEventId());
                                            newEvent.setRule(new RuleModel(newEvent));
                                            newEvent.setRecurrence(newEvent.getRule().getRecurrence());

                                            // find the first event of repeat events, and update it to server
                                            Event firstOrg = null;
                                            for (Event event : EventManager.getInstance().getOrgRepeatedEventList()){
                                                if (event.getEventUid().equals(orgEvent.getEventUid())){
                                                    firstOrg = event;
                                                }
                                            }
                                            firstOrg.getRule().addEXDate(new Date(orgEvent.getStartTime()));
                                            firstOrg.setRecurrence(firstOrg.getRule().getRecurrence());
                                            EventManager.getInstance().getWaitingEditEventList().add(firstOrg);

                                            presenter.updateAndInsertEvent(firstOrg, newEvent);
                                            break;
                                        }
                                        case 1:{
                                            // update all future events
                                            Toast.makeText(getContext(), "change for all future events", Toast.LENGTH_SHORT).show();
                                            // need to consider move this event, or edit this event
                                            // to change all event, need to add until day to origin event, and create a new repeat event
                                            // first find the origin event
                                            Event orgEvent = EventManager.getInstance().findOrgByUUID(event.getEventUid());
                                            // then copy the origin event rule model to a new rule model
                                            Event cpyOrgEvent = EventManager.getInstance().copyCurrentEvent(orgEvent);
                                            // then add until day to the orgEvent
                                            if (EventUtil.isSameDay(event.getStartTime(), orgEvent.getStartTime())){
                                                // the moving day is the first day of this repeat event
                                                // amend the old event
                                                // first get transfer exDates to the new event, calculate as
                                                ArrayList<Date> exDates = orgEvent.getRule().getEXDates();
                                                Date orgStartDate = new Date(orgEvent.getStartTime());
                                                ArrayList<Integer> gapDates = new ArrayList<>();
                                                for (Date exDate: exDates){
                                                    int gap = EventUtil.getDayDifferent(orgStartDate.getTime(), exDate.getTime());
                                                    gapDates.add(gap);
                                                }
                                                // use the old gap dates to calculate new exDates
                                                Date newStartDate = new Date(dayDraggableEventView.getStartTimeM());
                                                ArrayList<Date> newExDates = new ArrayList<>();
                                                long oneDay = 24 * 60 * 60 * 1000;
                                                for (Integer gap : gapDates){
                                                    newExDates.add(new Date(newStartDate.getTime() + gap * oneDay));
                                                }

                                                // also need to calculate the old until, and if there is an old until, set to event
                                                Date oldUntil = cpyOrgEvent.getRule().getUntil();
                                                Date newUntil = null;
                                                if (oldUntil!=null) {
                                                    int gapUntil = EventUtil.getDayDifferent(orgStartDate.getTime(), oldUntil.getTime());
                                                    newUntil = new Date(dayDraggableEventView.getStartTimeM() + gapUntil * oneDay);
                                                    event.getRule().setUntil(newUntil);
                                                }
                                                // set untilDate and exDate for new event then generate its recurrence
                                                event.setRule(cpyOrgEvent.getRule());
                                                if (newUntil!=null){
                                                    event.getRule().setUntil(newUntil);
                                                }
                                                event.getRule().setEXDates(newExDates);
                                                event.setRecurrence(event.getRule().getRecurrence());
                                                // last set the starttime and endtime
                                                long duration = event.getEndTime() - event.getStartTime();
                                                event.setStartTime(dayDraggableEventView.getStartTimeM());
                                                event.setEndTime(event.getStartTime() + duration);
                                                presenter.updateEventToServer(event);
                                            }else{
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
                                                Date newUntil= null;
                                                if (oldUntil!=null) {
                                                    // for until, need current change time to calculate gay
                                                    int gapUntil = EventUtil.getDayDifferent(event.getStartTime(), oldUntil.getTime());
                                                    newUntil = new Date(dayDraggableEventView.getStartTimeM() + gapUntil * oneDay);
                                                }
                                                // set untilDate and exDate for new event then generate its recurrence
                                                event.setRule(cpyOrgEvent.getRule());
                                                if (newUntil!=null){
                                                    event.getRule().setUntil(newUntil);
                                                }
                                                event.getRule().setEXDates(newExDates);
                                                event.setRecurrence(event.getRule().getRecurrence());
                                                // regeneate Uids so it will be a new event
                                                EventUtil.regenerateRelatedUid(event);
                                                // last set the starttime and endtime
                                                long duration = event.getEndTime() - event.getStartTime();
                                                event.setStartTime(dayDraggableEventView.getStartTimeM());
                                                event.setEndTime(event.getStartTime() + duration);
                                                presenter.updateAndInsertEvent(orgEvent, event);
                                            }

                                        }
                                        case 2:{
                                            // on click cancel
                                            break;
                                        }

                                    }
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    // this is not repeat event
                    EventManager.getInstance().getWaitingEditEventList().add((Event) dayDraggableEventView.getEvent());
                    Event copyEvent = EventManager.getInstance().copyCurrentEvent(orgEvent);
                    copyEvent.setStartTime(dayDraggableEventView.getStartTimeM());
                    copyEvent.setEndTime(dayDraggableEventView.getEndTimeM());
                    presenter.updateEventToServer(copyEvent);
                }




//                Event newEvent = (Event) dayDraggableEventView.getEvent();
//                EventManager.getInstance().getWaitingEditEventList().add((Event) dayDraggableEventView.getEvent());
//                Event copyEvent = EventManager.getInstance().copyCurrentEvent(newEvent);
//                copyEvent.setStartTime(dayDraggableEventView.getStartTimeM());
//                copyEvent.setEndTime(dayDraggableEventView.getEndTimeM());
//                presenter.updateEventToServer(copyEvent);
            }
        });
    }

    public void backToday(){
        weekView.backToToday();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadData(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_EVENT) {
            weekView.setDayEventMap(EventManager.getInstance().getEventsPackage());
            weekView.reloadEvents();
        }
    }

    public void scrollTo(Calendar calendar){
        weekView.scrollTo(calendar);
    }


    @Override
    public EventCommonPresenter createPresenter() {
        presenter = new EventCommonPresenter(getActivity());
        return presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void calendarNotifyDataSetChanged(){
        if (weekView!=null) {
            weekView.reloadEvents();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollTo(CalendarManager.getInstance().getCurrentShowCalendar());

    }

    public void scrollToWithOffset(long time){
        weekView.scrollToWithOffset(time);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        CalendarManager.getInstance().setCurrentShowCalendar(c);
    }


    @Override
    public void onTaskStart() {
        AppUtil.showProgressBar(getActivity(),"Updating","Please wait...");
    }

    @Override
    public void onTaskError(Throwable e) {

    }

    @Override
    public void onTaskComplete(List<Event> dataList) {
        AppUtil.hideProgressBar();
    }

    @Override
    public void onTaskComplete(Event data) {

    }
}
