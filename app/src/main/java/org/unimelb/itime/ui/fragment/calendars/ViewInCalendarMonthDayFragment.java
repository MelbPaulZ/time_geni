package org.unimelb.itime.ui.fragment.calendars;

import android.content.Intent;
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
import org.unimelb.itime.messageevent.MessageEventRefresh;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.vendor.dayview.MonthDayView;
import org.unimelb.itime.vendor.helper.MyCalendar;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Paul on 21/09/2016.
 */
public class ViewInCalendarMonthDayFragment extends CalendarMonthDayFragment implements EventCommonMvpView {
    private View root;
    private MonthDayView monthDayView;
    private EventCommonPresenter presenter;
    private String TAG = "MonthDayFragment";
    private EventManager eventManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root ==null){
            root = inflater.inflate(R.layout.fragment_calendar_monthday, container, false);
        }
        eventManager = EventManager.getInstance(getContext());
        initView();
        return root;
    }

    @Override
    public EventCommonPresenter createPresenter() {
        this.presenter = new EventCommonPresenter(getActivity());
        return presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showAnim(Event event){

    }

    private void initView(){
        monthDayView = (MonthDayView) root.findViewById(R.id.month_day_view);
        monthDayView.removeAllOptListener();
        monthDayView.setDayEventMap(eventManager.getEventsPackage());
        monthDayView.setEventClassName(Event.class);
        monthDayView.setOnHeaderListener(new MonthDayView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                eventManager.refreshRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    public void backToday(){
        monthDayView.backToToday();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadData(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_EVENT) {
            monthDayView.setDayEventMap(eventManager.getEventsPackage());
            monthDayView.reloadEvents();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(MessageEventRefresh messageEvent){
        monthDayView.setDayEventMap(eventManager.getEventsPackage());
        monthDayView.reloadEvents();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void calendarNotifyDataSetChanged(){
        if(monthDayView!=null) {
            monthDayView.reloadEvents();
            monthDayView.requestLayout(); // need?
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        scrollTo(CalendarManager.getInstance().getCurrentShowCalendar());

    }


    public void scrollTo(Calendar calendar){
        monthDayView.scrollTo(calendar);
    }

    public void scrollToWithOffset(long time){
        monthDayView.scrollToWithOffset(time);
    }


    @Override
    public void onTaskStart(int task) {
        AppUtil.showProgressBar(getActivity(),"Updating","Please wait...");
    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {
        AppUtil.hideProgressBar();

    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {
        AppUtil.hideProgressBar();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}

