package org.unimelb.itime.ui.fragment.calendars;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.CalendarManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageEventRefresh;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.dayview.EventController;
import org.unimelb.itime.vendor.dayview.MonthDayView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.unitviews.DraggableEventView;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarMonthDayFragment extends BaseUiFragment<Object ,EventCommonMvpView, EventCommonPresenter<EventCommonMvpView>> implements EventCommonMvpView {
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

    @Override
    public void setData(Object o) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void backToday(){
        monthDayView.backToToday();
    }

    private void initView(){
        monthDayView = (MonthDayView) root.findViewById(R.id.month_day_view);
        //Set the data source with format of ITimeEventPackageInterface
        //ITimeEventPackageInterface is composed by two parts:
        //  1: regular events. 2: repeated events.
        monthDayView.setDayEventMap(eventManager.getEventsPackage());
        //If creating instance of event is needed, set the class.
        monthDayView.setEventClassName(Event.class);
        monthDayView.setOnHeaderListener(new MonthDayView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                CalendarManager.getInstance().setCurrentShowCalendar(myCalendar.getCalendar());
                eventManager.syncRepeatedEvent(myCalendar.getCalendar().getTimeInMillis());
                EventBus.getDefault().post(new MessageMonthYear(myCalendar.getYear(), myCalendar.getMonth()));
            }
        });
        monthDayView.setOnBodyOuterListener(new EventController.OnEventListener(){
            @Override
            public boolean isDraggable(DraggableEventView dayDraggableEventView) {
                Event event = (Event) dayDraggableEventView.getEvent();
                if (event.getEventType().equals("solo")){
                    return true;
                }else{
                    return false;
                }
            }

            @Override
            public void onEventCreate(DraggableEventView dayDraggableEventView) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dayDraggableEventView.getStartTimeM());
                ((MainActivity)getActivity()).startEventCreateActivity(calendar);
            }

            @Override
            public void onEventClick(DraggableEventView dayDraggableEventView) {
                Event event = EventManager.getInstance(getContext()).findEventByUid(dayDraggableEventView.getEvent().getEventUid());
                EventUtil.startEditEventActivity(getContext(), getActivity(), event);
            }

            @Override
            public void onEventDragStart(DraggableEventView dayDraggableEventView) {

            }

            @Override
            public void onEventDragging(DraggableEventView dayDraggableEventView, int i, int i1) {

            }

            @Override
            public void onEventDragDrop(final DraggableEventView dayDraggableEventView) {

                final Event originEvent = (Event) dayDraggableEventView.getEvent();
                final Event event = eventManager.copyCurrentEvent(originEvent);
                event.setStartTime(dayDraggableEventView.getStartTimeM());
                event.setEndTime(dayDraggableEventView.getEndTimeM());
                if (event.getRecurrence().length>0){
                    // this is repeat event
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("this is a repeat event")
                            .setItems(EventUtil.getRepeatEventChangeOptions(getContext()), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
//
                                        case 0:{
                                            presenter.updateEvent(event, EventCommonPresenter.UPDATE_THIS, originEvent.getStartTime());
                                            break;
                                        }case 1:{
                                            presenter.updateEvent(event, EventCommonPresenter.UPDATE_FOLLOWING, originEvent.getStartTime());
                                            break;
                                        }case 2:{
                                            break;
                                        }

                                    }
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    // this is not repeat event
                    Event copyEvent = eventManager.copyCurrentEvent(event);
                    copyEvent.setStartTime(dayDraggableEventView.getStartTimeM());
                    copyEvent.setEndTime(dayDraggableEventView.getEndTimeM());
                    presenter.updateEvent(copyEvent, EventCommonPresenter.UPDATE_ALL, originEvent.getStartTime());
                }

            }
        });
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
            monthDayView.requestLayout(); // need?
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // to check
        scrollTo(CalendarManager.getInstance().getCurrentShowCalendar());

    }



    public void scrollTo(Calendar calendar){
        monthDayView.scrollTo(calendar);
    }

    public void scrollToWithOffset(long time){
        monthDayView.scrollToWithOffset(time);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Log.i(TAG, "scrollToWithOffset: " + c.getTime());
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

    @Override
    public ToolbarViewModel getToolbarViewModel() {
        return null;
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }
}

