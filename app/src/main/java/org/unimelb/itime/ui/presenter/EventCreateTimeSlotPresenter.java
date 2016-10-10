package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeSlotPresenter extends MvpBasePresenter<EventCreateNewTimeSlotMvpView> {
    private final String TAG = "CreateTimeSlotPresenter";
    private Context context;
    private LayoutInflater inflater;
    private EventApi eventApi;
    private Event event;
    public EventCreateTimeSlotPresenter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public void setEvent(Event event){
        this.event = event;
    }

    public Context getContext() {
        return context;
    }


    public LayoutInflater getInflater() {
        return inflater;
    }

    public void getTimeSlots(long startTime) {
        List<Invitee> invitees = event.getInvitee();

        Observable<HttpResult<List<TimeSlot>>> observable = eventApi.recommend(invitees, startTime);
        Subscriber<HttpResult<List<TimeSlot>>> subscriber = new Subscriber<HttpResult<List<TimeSlot>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<List<TimeSlot>> listHttpResult) {
//                List<TimeSlot>
                Log.i(TAG, "onNext: ");
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }


    public void initData(Event event){
            //        ******************************simulate data
            // simulate timeSlots
//        WeekView.TimeSlotStruct
            Map simulateTimeSlots = new HashMap<>();
            ArrayList<TimeSlot> timeSlots = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH,11);
            calendar.set(Calendar.HOUR_OF_DAY,5);
            calendar.set(Calendar.MINUTE,30);
            simulateTimeSlots.put(calendar.getTime().getTime(),false);
            TimeSlot timeSlot1 = new TimeSlot();
            timeSlot1.setTimeSlotUid(EventUtil.generateTimeSlotUid());
            timeSlot1.setStatus(context.getString(R.string.timeslot_status_create));
            timeSlot1.setEventUid(event.getEventUid());
            timeSlot1.setStartTime(calendar.getTimeInMillis());
            timeSlot1.setEndTime(calendar.getTimeInMillis() + 3600000);
            timeSlots.add(timeSlot1);


            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.DAY_OF_MONTH,12);
            calendar1.set(Calendar.HOUR_OF_DAY,7);
            calendar1.set(Calendar.MINUTE,45);
            simulateTimeSlots.put(calendar1.getTime().getTime(),false);
            TimeSlot timeSlot2 = new TimeSlot();
            timeSlot2.setTimeSlotUid(EventUtil.generateTimeSlotUid());
            timeSlot2.setEventUid(event.getEventUid());
            timeSlot2.setStatus(context.getString(R.string.timeslot_status_create));
            timeSlot2.setStartTime(calendar1.getTimeInMillis());
            timeSlot2.setEndTime(calendar1.getTimeInMillis() + 3600000);
            timeSlots.add(timeSlot2);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.DAY_OF_MONTH, 13);
            calendar2.set(Calendar.HOUR_OF_DAY,9);
            calendar2.set(Calendar.MINUTE,0);
            simulateTimeSlots.put(calendar2.getTimeInMillis(),false);
            TimeSlot timeSlot3 = new TimeSlot();
            timeSlot3.setStatus(context.getString(R.string.timeslot_status_create));
            timeSlot3.setTimeSlotUid(EventUtil.generateTimeSlotUid());
            timeSlot3.setEventUid(event.getEventUid());
            timeSlot3.setStartTime(calendar2.getTimeInMillis());
            timeSlot3.setEndTime(calendar2.getTimeInMillis()+3600000);
            timeSlots.add(timeSlot3);

            event.setTimeslot(timeSlots);
    }

}
