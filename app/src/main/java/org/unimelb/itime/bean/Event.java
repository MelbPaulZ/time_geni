package org.unimelb.itime.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

//import com.android.databinding.library.baseAdapters.BR;

//import org.unimelb.itime.BR;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Dictionary;
import java.util.HashMap;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.dao.InviteeDao;
import org.unimelb.itime.dao.EventPhotoDao;
import org.unimelb.itime.dao.TimeSlotDao;

/**
 * Created by Paul on 23/08/2016.
 */
@Entity()
public class Event extends BaseObservable implements ITimeEventInterface,Serializable {
    @Id()
    private String eventId;
    private String eventTitle;
    private String eventNote="";
    private String currentLocationAddress;
    private String currentLocationNote;
    private double currentLocationLatitude;
    private double currentLocationLongitude;
    private int currentRepeatTypeId;
    private String userId;
    private int alerTimeId;
    private int eventTypeId;
    private int visibilityTypeId;
    private int eventSourceId;
    private String calendarTypedId="";
    private Boolean isInfiniteRepeat;
    private Boolean isDeleted;
    private long repeatEndsTime;
    private String hostEventId;
    private int userStatusId;
    @ToMany(referencedJoinProperty = "id")
    private List<Invitee> invitee = new ArrayList<>(); // need to be checked
    @ToMany(referencedJoinProperty = "photoUid")
    private List<EventPhoto> eventPhotos = new ArrayList<>();
    private String url = "";
    private int duration;
    private long startTime;
    private long endTime;
    private boolean isHost; // can be delete later, only for test
//    attendee repeat?
    @ToMany(referencedJoinProperty = "timeSlotUid")
    private List<TimeSlot> timeslots = new ArrayList<>();
    /** Used for active entity operations. */
    @Generated(hash = 1542254534)
    private transient EventDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Event() {

    }

