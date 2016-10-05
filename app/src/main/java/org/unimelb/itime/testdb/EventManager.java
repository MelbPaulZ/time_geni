package org.unimelb.itime.testdb;

import com.google.gson.Gson;

import org.unimelb.itime.bean.Event;
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

    public void updateEvent(Event oldEvent, long newStartTime, long newEndTime){
        // problem here
        long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());
        if (this.eventMap.containsKey(oldBeginTime)){
            int sizeBeforeRM = eventMap.get(oldBeginTime).size();
            Event event = (Event) this.eventMap.get(oldBeginTime).get(0);
            this.eventMap.get(oldBeginTime).remove(oldEvent);
            int sizeAfterRM = eventMap.get(oldBeginTime).size();
            oldEvent.setStartTime(newStartTime);
            oldEvent.setEndTime(newEndTime);
            this.addEvent(oldEvent);
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
}
