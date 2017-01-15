package org.unimelb.itime.ui.fragment.calendars;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.dayview.EventController;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.unitviews.DraggableEventView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Paul on 21/09/2016.
 */
public class CalendarWeekFragment extends BaseUiFragment<Object, EventCommonMvpView, EventCommonPresenter<EventCommonMvpView>> implements EventCommonMvpView {

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
        initViews();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    private void initViews(){
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
        weekView.setOnBodyOuterListener(new EventController.OnEventListener() {
            @Override
            public boolean isDraggable(DraggableEventView dayDraggableEventView) {
                Event event = (Event) dayDraggableEventView.getEvent();
                if (event.getEventType().equals("solo")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onEventCreate(DraggableEventView dayDraggableEventView) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dayDraggableEventView.getStartTimeM());
                ((MainActivity) getActivity()).startEventCreateActivity(calendar);
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
                if (event.getRecurrence().length > 0) {
                    // this is repeat event
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("this is a repeat event")
                            .setItems(EventUtil.getRepeatEventChangeOptions(getContext()), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
//
                                        case 0: {
                                            presenter.updateEvent(event, EventCommonPresenter.UPDATE_THIS, originEvent.getStartTime());
                                            break;
                                        }
                                        case 1: {
                                            presenter.updateEvent(event, EventCommonPresenter.UPDATE_FOLLOWING, originEvent.getStartTime());
                                            break;
                                        }
                                        case 2: {
                                            break;
                                        }

                                    }
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    // this is not repeat event
                    Event copyEvent = eventManager.copyCurrentEvent(event);
                    copyEvent.setStartTime(dayDraggableEventView.getStartTimeM());
                    copyEvent.setEndTime(dayDraggableEventView.getEndTimeM());
                    presenter.updateEvent(copyEvent, EventCommonPresenter.UPDATE_ALL, originEvent.getStartTime());
                }

            }

        });
    }


    @Override
    public EventCommonPresenter createPresenter() {
        presenter = new EventCommonPresenter(getActivity());
        return presenter;
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

    @Override
    public void setData(Object o) {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }
}