    public Event(String eventId,
                 String eventTitle,
                 String eventNote,
                 String currentLocationAddress,
                 String currentLocationNote,
                 double currentLocationLatitude,
                 double currentLocationLongitude,
                 int currentRepeatTypeId,
                 String userId,
                 int alerTimeId,
                 int eventTypeId,
                 int visibilityTypeId,
                 int eventSourceId,
                 String calendarTypedId,
                 Boolean isInfiniteRepeat,
                 Boolean isDeleted,
                 long repeatEndsTime,
                 String hostEventId,
                 int userStatusId,
                 List<Invitee> invitee,
                 List<EventPhoto> eventPhotos,
                 String url,
                 int duration,
                 List<TimeSlot> timeslots) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventNote = eventNote;
        this.currentLocationAddress = currentLocationAddress;
        this.currentLocationNote = currentLocationNote;
        this.currentLocationLatitude = currentLocationLatitude;
        this.currentLocationLongitude = currentLocationLongitude;
        this.currentRepeatTypeId = currentRepeatTypeId;
        this.userId = userId;
        this.alerTimeId = alerTimeId;
        this.eventTypeId = eventTypeId;
        this.visibilityTypeId = visibilityTypeId;
        this.eventSourceId = eventSourceId;
        this.calendarTypedId = calendarTypedId;
        this.isInfiniteRepeat = isInfiniteRepeat;
        this.isDeleted = isDeleted;
        this.repeatEndsTime = repeatEndsTime;
        this.hostEventId = hostEventId;
        this.userStatusId = userStatusId;
        this.invitee = invitee;
        this.eventPhotos = eventPhotos;
        this.url = url;
        this.duration = duration;
        this.timeslots = timeslots;
    }

    @Generated(hash = 438601432)
    public Event(String eventId, String eventTitle, String eventNote,
            String currentLocationAddress, String currentLocationNote,
            double currentLocationLatitude, double currentLocationLongitude,
            int currentRepeatTypeId, String userId, int alerTimeId, int eventTypeId,
            int visibilityTypeId, int eventSourceId, String calendarTypedId,
            Boolean isInfiniteRepeat, Boolean isDeleted, long repeatEndsTime,
            String hostEventId, int userStatusId, String url, int duration, long startTime,
            long endTime, boolean isHost) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventNote = eventNote;
        this.currentLocationAddress = currentLocationAddress;
        this.currentLocationNote = currentLocationNote;
        this.currentLocationLatitude = currentLocationLatitude;
        this.currentLocationLongitude = currentLocationLongitude;
        this.currentRepeatTypeId = currentRepeatTypeId;
        this.userId = userId;
        this.alerTimeId = alerTimeId;
        this.eventTypeId = eventTypeId;
        this.visibilityTypeId = visibilityTypeId;
        this.eventSourceId = eventSourceId;
        this.calendarTypedId = calendarTypedId;
        this.isInfiniteRepeat = isInfiniteRepeat;
        this.isDeleted = isDeleted;
        this.repeatEndsTime = repeatEndsTime;
        this.hostEventId = hostEventId;
        this.userStatusId = userStatusId;
        this.url = url;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isHost = isHost;
    }

    public boolean hasEventTitle(){
        return eventTitle!=null;
    }

    public boolean hasEventId(){
        return eventId!=null;
    }

    public boolean hasEventLocationAddress(){
        return currentLocationAddress!=null;
    }

    public boolean hasEventNote(){
        return eventNote!=null;
    }

    public boolean hasCalendarTypedId(){return calendarTypedId!=null;}

    public boolean hasHostEventId(){return hostEventId!=null;}

    public boolean hasAttendee(){ return invitee !=null;}

    public boolean hasEventPhotos(){ return eventPhotos!=null;}

    public boolean hasUrl(){return url!=null;}

    public boolean hasTimeslots(){return timeslots!=null;}

    public void setEventPhotos(List<EventPhoto> eventPhotos){
        this.eventPhotos = eventPhotos;
    }

    @Keep
    public List<EventPhoto> getEventPhotos(){
        return eventPhotos;
    }



    public String getEventId() {
        return eventId;
    }

    @Override
    public void setTitle(String s) {
        eventTitle = s;
    }

    @Override
    public String getTitle() {
        return eventTitle;
    }

    @Override
    public void setStartTime(long l) {
        this.startTime = l;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public void setEndTime(long l) {
        this.endTime = l;
    }

    @Override
    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public void setEventType(int i) {
        this.eventTypeId = i;
    }

    @Override
    public int getEventType() {
        return eventTypeId;
    }

    @Override
    public void setStatus(int i) {
        this.userStatusId = i;
    }

    @Override
    public int getStatus() {
        return userStatusId;
    }

    @Override
    public void setLocation(String s) {

    }

    @Override
    public String getLocation() {
        return null;
    }

    @Override
    public List<ITimeTimeSlotInterface> getTimeSlots() {
        return null;
    }

    @Override
    public void setTimeSlots(List list) {
        this.timeslots = list;
    }


    public void setNote(String s) {
        eventNote = s;
    }


    public String getNote() {
        return eventNote;
    }


    public void setLocationAddress(String s) {
        currentLocationAddress = s;
    }


    public String getLocationAddress() {
        return currentLocationAddress;
    }


    public void setLocationNote(String s) {
        currentLocationNote = s;
    }


    public String getLocationNote() {
        return currentLocationNote;
    }


    public void setLocationLatitude(double v) {
        currentLocationLatitude = v;
    }


    public Double getLocationLatitude() {
        return currentLocationLatitude;
    }


    public void setLocationLongitude(double v) {
        currentLocationLongitude = v;
    }


    public Double getLocationLongitude() {
        return currentLocationLongitude;
    }


    public void setRepeatTypeId(int i) {
        currentRepeatTypeId = i;
    }


    public int getRepeatTypeId() {
        return currentRepeatTypeId;
    }


    public void setUserId(String s) {
        userId = s;
    }


    public String getUserId() {
        return userId;
    }


    public void setAlertTimeId(int i) {
        alerTimeId = i;
    }


    public int getAlertTimeId() {
        return alerTimeId;
    }



    public void setVisibilityTypeId(int i) {
        visibilityTypeId = i;
    }


    public int getVisibilityTypeId() {
        return visibilityTypeId;
    }


    public void setEventSourceId(int i) {
        eventSourceId = i;
    }


    public int getEventSourceId() {
        return eventSourceId;
    }


    public void setCalendarTypeId(String s) {
        calendarTypedId = s;
    }


    public String getCalendarTypeId() {
        return calendarTypedId;
    }


    public void setIsInfiniteRepeat(Boolean aBoolean) {
        isInfiniteRepeat = aBoolean;
    }


    public Boolean getIsInfiniteRepeat() {
        return isInfiniteRepeat;
    }


    public void setIsDeleted(Boolean aBoolean) {
        isDeleted = aBoolean;
    }


    public Boolean getIsDeleted() {
        return isDeleted;
    }


    public void setRepeatEndsTime(long integer) {
        repeatEndsTime = integer;
    }


    public long getRepeatEndsTime() {
        return repeatEndsTime;
    }


    public void setHostEventId(String s) {
        hostEventId = s;
    }


    public String getHostEventId() {
        return hostEventId;
    }


    public void setUserStatusId(int i) {
        userStatusId = i;
    }


    public int getUserStatusId() {
        return userStatusId;
    }





    public void setUrl(String s) {
        url = s;
    }


    public String getUrl() {
        return url;
    }


    public void setDuration(int i) {
        duration = i;
    }


    public int getDuration() {
        int temp = (int) ((endTime - startTime)/1000/60);
        return (int)((float)(endTime - startTime)/1000/60);
    }


    public void setInvitee(List<Invitee> arrayList) {
        invitee = arrayList;
    }


    @Keep
    public List<Invitee> getInvitee() {
        return invitee;
    }




    @Override
    public List<? extends ITimeInviteeInterface> getDisplayInvitee() {
        return null;
    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 481094641)
    public synchronized void resetTimeslots() {
        timeslots = null;
    }

    @Keep
    public List<TimeSlot> getTimeslots() {
        if (timeslots == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TimeSlotDao targetDao = daoSession.getTimeSlotDao();
            List<TimeSlot> timeslotsNew = targetDao._queryEvent_Timeslots(Integer.parseInt(eventId));
            synchronized (this) {
                if(timeslots == null) {
                    timeslots = timeslotsNew;
                }
            }
        }
        return timeslots;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2059293627)
    public synchronized void resetEventPhotos() {
        eventPhotos = null;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 777091542)
    public synchronized void resetInvitee() {
        invitee = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1459865304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
    }

    public boolean getIsHost() {
        return this.isHost;
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }

    public String getCalendarTypedId() {
        return this.calendarTypedId;
    }

    public void setCalendarTypedId(String calendarTypedId) {
        this.calendarTypedId = calendarTypedId;
    }

    public int getEventTypeId() {
        return this.eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public int getAlerTimeId() {
        return this.alerTimeId;
    }

    public void setAlerTimeId(int alerTimeId) {
        this.alerTimeId = alerTimeId;
    }

    public int getCurrentRepeatTypeId() {
        return this.currentRepeatTypeId;
    }

    public void setCurrentRepeatTypeId(int currentRepeatTypeId) {
        this.currentRepeatTypeId = currentRepeatTypeId;
    }

    public double getCurrentLocationLongitude() {
        return this.currentLocationLongitude;
    }

    public void setCurrentLocationLongitude(double currentLocationLongitude) {
        this.currentLocationLongitude = currentLocationLongitude;
    }

    public double getCurrentLocationLatitude() {
        return this.currentLocationLatitude;
    }

    public void setCurrentLocationLatitude(double currentLocationLatitude) {
        this.currentLocationLatitude = currentLocationLatitude;
    }

    public String getCurrentLocationNote() {
        return this.currentLocationNote;
    }

    public void setCurrentLocationNote(String currentLocationNote) {
        this.currentLocationNote = currentLocationNote;
    }

    public String getCurrentLocationAddress() {
        return this.currentLocationAddress;
    }

    public void setCurrentLocationAddress(String currentLocationAddress) {
        this.currentLocationAddress = currentLocationAddress;
    }

    public String getEventNote() {
        return this.eventNote;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public String getEventTitle() {
        return this.eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }



}
