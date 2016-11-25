package org.unimelb.itime.managers;

import com.google.gson.Gson;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeEventPackageInterface;

import java.lang.reflect.Array;
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

    private Map<Long, List<ITimeEventInterface>> regularEventMap = new HashMap<>();

    private ArrayList<Event> orgRepeatedEventList = new ArrayList<>();
    private Map<Long, List<ITimeEventInterface>> repeatedEventMap = new HashMap<>();

    //<UUID, List of tracer> : For tracking event on Day of repeated event map
    private Map<String,ArrayList<EventTracer>> uidTracerMap = new HashMap();

    private EventsPackage eventsPackage = new EventsPackage();

    private final int defaultRepeatedRange = 500;

    private Calendar nowRepeatedEndAt = Calendar.getInstance();
    private Calendar nowRepeatedStartAt = Calendar.getInstance();

    private Calendar calendar = Calendar.getInstance();

    public static EventManager getInstance() {
        return ourInstance;
    }

    private EventManager() {
        nowRepeatedStartAt.add(Calendar.DATE, -defaultRepeatedRange);
        nowRepeatedEndAt.add(Calendar.DATE, defaultRepeatedRange);

        eventsPackage.setRepeatedEventMap(repeatedEventMap);
        eventsPackage.setRegularEventMap(regularEventMap);
    }

    public EventsPackage getEventsPackage(){
        return eventsPackage;
    }

    public void addEvent(Event event){
        //if not repeated
        if (event.getRecurrence().length == 0){
            Long startTime = event.getStartTime();
            Long dayBeginMilliseconds = getDayBeginMilliseconds(startTime);

            if (regularEventMap.containsKey(dayBeginMilliseconds)){
                regularEventMap.get(dayBeginMilliseconds).add(event);
            }else {
                regularEventMap.put(dayBeginMilliseconds,new ArrayList<ITimeEventInterface>());
                regularEventMap.get(dayBeginMilliseconds).add(event);
            }
        }else{
            orgRepeatedEventList.add(event);
            this.addRepeatedEvent(event,nowRepeatedStartAt.getTimeInMillis(),nowRepeatedEndAt.getTimeInMillis());
        }
    }

    private void addRepeatedEvent(Event event, long rangeStart, long rangeEnd){
        RuleModel rule = RuleFactory.getInstance().getRuleModel(event);
        event.setRule(rule);

        ArrayList<Long> repeatedEventsTimes = rule.getOccurenceDates(rangeStart,rangeEnd);

        for (Long time: repeatedEventsTimes
                ) {
            Event dup_event = null;
            try {
                dup_event = (Event) event.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (dup_event == null){
                throw new RuntimeException("Clone error");
            }
            long duration = dup_event.getDurationMilliseconds();
            dup_event.setStartTime(time);
            dup_event.setEndTime(time + duration);

            Long startTime = dup_event.getStartTime();
            Long dayBeginMilliseconds = getDayBeginMilliseconds(startTime);

            EventTracer tracer = new EventTracer(this.repeatedEventMap, dup_event, dayBeginMilliseconds);

            //add event to uuid - tracer map for tracking back to delete on day map.
            if (uidTracerMap.containsKey(event.getEventUid())){
                uidTracerMap.get(event.getEventUid()).add(tracer);
            }else {
                uidTracerMap.put(event.getEventUid(), new ArrayList<EventTracer>());
                uidTracerMap.get(event.getEventUid()).add(tracer);
            }

            //add event to repeated map
            if (repeatedEventMap.containsKey(dayBeginMilliseconds)){
                repeatedEventMap.get(dayBeginMilliseconds).add(dup_event);
            }else {
                repeatedEventMap.put(dayBeginMilliseconds,new ArrayList<ITimeEventInterface>());
                repeatedEventMap.get(dayBeginMilliseconds).add(dup_event);
            }
        }
    }

    public void loadRepeatedEvent(long rangeStart, long rangeEnd){
        this.nowRepeatedStartAt.setTimeInMillis(rangeStart);
        this.nowRepeatedEndAt.setTimeInMillis(rangeEnd);
        for (Event event:orgRepeatedEventList
                ) {
            this.addRepeatedEvent(event, rangeStart, rangeEnd);
        }
    }

    public void removeRepeatedEvent(Event event){
        List<EventTracer> tracers = uidTracerMap.get(event.getEventUid());
        for (EventTracer tracer:tracers
                ) {
            tracer.removeSelfFromRepeatedEventMap();
        }
        uidTracerMap.remove(event.getEventUid());
    }

    public void updateRepeatedEvent(Event event){
        removeRepeatedEvent(event);
        addRepeatedEvent(event,nowRepeatedStartAt.getTimeInMillis(),nowRepeatedEndAt.getTimeInMillis());
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
        if (this.regularEventMap.containsKey(oldBeginTime)){
            Event updateEvent = null;
            for (ITimeEventInterface ev : regularEventMap.get(oldBeginTime)){
                if (((Event)ev).getEventUid().equals(oldEvent.getEventUid())){
                    updateEvent = (Event) ev;
                    break;
                }
            }
            if (updateEvent!=null){
                this.regularEventMap.get(oldBeginTime).remove(updateEvent);
            }
            oldEvent.setStartTime(newStartTime);
            oldEvent.setEndTime(newEndTime);
            this.addEvent(oldEvent);
        }
    }

    public void updateEvent(Event oldEvent, Event newEvent){
        long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());

        if (this.regularEventMap.containsKey(oldBeginTime)){
            int id = regularEventMap.get(oldBeginTime).indexOf(oldEvent);

            Event old = (Event) regularEventMap.get(oldBeginTime).get(id);
            regularEventMap.get(oldBeginTime).remove(old);


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
        // here the event rule needs to be manually copied to the copyEvent, because the rule is not a json
        

        copyEvent.setRule();
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

    public String getHostInviteeUid(Event event) {
        for (Invitee invitee : event.getInvitee()) {
            if (invitee.getIsHost() == 1) {
                // 1 means is host
                return invitee.getInviteeUid();
            }
        }
        return null;
    }


    public class EventsPackage implements ITimeEventPackageInterface {

        private Map<Long, List<ITimeEventInterface>> regularEventMap;
        private Map<Long, List<ITimeEventInterface>> repeatedEventMap;

        public void setRegularEventMap(Map<Long, List<ITimeEventInterface>> regularEventMap) {
            this.regularEventMap = regularEventMap;
        }

        public void setRepeatedEventMap(Map<Long, List<ITimeEventInterface>> repeatedEventMap) {
            this.repeatedEventMap = repeatedEventMap;
        }

        public void clearPackage(){
            this.regularEventMap.clear();
            //** here need to handle the linked effect.
//            this.repeatedMap.clear();
        }

        @Override
        public Map<Long, List<ITimeEventInterface>> getRegularEventDayMap() {
            return regularEventMap;
        }

        @Override
        public Map<Long,List<ITimeEventInterface>> getRepeatedEventDayMap() {
            return repeatedEventMap;
        }
    }

    public class EventTracer{
        private Map<Long, List<ITimeEventInterface>> repeatedEventMap;
        private ITimeEventInterface event;
        private long belongToDayOfBegin;

        public EventTracer(Map<Long, List<ITimeEventInterface>> repeatedEventMap
                , ITimeEventInterface event,long belongToDayOfBegin){
            this.repeatedEventMap = repeatedEventMap;
            this.event = event;
            this.belongToDayOfBegin = belongToDayOfBegin;
        }

        public void removeSelfFromRepeatedEventMap(){
            repeatedEventMap.get(belongToDayOfBegin).remove(event);
        }
    }
}
