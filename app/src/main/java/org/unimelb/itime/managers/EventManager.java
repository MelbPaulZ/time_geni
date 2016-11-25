package org.unimelb.itime.managers;

import com.google.gson.Gson;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhaoliu on 29/08/16.
 */
public class EventManager {
    private final String TAG = "MyAPP";
    private static EventManager ourInstance = new EventManager();

    private Event currentEvent = new Event();

    Map<Long, List<ITimeEventInterface>> eventMap = new HashMap<>();
    Calendar calendar = Calendar.getInstance();

    public static EventManager getInstance() {
        return ourInstance;
    }

    private EventManager() {
    }

    public Map<Long, List<ITimeEventInterface>> getEventsMap(){

        return this.eventMap;
    }

    public void addEvent(Event event){
        Long startTime = event.getStartTime();
        Long dayBeginMilliseconds = getDayBeginMilliseconds(startTime);

        if (eventMap.containsKey(dayBeginMilliseconds)){
            eventMap.get(dayBeginMilliseconds).add(event);
        }else {
            List<ITimeEventInterface> events = new ArrayList<>();
            eventMap.put(dayBeginMilliseconds,events);
            eventMap.get(dayBeginMilliseconds).add(event);
        }
    }

    private long getDayBeginMilliseconds(long startTime){
        calendar.setTimeInMillis(startTime);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTimeInMillis();
    }

    // this is for event drag
    public void updateEvent(Event oldEvent, long newStartTime, long newEndTime){
        long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());
        if (this.eventMap.containsKey(oldBeginTime)){
            Event updateEvent = null;
            for (ITimeEventInterface ev : eventMap.get(oldBeginTime)){
                if (((Event)ev).getEventUid().equals(oldEvent.getEventUid())){
                    updateEvent = (Event) ev;
                    break;
                }
            }
            if (updateEvent!=null){
                this.eventMap.get(oldBeginTime).remove(updateEvent);
            }
            oldEvent.setStartTime(newStartTime);
            oldEvent.setEndTime(newEndTime);
            this.addEvent(oldEvent);
        }
    }

    public void updateEvent(Event oldEvent, Event newEvent){
        long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());
        List<ITimeEventInterface> newList = new ArrayList<>();
        if (this.eventMap.containsKey(oldBeginTime)){
            List<ITimeEventInterface> eventList = eventMap.get(oldBeginTime);
            for (ITimeEventInterface iTimeEventInterface: eventList){
                if ( !((Event)iTimeEventInterface).getEventUid().equals(oldEvent.getEventUid())){
                    newList.add(iTimeEventInterface);
                }
            }
            eventMap.get(oldBeginTime).clear();
            eventMap.put(oldBeginTime, newList);
            // so far remove the old event, next to add new event
            this.addEvent(newEvent);



//            int id = eventMap.get(oldBeginTime).indexOf(oldEvent);
//
//            Event old = (Event) eventMap.get(oldBeginTime).get(id);
//            eventMap.get(oldBeginTime).remove(old);
//
//            this.addEvent(newEvent);
            EventManager.getInstance().setCurrentEvent(newEvent);
        }
    }


    public Event copyCurrentEvent(Event event){
        Gson gson = new Gson();
        String eventStr = gson.toJson(event);
        Event copyEvent = gson.fromJson(eventStr, Event.class);
        return copyEvent;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public boolean isTimeslotExistInEvent(Event event, Timeslot timeslot){
        if (event.hasTimeslots()) {
            List<Timeslot> timeslotList = event.getTimeslot();
            for (Timeslot ts : timeslotList){
                long tsSecond= ts.getStartTime()/(1000*60*15);
                long recSecond = timeslot.getStartTime()/(1000*60*15);
                if(tsSecond == recSecond){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    public String getHostInviteeUid(Event event){
        for (Invitee invitee: event.getInvitee()){
            if (invitee.getIsHost() == 1){
                // 1 means is host
                return invitee.getInviteeUid();
            }
        }
        return null;
    }
}
