package org.unimelb.itime.ui.fragment.calendars;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.dayview.EventController;
import org.unimelb.itime.vendor.unitviews.DraggableEventView;

import java.util.List;

/**
 * Created by yinchuandong on 13/1/17.
 */

public abstract class CalendarBaseViewFragment extends BaseUiAuthFragment<MainCalendarMvpView, EventPresenter<MainCalendarMvpView>> implements MainCalendarMvpView{

    public final static int REQ_EVENT_CREATE = 1000;
    public final static int REQ_EVENT_DETAIL = 1001;

    private EventPresenter<MainCalendarMvpView> presenter;


    @Override
    public EventPresenter<MainCalendarMvpView> createPresenter() {
        this.presenter = new EventPresenter<>(getContext());
        return this.presenter;
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {

    }

    @Override
    public void onTaskError(int taskId) {

    }

    public void toEventCreatePage(long startTime) {
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra(BaseActivity.TASK, BaseActivity.TASK_SELF_CREATE_EVENT);
        intent.putExtra("start_time", startTime);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getContext(), R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        startActivityForResult(intent, REQ_EVENT_CREATE, bundleAnimation);
    }

    public void toEventDetailPage(String eventUid, long startTime) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(BaseActivity.TASK, BaseActivity.TASK_SELF_DETAIL_EVENT);
        intent.putExtra("start_time", startTime);
        intent.putExtra("event_uid", eventUid);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getContext(), R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        startActivityForResult(intent, REQ_EVENT_DETAIL, bundleAnimation);
    }

    public void toSearchEventPage() {

    }

    public abstract void backToToday();


    public class EventItemListener implements EventController.OnEventListener{
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
            toEventCreatePage(dayDraggableEventView.getStartTimeM());
        }

        @Override
        public void onEventClick(DraggableEventView dayDraggableEventView) {
            Event event = (Event) dayDraggableEventView.getEvent();
            toEventDetailPage(event.getEventUid(), event.getStartTime());
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
            final Event event = EventUtil.copyEvent(originEvent);
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
                Event copyEvent = EventUtil.copyEvent(event);
                copyEvent.setStartTime(dayDraggableEventView.getStartTimeM());
                copyEvent.setEndTime(dayDraggableEventView.getEndTimeM());
                presenter.updateEvent(copyEvent, EventCommonPresenter.UPDATE_ALL, originEvent.getStartTime());
            }

        }

    }
}
