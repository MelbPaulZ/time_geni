package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditPresenter extends EventCommonPresenter<EventEditMvpView> {
    private Context context;
    private EventApi eventApi;
    private String TAG = "EventEditPresenter";


    public EventEditPresenter(Context context) {
        super(context);
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public void deleteEvent(Event event){
        Observable<HttpResult<Event>> observable = eventApi.delete(CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid(), event.getEventUid());
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                Log.i(TAG, "onNext: " + eventHttpResult.getData().getSummary());
                // todo delete event

            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void changeLocation(){
        EventEditMvpView view = getView();
        if (view != null){
            view.changeLocation();
        }
    }

    public void updateEvent(Event newEvent){
        updateEventToServer(newEvent);
    }

    /** this method is for change repeat event
     * */
    public void updateAndInsertEvent(Event orgEvent, Event event){
        Toast.makeText(getContext(), "waiting for server update", Toast.LENGTH_SHORT).show();
        super.updateAndInsertEvent(orgEvent, event);
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
