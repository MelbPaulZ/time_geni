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
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeEventPackageInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhaoliu on 29/08/16.
 */
public class EventManager {
    private final String TAG = "EventManager";
    private static EventManager instance;

    private Event currentEvent = new Event();

    private Map<Long, List<ITimeEventInterface>> regularEventMap = new HashMap<>();

    private ArrayList<Event> orgRepeatedEventList = new ArrayList<>();
    private Map<Long, List<ITimeEventInterface>> repeatedEventMap = new HashMap<>();

    //<UUID, List of tracer> : For tracking event on Day of repeated event map
    private Map<String,ArrayList<EventTracer>> uidTracerMap = new HashMap();

    //recurrence uid (origin event uid) : special events for origin event
    private Map<String, ArrayList<Event>> specialEvent = new HashMap<>();

    private EventsPackage eventsPackage = new EventsPackage();

    private final int defaultRepeatedRange = 500;
    // [0 - 1] percentage of frag for load more fur/pre event
    private final float refreshFlag = 0.9f;

    private Calendar nowRepeatedEndAt = Calendar.getInstance();
    private Calendar nowRepeatedStartAt = Calendar.getInstance();

    private Calendar calendar = Calendar.getInstance();
    private Context context;

    public EventManager(Context context){
        this.context = context;
        setToBeginOfDay(nowRepeatedStartAt);
        setToBeginOfDay(nowRepeatedEndAt);

        nowRepeatedStartAt.add(Calendar.DATE, -defaultRepeatedRange);
        nowRepeatedEndAt.add(Calendar.DATE, defaultRepeatedRange);

        eventsPackage.setRepeatedEventMap(repeatedEventMap);
        eventsPackage.setRegularEventMap(regularEventMap);
    }

    public static EventManager getInstance(Context context){
        if (instance == null){
            instance = new EventManager(context);
            instance.loadDB();
        }
        return instance;
    }

    public List<Event> getOrgRepeatedEventList(){
        return this.orgRepeatedEventList;
    }


    public void clearManager(){
        this.instance = null;
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
        //check if special event
//        checkSpecialEvent(event);

        //if should show
        if (event.getShowLevel() == 1){
            //if not repeated
            if (event.getDeleteLevel() == 0) {
                // delete level == 0 means the event is not deleted
                if (event.getRecurrence().length == 0) {
                    Long startTime = event.getStartTime();
                    Long dayBeginMilliseconds = getDayBeginMilliseconds(startTime);

                    if (regularEventMap.containsKey(dayBeginMilliseconds)) {
                        regularEventMap.get(dayBeginMilliseconds).add(event);
                    } else {
                        regularEventMap.put(dayBeginMilliseconds, new ArrayList<ITimeEventInterface>());
                        regularEventMap.get(dayBeginMilliseconds).add(event);
                    }
                } else {
                    if (!isIncludeRepeate(event)){
                        orgRepeatedEventList.add(event);
                        this.addRepeatedEvent(event, nowRepeatedStartAt.getTimeInMillis(), nowRepeatedEndAt.getTimeInMillis());
                    }else{
                        Log.i(TAG, "addEvent: Reapted adding reapte event, dropped.");
                    }

                }
            }
        }
    }

    public Map<String, ArrayList<Event>> getSpecialEventMap(){
        return  this.specialEvent;
    }

    private void checkSpecialEvent(Event event){
        String rEUID = event.getRecurringEventUid();
        if (!rEUID.equals("")){
            if (this.specialEvent.containsKey(rEUID)){
                this.specialEvent.get(rEUID).add(event);
            }
        }
    }

    private boolean isIncludeRepeate(Event repeater){
        for (Event event:this.orgRepeatedEventList
             ) {
            if (event.getEventUid().equals(repeater.getEventUid())){
                return true;
            }
        }

        return false;
    }

//    public void deleteEvent(Event event){
////        if (event.)
//    }

    private synchronized void addRepeatedEvent(Event event, long rangeStart, long rangeEnd){
        RuleModel rule = RuleFactory.getInstance().getRuleModel(event);
        event.setRule(rule);

        ArrayList<Long> repeatedEventsTimes = rule.getOccurenceDates(rangeStart,rangeEnd);
        ArrayList<Event> specialList = null;
        //special event
        if (this.specialEvent.containsKey(event.getEventUid())){
            specialList = this.specialEvent.get(event.getEventUid());
        }

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

            //handle special event
//            if (specialList != null){
//                boolean cancelled = false;
//                for (Event spEvent:specialList
//                     ) {
//                    if (EventUtil.isSameDay(dup_event.getStartTime(), spEvent.getStartTime()) && spEvent.getStatus().equals(Event.STATUS_CANCELLED)){
//                        cancelled = true;
//                        break;
//                    }
//                }
//                if (cancelled){
//                    continue;
//                }
//            }

            //if should show
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

    // repeat event update need to change
    public synchronized void updateEvent(Event oldEvent, Event newEvent){
        long oldBeginTime = this.getDayBeginMilliseconds(oldEvent.getStartTime());

        setCurrentEvent(newEvent);
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
                    //update deleted level
                    //show -> hide
                    if (oldEvent.getShowLevel() == 1 && newEvent.getShowLevel() != 1){
                        //deleted
                    }else{
                        //not deleted
                        this.addEvent(newEvent);
                    }
                }else {
//                    throw new RuntimeException("old event cannot be find in regularEventMap");
                }
            }
        }else{
            //if old is repeated
            this.removeRepeatedEvent(oldEvent);
            this.orgRepeatedEventList.remove(oldEvent);
            this.addEvent(newEvent);
        }
        // here update DB
        Event dbOldEvent = DBManager.getInstance(context).getEvent(oldEvent.getEventUid());
        dbOldEvent.delete();
        DBManager.getInstance(context).insertEvent(newEvent);
    }

    public synchronized void updateDB(List<Event> events){
        List<? extends ITimeEventInterface> orgITimeInterfaces = getAllEvents();
        List<Event> orgEvents = (List<Event>)  orgITimeInterfaces;

        for (Event event:events) {
            Event orgOld = null;

            for (Event orgEvent:orgEvents) {
                if (orgEvent.getEventUid().equals(event.getEventUid())){
                    orgOld = orgEvent;
                    // find event in event manager, and then update
                    updateEvent(orgOld,event);
                    break;
                }
            }

            if (orgOld == null){
                // if cannot find event, then insert it in DB and eventmanager
                DBManager.getInstance(context).insertEvent(event);
                addEvent(event);
            }
        }
    }

    public List<ITimeEventInterface> getAllEvents(){
        ArrayList<ITimeEventInterface> allEvents = new ArrayList<>();
        for (Map.Entry<Long, List<ITimeEventInterface>> entry: regularEventMap.entrySet()){
            allEvents.addAll(entry.getValue());
        }
        allEvents.addAll(orgRepeatedEventList);
        return allEvents;
    }

    public Event findEventByUid(String eventUid){

        Event event = DBManager.getInstance(context).getEvent(eventUid);
        if (event==null){
            return null;
        }
        Long key = getDayBeginMilliseconds(event.getStartTime());
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


    public void loadDB(){
        List<Event> list = DBManager.getInstance(context).getAllEvents();
        for (Event ev: list) {
            if (ev.getShowLevel() == 1){
                addEvent(ev);
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


}
