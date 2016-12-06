package org.unimelb.itime.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.messageevent.MessageEventRefresh;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeEventPackageInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;
import static android.R.attr.key;

/**
 * Created by yuhaoliu on 29/08/16.
 */
public class EventManager {
    private final String TAG = "EventManager";
    private static EventManager ourInstance;

    private Event currentEvent = new Event();

    private Map<Long, List<ITimeEventInterface>> regularEventMap = new HashMap<>();

    private ArrayList<Event> orgRepeatedEventList = new ArrayList<>();
    private Map<Long, List<ITimeEventInterface>> repeatedEventMap = new HashMap<>();

    //<UUID, List of tracer> : For tracking event on Day of repeated event map
    private Map<String,ArrayList<EventTracer>> uidTracerMap = new HashMap();

    private EventsPackage eventsPackage = new EventsPackage();

    private final int defaultRepeatedRange = 50;
    // [0 - 1] percentage of frag for load more fur/pre event
    private final float refreshFlag = 0.9f;

    private Calendar nowRepeatedEndAt = Calendar.getInstance();
    private Calendar nowRepeatedStartAt = Calendar.getInstance();

    private Calendar calendar = Calendar.getInstance();

    public static EventManager getInstance() {
        if (ourInstance == null){
            ourInstance = new EventManager();
            loadDB();
        }

        return ourInstance;
    }

    public static EventManager getInstance(Context context){
        if (ourInstance == null){
            ourInstance = new EventManager();
            loadDB(context);
        }
        return ourInstance;
    }


    public void clearManager(){
        this.ourInstance = null;
    }

    private List<Event> waitingEditEventList= new ArrayList<>(); // this list contains all events that waits for update

    private EventManager() {
        setToBeginOfDay(nowRepeatedStartAt);
        setToBeginOfDay(nowRepeatedEndAt);

        nowRepeatedStartAt.add(Calendar.DATE, -defaultRepeatedRange);
        nowRepeatedEndAt.add(Calendar.DATE, defaultRepeatedRange);

        eventsPackage.setRepeatedEventMap(repeatedEventMap);
        eventsPackage.setRegularEventMap(regularEventMap);

//        EventBus.getDefault().register(this);
    }

    private Calendar setToBeginOfDay(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
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
            Log.i(TAG, "addEvent: addRepeatedEvent");
            this.addRepeatedEvent(event,nowRepeatedStartAt.getTimeInMillis(),nowRepeatedEndAt.getTimeInMillis());
        }
    }

    private synchronized void addRepeatedEvent(Event event, long rangeStart, long rangeEnd){
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

            //dup time is right

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

    public void refreshRepeatedEvent(long currentDate){
        boolean reachPreFlg = currentDate < this.getLoadPreFlag();
        boolean reachFurFlg = currentDate > this.getLoadFurFlag();
        if (reachPreFlg || reachFurFlg){
            //load more pre
            if (reachPreFlg){
                Calendar tempStart = Calendar.getInstance();
                tempStart.setTimeInMillis(nowRepeatedStartAt.getTimeInMillis());
                tempStart.add(Calendar.DATE,-defaultRepeatedRange);
                for (Event event:orgRepeatedEventList
                        ) {
                    this.addRepeatedEvent(event, tempStart.getTimeInMillis(), nowRepeatedStartAt.getTimeInMillis());
                }
                nowRepeatedStartAt.setTimeInMillis(tempStart.getTimeInMillis());
            }
            //load more future
            if (reachFurFlg){
                Calendar tempEnd = Calendar.getInstance();
                tempEnd.setTimeInMillis(nowRepeatedEndAt.getTimeInMillis());
                tempEnd.add(Calendar.DATE,defaultRepeatedRange);
                for (Event event:orgRepeatedEventList
                        ) {
                    this.addRepeatedEvent(event, nowRepeatedEndAt.getTimeInMillis(), tempEnd.getTimeInMillis());
                }
                nowRepeatedEndAt.setTimeInMillis(tempEnd.getTimeInMillis());
            }
            EventBus.getDefault().post(new MessageEventRefresh());
        }
    }

    private long getLoadPreFlag(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.nowRepeatedStartAt.getTimeInMillis());
        cal.add(Calendar.DATE, (int) (defaultRepeatedRange * (1 - refreshFlag)));
        return cal.getTimeInMillis();
    }

    private long getLoadFurFlag(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.nowRepeatedEndAt.getTimeInMillis());
        cal.add(Calendar.DATE, (int) (- defaultRepeatedRange * (1-refreshFlag)));
        return cal.getTimeInMillis();
    }

//    public void loadRepeatedEvent(long rangeStart, long rangeEnd){
//        this.nowRepeatedStartAt.setTimeInMillis(rangeStart);
//        this.nowRepeatedEndAt.setTimeInMillis(rangeEnd);
//        for (Event event:orgRepeatedEventList
//                ) {
//            this.removeRepeatedEvent(event);
//            this.addRepeatedEvent(event, rangeStart, rangeEnd);
//        }
//    }

    public void removeRepeatedEvent(Event event){
        List<EventTracer> tracers = uidTracerMap.get(event.getEventUid());
        if (tracers != null){
            for (EventTracer tracer:tracers
                    ) {
                tracer.removeSelfFromRepeatedEventMap();
            }
            uidTracerMap.remove(event.getEventUid());
        }
    }

//    public void updateRepeatedEvent(Event event){
//        removeRepeatedEvent(event);
//        addRepeatedEvent(event,nowRepeatedStartAt.getTimeInMillis(),nowRepeatedEndAt.getTimeInMillis());
//    }

    private long getDayBeginMilliseconds(long startTime){
        calendar.setTimeInMillis(startTime);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTimeInMillis();
    }

    // this is for event drag
