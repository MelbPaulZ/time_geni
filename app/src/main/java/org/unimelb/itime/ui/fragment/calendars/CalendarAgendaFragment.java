package org.unimelb.itime.ui.fragment.calendars;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.managers.CalendarManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageEventRefresh;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.agendaview.AgendaViewBody;
import org.unimelb.itime.vendor.agendaview.MonthAgendaView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.Calendar;

/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarAgendaFragment extends BaseUiFragment {
    private View root;
    private MonthAgendaView monthAgendaView;
    private EventManager eventManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_calendar_agenda, container, false);
        }
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventManager = EventManager.getInstance(getContext());
        monthAgendaView = (MonthAgendaView) root.findViewById(R.id.month_agenda_view);
        monthAgendaView.setDayEventMap(eventManager.getEventsPackage());
        monthAgendaView.setOnEventClickListener(new AgendaViewBody.OnEventClickListener() {
            @Override
            public void onEventClick(ITimeEventInterface iTimeEventInterface) {
                EventUtil.startEditEventActivity(getContext(), getActivity(), iTimeEventInterface);
            }
        });
        monthAgendaView.setOnHeaderListener(new MonthAgendaView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                Log.i("Header", "monthAgendaView: ");
                CalendarManager.getInstance().setCurrentShowCalendar(myCalendar.getCalendar());
                eventManager.refreshRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }

            @Override
            public void backToToday() {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(MessageEventRefresh messageEvent){
        monthAgendaView.setDayEventMap(eventManager.getEventsPackage());
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollTo(CalendarManager.getInstance().getCurrentShowCalendar());

    }


    public void backToday(){
        monthAgendaView.backToToday();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadData(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_EVENT) {
            monthAgendaView.setDayEventMap(eventManager.getEventsPackage());
        }
    }

    @Override
    public MvpPresenter createPresenter() {
        return new EventCommonPresenter(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void setData(Object o) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void scrollTo(Calendar calendar){
        monthAgendaView.scrollTo(calendar);
    }

    public void calendarNotifyDataSetChanged(){
        if (monthAgendaView!=null) {
            monthAgendaView.setDayEventMap(eventManager.getEventsPackage());
        }
    }

}
