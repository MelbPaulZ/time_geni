package org.unimelb.itime.ui.fragment.calendars;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.CalendarManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;


/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarWeekFragment extends CalendarBaseViewFragment {

    private View root;
    private WeekView weekView;
    private String TAG = "CalendarWeekFragment";
    private EventManager eventManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_week_view, container, false);
        initViews();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadData(MessageEvent messageEvent) {
        if (messageEvent.task == MessageEvent.RELOAD_EVENT) {
            weekView.setDayEventMap(eventManager.getEventsPackage());
            weekView.reloadEvents();
        }
    }

    public void scrollTo(Calendar calendar) {
        weekView.scrollTo(calendar);
    }

    private void initViews() {
        eventManager = EventManager.getInstance(getContext());
        weekView = (WeekView) root.findViewById(R.id.week_view);
        weekView.setDayEventMap(eventManager.getEventsPackage());
        weekView.setEventClassName(Event.class);
        weekView.setOnHeaderListener(new WeekView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                CalendarManager.getInstance().setCurrentShowCalendar(myCalendar.getCalendar());
                eventManager.syncRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
        weekView.setOnBodyOuterListener(new EventItemListener());

    }




    @Override
    public void onStart() {
        super.onStart();
//        if (weekView != null){
//            weekView.setDayEventMap(eventManager.getEventsPackage());
//            weekView.reloadEvents();
//        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void calendarNotifyDataSetChanged() {
        if (weekView != null) {
            weekView.reloadEvents();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollTo(CalendarManager.getInstance().getCurrentShowCalendar());

    }

    public void scrollToWithOffset(long time) {
        weekView.scrollToWithOffset(time);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        CalendarManager.getInstance().setCurrentShowCalendar(c);
    }

    @Override
    public void backToToday() {
        weekView.backToToday();
    }
}
