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
import org.unimelb.itime.vendor.dayview.MonthDayView;
import org.unimelb.itime.vendor.eventview.DayDraggableEventView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarMonthDayFragment extends Fragment {
    private View root;
    private MonthDayView monthDayView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root ==null){
            root = inflater.inflate(R.layout.fragment_calendar_monthday, container, false);
        }
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        monthDayView = (MonthDayView) root.findViewById(R.id.month_day_view);
        Map<Long, List<ITimeEventInterface>> map = EventManager.getInstance().getEventsMap();
        monthDayView.setDayEventMap(EventManager.getInstance().getEventsMap());
        monthDayView.setEventClassName(Event.class);
        monthDayView.setOnHeaderListener(new MonthDayView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
        monthDayView.setOnBodyOuterListener(new FlexibleLenViewBody.OnBodyListener() {
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
                monthDayView.reloadEvents();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadData(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_EVENT) {
            monthDayView.setDayEventMap(EventManager.getInstance().getEventsMap());
            Calendar calendar = Calendar.getInstance();
            long time = calendar.getTimeInMillis();
            monthDayView.reloadEvents();
            monthDayView.requestLayout();
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
        if(monthDayView!=null) {
            monthDayView.reloadEvents();
        }
    }
}

