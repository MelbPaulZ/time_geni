package org.unimelb.itime.ui.fragment.calendars;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.eventview.DayDraggableEventView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarWeekFragment extends Fragment {

    private View root;
    private WeekView weekView;

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
        weekView.setDayEventMap(EventManager.getInstance().getEventsMap());
        weekView.setEventClassName(Event.class);
        weekView.setOnHeaderListener(new WeekView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
        weekView.setOnBodyOuterListener(new FlexibleLenViewBody.OnBodyListener() {
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
            public void onEventDragDrop(DayDraggableEventView dayDraggableEventView) {
                EventManager.getInstance().updateEvent((Event) dayDraggableEventView.getEvent(),
                        dayDraggableEventView.getStartTimeM(), dayDraggableEventView.getEndTimeM());
                ((Event)dayDraggableEventView.getEvent()).update();
                weekView.reloadEvents();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadData(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.INIT_DB) {
            weekView.setDayEventMap(EventManager.getInstance().getEventsMap());
            weekView.reloadEvents();
        }
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
}
