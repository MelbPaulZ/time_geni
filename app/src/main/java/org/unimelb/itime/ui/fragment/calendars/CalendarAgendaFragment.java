package org.unimelb.itime.ui.fragment.calendars;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.agendaview.AgendaViewBody;
import org.unimelb.itime.vendor.agendaview.MonthAgendaView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.List;

/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarAgendaFragment extends Fragment {
    private View root;
    private MonthAgendaView monthAgendaView;
    private final int ACTIVITY_EDITEVENT = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar_agenda,container,false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        monthAgendaView = (MonthAgendaView) root.findViewById(R.id.month_agenda_view);
        monthAgendaView.setDayEventMap(EventManager.getInstance().getEventsPackage());
        monthAgendaView.setOnEventClickListener(new AgendaViewBody.OnEventClickListener() {
            @Override
            public void onEventClick(ITimeEventInterface iTimeEventInterface) {
                EventUtil.startEditEventActivity(getContext(), getActivity(), iTimeEventInterface);
            }
        });
        monthAgendaView.setOnHeaderListener(new MonthAgendaView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }

            @Override
            public void backToToday() {

            }
        });
    }

    public void backToday(){
        monthAgendaView.backToToday();
    }


    @Subscribe
    public void loadData(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_EVENT) {
            monthAgendaView.setDayEventMap(EventManager.getInstance().getEventsPackage());
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
        if (monthAgendaView!=null) {
            monthAgendaView.setDayEventMap(EventManager.getInstance().getEventsPackage());
        }
    }

}
