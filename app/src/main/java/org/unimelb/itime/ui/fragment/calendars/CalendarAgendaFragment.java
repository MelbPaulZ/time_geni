package org.unimelb.itime.ui.fragment.calendars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.agendaview.AgendaViewBody;
import org.unimelb.itime.vendor.agendaview.MonthAgendaView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

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
        monthAgendaView.setDayEventMap(EventManager.getInstance().getEventsMap());
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
        });
    }
}