//    public void updateEvent(Event oldEvent, long newStartTime, long newEndTime){
//        if (oldEvent.getRecurrence().length == 0){
//            long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());
//            if (this.regularEventMap.containsKey(oldBeginTime)){
//                Event updateEvent = null;
//                for (ITimeEventInterface ev : regularEventMap.get(oldBeginTime)){
//                    if (((Event)ev).getEventUid().equals(oldEvent.getEventUid())){
//                        updateEvent = (Event) ev;
//                        break;
//                    }
//                }
//                if (updateEvent!=null){
//                    this.regularEventMap.get(oldBeginTime).remove(updateEvent);
//                }
//                oldEvent.setStartTime(newStartTime);
//                oldEvent.setEndTime(newEndTime);
//                this.addEvent(oldEvent);
//            }
//        }else {
//
//        }
//    }

    // repeat event update need to change
    public void updateEvent(Event oldEvent, Event newEvent){
        long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());

        EventManager.getInstance().setCurrentEvent(newEvent);
        //if old not repeated
        if (oldEvent.getRecurrence().length == 0){
            if (this.regularEventMap.containsKey(oldBeginTime)){
                Event old = null;

                for (ITimeEventInterface iTimeEventInterface : regularEventMap.get(oldBeginTime)){
                    if ( ((Event)iTimeEventInterface).getEventUid().equals(oldEvent.getEventUid())){
                        old = (Event) iTimeEventInterface;
                    }
                }
                if (old != null){
                    regularEventMap.get(oldBeginTime).remove(old);
                    this.addEvent(newEvent);

                }else {
                    throw new RuntimeException("old event cannot be find in regularEventMap");
                }
            }
        }else{
            //if old is repeated
            this.removeRepeatedEvent(oldEvent);
            this.orgRepeatedEventList.remove(oldEvent);
            this.addEvent(newEvent);
        }
        // here update DB
        Event dbOldEvent = DBManager.getInstance().getEvent(oldEvent.getEventUid());
        dbOldEvent.delete();
        DBManager.getInstance().insertEvent(newEvent);
    }

    public List<ITimeEventInterface> getAllEvents(){
        List<Event> eventList = DBManager.getInstance().getAllEvents();
        ArrayList<ITimeEventInterface> allEvents = new ArrayList<>();
        for (Map.Entry<Long, List<ITimeEventInterface>> entry: regularEventMap.entrySet()){
            allEvents.addAll(entry.getValue());
        }
        allEvents.addAll(orgRepeatedEventList);
        return allEvents;
    }

    public Event findEventByUid(Context context, String eventUid){

        Event event = DBManager.getInstance(context).getEvent(eventUid);
        if (event==null){
            return null;
        }
        Long key = EventManager.getInstance().getDayBeginMilliseconds(event.getStartTime());
        if (event.getRecurrence().length!=0){
            // repeat event
            for (Event ev : orgRepeatedEventList){
                if (ev.getEventUid().equals(eventUid)){
                    return ev;
                }
            }
        }else {
            // non-repeat event
            if (regularEventMap.containsKey(key)) {
                for (ITimeEventInterface iTimeEventInterface : regularEventMap.get(key)) {
                    if (((Event) iTimeEventInterface).getEventUid().equals(eventUid)) {
                        return (Event) iTimeEventInterface;
                    }
                }
            }
        }
        return null;
    }

    public Event copyCurrentEvent(Event event){
        Gson gson = new Gson();

        String eventStr = gson.toJson(event);
        Event copyEvent = gson.fromJson(eventStr, Event.class);

        Type dataType = new TypeToken <RuleModel<Event>>() {}.getType();
        RuleModel response = gson.fromJson(gson.toJson(event.getRule(), dataType), dataType);
        copyEvent.setRule(response);

        return copyEvent;
    }

    public Event getNewEvent(){
        return new Event();
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

    public List<Event> getWaitingEditEventList() {
        return waitingEditEventList;
    }


    public static void loadDB(Context context){
        List<Event> list = DBManager.getInstance(context).getAllEvents();
        for (Event ev: list) {
            EventManager.getInstance().addEvent(ev);
        }
    }

    public static void loadDB(){
        List<Event> list = DBManager.getInstance().getAllEvents();
        for (Event ev: list) {
            EventManager.getInstance().addEvent(ev);
        }
    }

    public void updateDB(List<Event> events){
        List<? extends ITimeEventInterface> orgITimeInterfaces = EventManager.getInstance().getAllEvents();
        List<Event> orgEvents = (List<Event>)  orgITimeInterfaces;

        for (Event event:events
                ) {
            Event orgOld = null;

            for (Event orgEvent:orgEvents
                    ) {
                if (orgEvent.getEventUid().equals(event.getEventUid())){
                    orgOld = orgEvent;
                    EventManager.getInstance().updateEvent(orgOld,event);
                    break;
                }
            }

            if (orgOld == null){
                DBManager.getInstance().insertEvent(event);
                EventManager.getInstance().addEvent(event);
            }
        }
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

    public Event findOrgByUUID(String UUID){
        for (Event orgE:orgRepeatedEventList
             ) {
            if (orgE.getEventUid().equals(UUID)){
                return orgE;
            }
        }
        throw new RuntimeException("findOrgByUUID: cannot find org event by UUID: " + UUID);
    }

    /** priority search in waiting list, if not find, then go through all events
     * */
    public Event findEventByUUID(String UUID){
        for (Event event : waitingEditEventList){
            if (event.getEventUid().equals(UUID)){
                return event;
            }
        }

        // if event is not in waiting list, should never happens
        throw new RuntimeException("cannot find event in waiting list");
    }

}
