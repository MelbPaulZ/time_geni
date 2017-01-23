package org.unimelb.itime.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleInterface;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinchuandong on 22/08/2016.
 */

@Entity(active =  true)
public class Event implements ITimeEventInterface<Event>, Serializable, Cloneable, Parcelable, RuleInterface, ITimeComparable<Event>{
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_UPDATING = "updating";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_CANCELLED = "cancelled";

    public static final String TYPE_GROUP = "group";
    public static final String TYPE_SOLO = "solo";

    @Id
    private String eventUid = "";
    private String eventId = "";
    private String hostUserUid = ""; // add by paul
    private String userUid = "";
    private String calendarUid = "";
    private String iCalUID = "";
    private String recurringEventUid = "";
    private String recurringEventId = "";
    @Convert(converter = Event.ArrayOfStringConverter.class, columnType = String.class)
    private String[] recurrence={};
    private String status = "";
    private String summary = "";
    private long startTime;
    private long endTime;
    private int confirmedCount;
    private int showLevel;
    private String description = "";
    private String location = "";
    private String locationNote = "";
    private String locationLatitude = "";
    private String locationLongitude = "";
    private String eventType = "";
    private int reminder;
    private int freebusyAccess;
    private String source = "";
    private int deleteLevel;
    private int icsSequence;
    private int inviteeVisibility = 1;
    private String display = "";

    private String url = "";

    // to save message in event, need to convert the message to string
    @Convert(converter = Event.MessageConverter.class, columnType = String.class)
    private Message message;

    // later delete
    private transient long repeatEndsTime;

    //for vendor
    private transient boolean highlighted;


    @Convert(converter = Event.InviteeConverter.class, columnType = String.class)
    private List<Invitee> invitee = new ArrayList<>();

    @Expose(serialize = true, deserialize = true)
    private transient RuleModel rule = new RuleModel(this);

    @Convert(converter = Event.PhotoUrlConverter.class , columnType = String.class)
    private List<PhotoUrl> photo = new ArrayList<>();

    @Convert(converter = Event.TimeslotConverter.class , columnType = String.class)
    private List<Timeslot> timeslot = new ArrayList<>();

    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1542254534)
    private transient EventDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Event() {

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

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
    }

    public String getEventUid(){ return eventUid; }

    public long getStartTime(){return startTime;}

    public long getEndTime(){return endTime;}


    @Override
    public int getDisplayEventType() {
        return EventUtil.parseEventType(this.eventType);
    }


    @Override
    public String getDisplayStatus() {
//        return EventUtil.parseEventStatus(this.status);
        return this.display;
    }

    public int getParsedStatus(){
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

    // this duration is in minute as unit
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
    public Event clone() {
        Event event = null;
        try
        {
            event = (Event) super.clone();
            RuleModel ruleModel = new RuleModel();
            event.setRule(ruleModel);
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
        return this.recurrence!=null && this.recurrence.length>0;
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

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getShowLevel() {
        return this.showLevel;
    }

    public void setShowLevel(int showLevel) {
        this.showLevel = showLevel;
    }

    public int getConfirmedCount() {
        return this.confirmedCount;
    }

    public void setConfirmedCount(int confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    @Override
    public boolean iTimeEquals(Event obj2) {
        return this.getEventUid().equals(obj2.getEventUid());
    }


    public static class ArrayOfStringConverter implements PropertyConverter<String[], String>{
        Gson gson = new Gson();
        @Override
        public String[] convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<String[]>() {}.getType();
            String[] rst =  gson.fromJson(databaseValue, listType);

            return rst;
        }

        @Override
        public String convertToDatabaseValue(String[] entityProperty) {
            return  gson.toJson(entityProperty);
        }
    }

    public static class MessageConverter implements PropertyConverter<Message, String>{
        Gson gson = new Gson();

        @Override
        public Message convertToEntityProperty(String databaseValue) {
            return gson.fromJson(databaseValue, Message.class);
        }

        @Override
        public String convertToDatabaseValue(Message entityProperty) {
            return gson.toJson(entityProperty);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventUid);
        dest.writeString(this.eventId);
        dest.writeString(this.hostUserUid);
        dest.writeString(this.userUid);
        dest.writeString(this.calendarUid);
        dest.writeString(this.iCalUID);
        dest.writeString(this.recurringEventUid);
        dest.writeString(this.recurringEventId);
        dest.writeStringArray(this.recurrence);
        dest.writeString(this.status);
        dest.writeString(this.summary);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeInt(this.confirmedCount);
        dest.writeInt(this.showLevel);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeString(this.locationNote);
        dest.writeString(this.locationLatitude);
        dest.writeString(this.locationLongitude);
        dest.writeString(this.eventType);
        dest.writeInt(this.reminder);
        dest.writeInt(this.freebusyAccess);
        dest.writeString(this.source);
        dest.writeInt(this.deleteLevel);
        dest.writeInt(this.icsSequence);
        dest.writeInt(this.inviteeVisibility);
        dest.writeString(this.display);
        dest.writeString(this.url);
        dest.writeList(this.invitee);
        dest.writeList(this.photo);
        dest.writeList(this.timeslot);
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    protected Event(Parcel in) {
        this.eventUid = in.readString();
        this.eventId = in.readString();
        this.hostUserUid = in.readString();
        this.userUid = in.readString();
        this.calendarUid = in.readString();
        this.iCalUID = in.readString();
        this.recurringEventUid = in.readString();
        this.recurringEventId = in.readString();
        this.recurrence = in.createStringArray();
        this.status = in.readString();
        this.summary = in.readString();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.confirmedCount = in.readInt();
        this.showLevel = in.readInt();
        this.description = in.readString();
        this.location = in.readString();
        this.locationNote = in.readString();
        this.locationLatitude = in.readString();
        this.locationLongitude = in.readString();
        this.eventType = in.readString();
        this.reminder = in.readInt();
        this.freebusyAccess = in.readInt();
        this.source = in.readString();
        this.deleteLevel = in.readInt();
        this.icsSequence = in.readInt();
        this.inviteeVisibility = in.readInt();
        this.display = in.readString();
        this.url = in.readString();
        this.invitee = new ArrayList<Invitee>();
        in.readList(this.invitee, Invitee.class.getClassLoader());
        this.photo = new ArrayList<PhotoUrl>();
        in.readList(this.photo, PhotoUrl.class.getClassLoader());
        this.timeslot = new ArrayList<Timeslot>();
        in.readList(this.timeslot, Timeslot.class.getClassLoader());
    }


    @Keep
    public Event(String eventUid, String eventId, String hostUserUid, String userUid, String calendarUid, String iCalUID,
            String recurringEventUid, String recurringEventId, String[] recurrence, String status, String summary, long startTime,
            long endTime, int confirmedCount, int showLevel, String description, String location, String locationNote,
            String locationLatitude, String locationLongitude, String eventType, int reminder, int freebusyAccess, String source,
            int deleteLevel, int icsSequence, int inviteeVisibility, String display, String url, Message message,
            List<Invitee> invitee, List<PhotoUrl> photo, List<Timeslot> timeslot) {
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
        this.confirmedCount = confirmedCount;
        this.showLevel = showLevel;
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
        this.display = display;
        this.url = url;
        this.message = message;
        this.invitee = invitee;
        this.photo = photo;
        this.timeslot = timeslot;

        //
        setRule(RuleFactory.getInstance().getRuleModel(this));
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public boolean isHighlighted() {
        return highlighted;
    }

    @Override
    public void setHighLighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
