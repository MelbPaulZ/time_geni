package org.unimelb.itime.bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinchuandong on 22/08/2016.
 */

@Entity(active =  true)
public class Event implements ITimeEventInterface<Event>, Serializable, Cloneable{
    @Id
    private String eventUid;
    private String eventId;
    private String hostUserUid; // add by paul
    private String userUid;
    private String calendarUid;
    private String iCalUID;
    private String recurringEventUid;
    private String recurringEventId;
    @Convert(converter = Event.ArrayOfStringConverter.class, columnType = String.class)
    private String[] recurrence={};
    private String status;
    private String summary;
    private long startTime;
    private long endTime;
    private String description;
    private String location = "";
    private String locationNote;
    private String locationLatitude;
    private String locationLongitude;
    private String eventType;
    private int reminder;
    private int freebusyAccess;
    private String source;
    private int deleteLevel;
    private int icsSequence;
    private int inviteeVisibility;

    private String url;

    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }

    private transient RuleModel rule;

    @Convert(converter = Event.PhotoUrlConverter.class , columnType = String.class)
    private List<PhotoUrl> photo = new ArrayList<>();

    @Convert(converter = Event.TimeslotConverter.class , columnType = String.class)
    private List<Timeslot> timeslot = new ArrayList<>();

    // later delete
    private transient long repeatEndsTime;

    @Convert(converter = Event.InviteeConverter.class, columnType = String.class)
    private List<Invitee> invitee = new ArrayList<>();
    /** Used for active entity operations. */
    @Generated(hash = 1542254534)
    private transient EventDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Event() {
//        invitee = new ArrayList<>();
//        timeslot = new ArrayList<>();
    }


    @Generated(hash = 295629456)
    public Event(String eventUid, String eventId, String hostUserUid, String userUid,
            String calendarUid, String iCalUID, String recurringEventUid, String recurringEventId,
            String[] recurrence, String status, String summary, long startTime, long endTime,
            String description, String location, String locationNote, String locationLatitude,
            String locationLongitude, String eventType, int reminder, int freebusyAccess,
            String source, int deleteLevel, int icsSequence, int inviteeVisibility, String url,
            List<PhotoUrl> photo, List<Timeslot> timeslot, List<Invitee> invitee) {
        this.eventUid = eventUid;
        this.eventId = eventId;
        this.hostUserUid = hostUserUid;
        this.userUid = userUid;
        this.calendarUid = calendarUid;
        this.iCalUID = iCalUID;
        this.recurringEventUid = recurringEventUid;
        this.recurringEventId = recurringEventId;
        this.recurrence = recurrence;
        this.status = status;
        this.summary = summary;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
        this.locationNote = locationNote;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.eventType = eventType;
        this.reminder = reminder;
        this.freebusyAccess = freebusyAccess;
        this.source = source;
        this.deleteLevel = deleteLevel;
        this.icsSequence = icsSequence;
        this.inviteeVisibility = inviteeVisibility;
        this.url = url;
        this.photo = photo;
        this.timeslot = timeslot;
        this.invitee = invitee;
    }


    @Override
    public void setTitle(String summary) {
        this.summary = summary;
    }

    @Override
    public String getTitle() {
        return this.summary;
    }

    @Override
    public List<? extends ITimeInviteeInterface> getDisplayInvitee() {
        if (this.invitee==null){
            this.invitee = this.getInvitee();
        }
        return this.invitee;
    }

    public void addInvitee(Invitee invitee){
        this.invitee.add(invitee);
    }

    public void setEventId(String id){ this.eventId = id;}

    public void setStartTime(long startTime){ this.startTime = startTime; }

    public void setEndTime(long endTime){ this.endTime = endTime; }

    public String getEventUid(){ return eventUid; }

    public long getStartTime(){return startTime;}

    public long getEndTime(){return endTime;}


    @Override
    public int getDisplayEventType() {
        return EventUtil.parseEventType(this.eventType);
    }


    @Override
    public int getDisplayStatus() {
        return EventUtil.parseEventStatus(this.status);
    }

    public String getEventType(){
        return this.eventType;
    }


    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getDuration(){
        return (int)((endTime - startTime) /(1000*60));
    }

    public long getDurationMilliseconds(){
        return (endTime - startTime);
    }

    @Override
    public int compareTo(Event event) {
        long selfStartTime = this.getStartTime();
        long cmpTgtStartTime = event.getStartTime();
        int result = selfStartTime < cmpTgtStartTime ? -1 : 1;

        if (result == -1){
            return result;
        }else {
            return selfStartTime == cmpTgtStartTime ? 0 : 1;
        }
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    @Override
    public String getLocation() {
        return location;
    }



    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    public void setInvitee(List<Invitee> invitee) {
        this.invitee = invitee;
    }


    public String getUrl() {
        return this.url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getSummary() {
        return this.summary;
    }


    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String[] getRecurrence() {
        return this.recurrence;
    }


    public void setRecurrence(String[] recurrence) {
        this.recurrence = recurrence;
    }


    public String getICalUID() {
        return this.iCalUID;
    }


    public void setICalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }


    public String getCalendarUid() {
        return this.calendarUid;
    }


    public void setCalendarUid(String calendarUid) {
        this.calendarUid = calendarUid;
    }


    public String getRecurringEventId() {
        return this.recurringEventId;
    }


    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }


    public String getRecurringEventUid() {
        return this.recurringEventUid;
    }


    public void setRecurringEventUid(String recurringEventUid) {
        this.recurringEventUid = recurringEventUid;
    }


    public String getEventId() {
        return this.eventId;
    }

    public String getiCalUID() {
        return iCalUID;
    }

    public void setiCalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }



    public String getLocationNote() {
        return locationNote;
    }

    public void setLocationNote(String locationNote) {
        this.locationNote = locationNote;
    }



    public long getRepeatEndsTime() {
        return repeatEndsTime;
    }

    public void setRepeatEndsTime(long repeatEndsTime) {
        this.repeatEndsTime = repeatEndsTime;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    public List<Timeslot> getTimeslot() {
        return timeslot;
    }





    public void setTimeslot(List<Timeslot> timeslot) {
        this.timeslot = timeslot;
    }





    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTimeslots() {
        timeslot = null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasAttendee(){
        return invitee!=null;
    }

    public boolean hasTimeslots(){
        return timeslot !=null;
    }


    public String getHostUserUid() {
        return hostUserUid;
    }

    public void setHostUserUid(String hostUserUid) {
        this.hostUserUid = hostUserUid;
    }





    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }


    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public int getFreebusyAccess() {
        return freebusyAccess;
    }

    public void setFreebusyAccess(int freebusyAccess) {
        this.freebusyAccess = freebusyAccess;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }

    public int getIcsSequence() {
        return icsSequence;
    }

    public void setIcsSequence(int icsSequence) {
        this.icsSequence = icsSequence;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Event event = null;
        try
        {
            event = (Event) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return event;
    }

    public List<PhotoUrl> getPhoto(){
        return this.photo;
    }


    public void setPhoto(List<PhotoUrl> photoUrls){
        this.photo = photoUrls;
    }

    public boolean hasPhoto(){
        return photo!=null && photo.size()>0;
    }


    public boolean hasRecurrence(){
        return this.recurrence!=null;
    }







    public List<Invitee> getInvitee() {
        return invitee;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1459865304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
    }

    public int getInviteeVisibility() {
        return inviteeVisibility;
    }

    public void setInviteeVisibility(int inviteeVisibility) {
        this.inviteeVisibility = inviteeVisibility;
    }


    public static class ArrayOfStringConverter implements PropertyConverter<String[], String>{
        Gson gson = new Gson();
        @Override
        public String[] convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<String[]>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(String[] entityProperty) {
            return  gson.toJson(entityProperty);
        }
    }

    public static class TimeslotConverter implements PropertyConverter<List<Timeslot> , String>{
        Gson gson = new Gson();
        @Override
        public List<Timeslot> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<Timeslot>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<Timeslot> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class PhotoUrlConverter implements PropertyConverter<List<PhotoUrl> , String>{
        Gson gson = new Gson();
        @Override
        public List<PhotoUrl> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<PhotoUrl>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<PhotoUrl> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public static class InviteeConverter implements PropertyConverter<List<Invitee> , String>{
        Gson gson = new Gson();
        @Override
        public List<Invitee> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<Invitee>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<Invitee> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }
}
