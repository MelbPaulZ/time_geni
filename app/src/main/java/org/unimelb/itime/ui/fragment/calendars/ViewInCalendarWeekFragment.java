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
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Paul on 21/09/2016.
 */
public class ViewInCalendarWeekFragment extends CalendarWeekFragment implements EventCommonMvpView {

    private View root;
    private WeekView weekView;
    private EventCommonPresenter presenter;
    private String TAG = "CalendarWeekFragment";
    private EventManager eventManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_week_view, container, false);
        }
        initView();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void showAnim(final Event event){
        if (this.weekView != null){
            this.weekView.showEventAnim(event);
        }
    }

    private void initView(){
        eventManager = EventManager.getInstance(getContext());
        weekView = (WeekView) root.findViewById(R.id.week_view);
        weekView.setDayEventMap(eventManager.getEventsPackage());
        weekView.removeAllOptListener();
        weekView.setEventClassName(Event.class);
        weekView.setOnHeaderListener(new WeekView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                eventManager.syncRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
    }

    public void backToday() {
        weekView.backToToday();
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


    @Override
    public EventCommonPresenter createPresenter() {
        presenter = new EventCommonPresenter(getActivity());
        return presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
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
    public void onTaskStart(int task) {
        if (task == EventCommonPresenter.TASK_EVENT_UPDATE) {
            AppUtil.showProgressBar(getActivity(), "Updating", "Please wait...");
        }
    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {
        if (task == EventCommonPresenter.TASK_EVENT_UPDATE) {
            AppUtil.hideProgressBar();
        }

    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {
        if (task == EventCommonPresenter.TASK_EVENT_UPDATE) {
            AppUtil.hideProgressBar();
        }
    }
}
