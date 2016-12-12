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
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageEventRefresh;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.CommonMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.dayview.MonthDayView;
import org.unimelb.itime.vendor.eventview.DayDraggableEventView;
import org.unimelb.itime.vendor.helper.MyCalendar;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Paul on 21/09/2016.
 */
public class ViewInCalendarMonthDayFragment extends CalendarMonthDayFragment implements CommonMvpView {
    private View root;
    private MonthDayView monthDayView;
    private CommonPresenter presenter;
    private String TAG = "MonthDayFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root ==null){
            root = inflater.inflate(R.layout.fragment_calendar_monthday, container, false);
        }
        initView();
        return root;
    }

    @Override
    public CommonPresenter createPresenter() {
        this.presenter = new CommonPresenter(getActivity());
        return presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(){
        monthDayView = (MonthDayView) root.findViewById(R.id.month_day_view);
        monthDayView.removeAllOptListener();
        monthDayView.setDayEventMap(EventManager.getInstance().getEventsPackage());
        monthDayView.setEventClassName(Event.class);
        monthDayView.setOnHeaderListener(new MonthDayView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                Log.i("Header", "monthDayView: " + myCalendar.getCalendar().getTime());
                CalendarManager.getInstance().setCurrentShowCalendar(myCalendar.getCalendar());
                EventManager.getInstance().refreshRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
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
            monthDayView.setDayEventMap(EventManager.getInstance().getEventsPackage());
            monthDayView.reloadEvents();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(MessageEventRefresh messageEvent){
        monthDayView.setDayEventMap(EventManager.getInstance().getEventsPackage());
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
        scrollTo(CalendarManager.getInstance().getCurrentShowCalendar());

    }


    public void scrollTo(Calendar calendar){
        monthDayView.scrollTo(calendar);
    }

    public void scrollToWithOffset(long time){
        monthDayView.scrollToWithOffset(time);
    }

    @Override
    public void onShowDialog() {
        AppUtil.showProgressBar(getActivity(),"Updating","Please wait...");
    }

    @Override
    public void onHideDialog() {
        AppUtil.hideProgressBar();
    }
}

