package org.unimelb.itime.managers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.messageevent.MessageEventRefresh;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
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

    private List<ITimeEventInterface> allDayEventList = new ArrayList<>();
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

    private Context context;

    public EventManager(Context context){
        this.context = context;
        nowRepeatedStartAt = EventUtil.getBeginOfDayCalendar(nowRepeatedStartAt);
        nowRepeatedEndAt = EventUtil.getBeginOfDayCalendar(nowRepeatedEndAt);

        nowRepeatedStartAt.add(Calendar.DATE, -defaultRepeatedRange);
        nowRepeatedEndAt.add(Calendar.DATE, defaultRepeatedRange);

        eventsPackage.setRepeatedEventMap(repeatedEventMap);
        eventsPackage.setRegularEventMap(regularEventMap);
        eventsPackage.setAllDayEventList(allDayEventList);
    }

    public static EventManager getInstance(Context context){
        if (instance == null){
            instance = new EventManager(context);
            instance.loadDB();
        }
        return instance;
    }

    public EventsPackage getEventsPackage(){
        return eventsPackage;
    }

    public List<Event> getOrgRepeatedEventList(){
        return this.orgRepeatedEventList;
    }

    public Map<String, ArrayList<Event>> getSpecialEventMap(){
        return  this.specialEvent;
    }

    public List<ITimeEventInterface> getAllEvents(){
        ArrayList<ITimeEventInterface> allEvents = new ArrayList<>();
        for (Map.Entry<Long, List<ITimeEventInterface>> entry: regularEventMap.entrySet()){
            allEvents.addAll(entry.getValue());
        }
        allEvents.addAll(orgRepeatedEventList);
        allEvents.addAll(allDayEventList);

        return allEvents;
    }

    public Event findEventByUid(String eventUid){

        Event event = DBManager.getInstance(context).getEvent(eventUid);
        if (event==null){
            return null;
        }

        Long key = EventUtil.getDayBeginMilliseconds(event.getStartTime());

        if (EventUtil.isAllDayEvent(event)){
            List<Event> allDayEventList = new ArrayList(this.allDayEventList);

           return EventUtil.getItemInList(allDayEventList, event);
        }

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
                    if ((iTimeEventInterface).getEventUid().equals(eventUid)) {
                        return (Event) iTimeEventInterface;
                    }
                }
            }
        }
        return null;
    }

    public void syncRepeatedEvent(long currentDate){
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

    public void loadDB(){
        List<Event> list = DBManager.getInstance(context).getAllEvents();
        for (Event ev: list) {
            if (ev.getShowLevel() > 0){
                addEvent(ev);
            }
        }
    }

    public void clearManager(){
        this.instance = null;
    }

    public void addEvent(Event event){
        //check if special event
        handleSpecialEvent(event);

        //if should show
        if (event.getShowLevel() <= 0) {
            return;
        }

        if (EventUtil.isAllDayEvent(event)){
            allDayEventList.add(event);
            return;
        }

        if (event.getRecurrence().length == 0) {
            Long startTime = event.getStartTime();
            Long dayBeginMilliseconds = EventUtil.getDayBeginMilliseconds(startTime);

            if (regularEventMap.containsKey(dayBeginMilliseconds)) {
                regularEventMap.get(dayBeginMilliseconds).add(event);
            } else {
                regularEventMap.put(dayBeginMilliseconds, new ArrayList<ITimeEventInterface>());
                regularEventMap.get(dayBeginMilliseconds).add(event);
            }
        } else {
            if (!isIncludeRepeated(event)){
                orgRepeatedEventList.add(event);
                this.addRepeatedEvent(event, nowRepeatedStartAt.getTimeInMillis(), nowRepeatedEndAt.getTimeInMillis());
            }else{
                Log.i(TAG, "addEvent: Reapted adding reapte event, dropped.");
            }

        }
    }

    // repeat event update need to change
    public synchronized void updateEvent(Event oldEvent, Event newEvent){
        setCurrentEvent(newEvent);

        if (EventUtil.isAllDayEvent(oldEvent)){
            updateAllDayEvent(oldEvent, newEvent);
            return;
        }

        //if old not repeated
        if (oldEvent.getRecurrence().length == 0){
            this.updateRegularEvent(oldEvent, newEvent);
        }else{
            //if old is repeated
           this.updateRepeatedEvent(oldEvent, newEvent);
        }

        // here update DB
        Event dbOldEvent = DBManager.getInstance(context).getEvent(oldEvent.getEventUid());
        dbOldEvent.delete();
        DBManager.getInstance(context).insertEvent(newEvent);
    }

    private void updateRegularEvent(Event oldEvent, Event newEvent){
        long oldBeginTime = EventUtil.getDayBeginMilliseconds(oldEvent.getStartTime());

        if (this.regularEventMap.containsKey(oldBeginTime)){
            Event old = null;

            for (ITimeEventInterface iTimeEventInterface : regularEventMap.get(oldBeginTime)){
                if ((iTimeEventInterface).getEventUid().equals(oldEvent.getEventUid())){
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
    }

    private void updateRepeatedEvent(Event oldEvent, Event newEvent){
        this.removeRepeatedEvent(oldEvent);
        this.orgRepeatedEventList.remove(oldEvent);
        this.addEvent(newEvent);
    }

    private void updateAllDayEvent(Event oldEvent, Event newEvent){
        this.allDayEventList.remove(EventUtil.getItemInList(new ArrayList(this.allDayEventList), oldEvent));
        this.addEvent(newEvent);
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


    private void handleSpecialEvent(Event event){
        String rEUID = event.getRecurringEventUid();

        if (!rEUID.equals("") && !rEUID.equals(event.getEventUid())){
            if (this.specialEvent.containsKey(rEUID)){
                ArrayList<Event> specialEvents = this.specialEvent.get(rEUID);
                EventUtil.removeWhileLooping(specialEvents,event);
                this.specialEvent.get(rEUID).add(event);
            }else{
                ArrayList<Event> specialList = new ArrayList<>();
                specialList.add(event);
                this.specialEvent.put(rEUID, specialList);
            }

            Event org = findOrgByUUID(event.getRecurringEventUid());
            if (org != null){
                refreshRepeatedEvent(org);
            }
        }
    }

    private boolean isIncludeRepeated(Event repeater){
        for (Event event:this.orgRepeatedEventList
             ) {
            if (event.getEventUid().equals(repeater.getEventUid())){
                return true;
            }
        }

        return false;
    }

    private void addRepeatedEvent(Event event, long rangeStart, long rangeEnd){
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
            dup_event = event.clone();

            if (dup_event == null){
                throw new RuntimeException("Clone error");
            }

            //dup time is right
            long duration = dup_event.getDurationMilliseconds();
            dup_event.setStartTime(time);
            dup_event.setEndTime(time + duration);

            //handle special event
            if (specialList != null){
                boolean skip = false;
                for (Event spEvent:specialList
                     ) {
                    if (EventUtil.isSameDay(dup_event.getStartTime(), spEvent.getStartTime())){
                        skip = true;
                        break;
                    }
                }
                if (skip){
                    continue;
                }
            }

            //if should show
            Long startTime = dup_event.getStartTime();
            Long dayBeginMilliseconds = EventUtil.getDayBeginMilliseconds(startTime);

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

    private void removeRepeatedEvent(Event event){
        List<EventTracer> tracers = uidTracerMap.get(event.getEventUid());
        if (tracers != null){
            for (EventTracer tracer:tracers
                    ) {
                tracer.removeSelfFromRepeatedEventMap();
            }
            uidTracerMap.remove(event.getEventUid());
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

    private Event findOrgByUUID(String UUID){
        for (Event orgE:orgRepeatedEventList
                ) {
            if (orgE.getEventUid().equals(UUID)){
                return orgE;
            }
        }
        return null;
    }

    /**
     * Paul Paul, your finding Repeated Events
     * @param dayStartTime
     * @param UUID
     * @return
     */
    public Event findRepeatedByUUUID(long dayStartTime, String UUID){
        if (repeatedEventMap.containsKey(dayStartTime)){
            List<ITimeEventInterface> repeats = repeatedEventMap.get(dayStartTime);
            for (ITimeEventInterface event:repeats
                 ) {
                if (event.getEventUid().equals(UUID)){
                    return (Event) event;
                }
            }
        }else {
            Log.i(TAG, "findRepeatedByUUUID:  Repeated Event not belong to input day");
        }

        return null;
    }

    private void refreshRepeatedEvent(Event event){
        this.removeRepeatedEvent(event);
        this.orgRepeatedEventList.remove(event);
        this.addEvent(event);
    }

    private class EventsPackage implements ITimeEventPackageInterface {
        private List<ITimeEventInterface> allDayEventList;
        private Map<Long, List<ITimeEventInterface>> regularEventMap;
        private Map<Long, List<ITimeEventInterface>> repeatedEventMap;

        void setAllDayEventList(List<ITimeEventInterface> allDayEventList) {
            this.allDayEventList = allDayEventList;
        }

        void setRegularEventMap(Map<Long, List<ITimeEventInterface>> regularEventMap) {
            this.regularEventMap = regularEventMap;
        }

        void setRepeatedEventMap(Map<Long, List<ITimeEventInterface>> repeatedEventMap) {
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

        @Override
        public List<ITimeEventInterface> getAllDayEvents() {
            return allDayEventList;
        }
    }

    private class EventTracer{
        private Map<Long, List<ITimeEventInterface>> repeatedEventMap;
        private ITimeEventInterface event;
        private long belongToDayOfBegin;

        EventTracer(Map<Long, List<ITimeEventInterface>> repeatedEventMap
                , ITimeEventInterface event,long belongToDayOfBegin){
            this.repeatedEventMap = repeatedEventMap;
            this.event = event;
            this.belongToDayOfBegin = belongToDayOfBegin;
        }

        void removeSelfFromRepeatedEventMap(){
            repeatedEventMap.get(belongToDayOfBegin).remove(event);
        }
    }


    /********************************** Paul Paul 改 *********************************************/

    // paul paul 改！
    public void initNewEvent(Calendar startTimeCalendar){
        // initial default values for new event
        Event event = new Event();
        setCurrentEvent(event);
        event.setEventUid(AppUtil.generateUuid());
        event.setHostUserUid(UserUtil.getInstance(context).getUserUid());
        long endTime = startTimeCalendar.getTimeInMillis() + 3600 * 1000;
        event.setStartTime(startTimeCalendar.getTimeInMillis());
        event.setEndTime(endTime);
        setCurrentEvent(event);
    }

    // paul paul 改！
    @Deprecated
    public Event copyCurrentEvent(Event event){
        Gson gson = new Gson();

        String eventStr = gson.toJson(event);
        Event copyEvent = gson.fromJson(eventStr, Event.class);

        Type dataType = new TypeToken <RuleModel<Event>>() {}.getType();
        RuleModel response = gson.fromJson(gson.toJson(event.getRule(), dataType), dataType);
        copyEvent.setRule(response);

        return copyEvent;
    }


    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
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

}
