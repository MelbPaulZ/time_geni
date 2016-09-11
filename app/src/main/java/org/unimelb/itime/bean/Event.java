package org.unimelb.itime.bean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.dao.InviteeDao;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.io.Serializable;
import java.util.List;
import org.unimelb.itime.dao.TimeSlotDao;

/**
 * Created by yinchuandong on 22/08/2016.
 */

@Entity
public class Event implements ITimeEventInterface<Event>, Serializable {
    @Id
    private String eventUid;
    // for other calendars
    private String eventId;
    private String recurringEventUid;
    // for other calendars
    private String recurringEventId;
    private String calendarUid;
    private String iCalUID;
    private String hostUserUid; // add by paul
    private String recurrence;
    private String summary;
    private String url;
    private String location;
    private String locationNote;
    private double locationLatitude;
    private double locationLongitude;
    private String note;

    @ToMany(referencedJoinProperty = "eventUid")
    private List<TimeSlot> timeslots = null;

    // later delete
    private transient long repeatEndsTime;
    private transient boolean isHost;

    @Property
    @NotNull
    private long startTime;
    @Property
    @NotNull
    private long endTime;
    @Property
    @NotNull
    private int eventType;
    @Property
    @NotNull
    private int status;

    @ToMany(referencedJoinProperty = "eventUid")
    private List<Invitee> invitee = null;
    /** Used for active entity operations. */
    @Generated(hash = 1542254534)
    private transient EventDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Event() {
    }







    @Generated(hash = 902139853)
    public Event(String eventUid, String eventId, String recurringEventUid, String recurringEventId,
            String calendarUid, String iCalUID, String hostUserUid, String recurrence, String summary,
            String url, String location, String locationNote, double locationLatitude,
            double locationLongitude, String note, long startTime, long endTime, int eventType,
            int status) {
        this.eventUid = eventUid;
        this.eventId = eventId;
        this.recurringEventUid = recurringEventUid;
        this.recurringEventId = recurringEventId;
        this.calendarUid = calendarUid;
        this.iCalUID = iCalUID;
        this.hostUserUid = hostUserUid;
        this.recurrence = recurrence;
        this.summary = summary;
        this.url = url;
        this.location = location;
        this.locationNote = locationNote;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.status = status;
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
        return this.invitee;
    }

    public void setEventId(String id){ this.eventUid = id;}

    public void setStartTime(long startTime){ this.startTime = startTime; }

    public void setEndTime(long endTime){ this.endTime = endTime; }

    public String getEventUid(){ return eventUid; }

    public long getStartTime(){return startTime;}

    public long getEndTime(){return endTime;}

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDuration(){
        return (int)((endTime - startTime) /(1000*60));
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
    @Generated(hash = 777091542)
    public synchronized void resetInvitee() {
        invitee = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1368951675)
    public List<Invitee> getInvitee() {
        if (invitee == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InviteeDao targetDao = daoSession.getInviteeDao();
            List<Invitee> inviteeNew = targetDao._queryEvent_Invitee(eventUid);
            synchronized (this) {
                if(invitee == null) {
                    invitee = inviteeNew;
                }
            }
        }
        return invitee;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1459865304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
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


    public String getRecurrence() {
        return this.recurrence;
    }


    public void setRecurrence(String recurrence) {
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

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
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
    @Generated(hash = 1734061039)
    public List<TimeSlot> getTimeslots() {
        if (timeslots == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TimeSlotDao targetDao = daoSession.getTimeSlotDao();
            List<TimeSlot> timeslotsNew = targetDao._queryEvent_Timeslots(eventUid);
            synchronized (this) {
                if(timeslots == null) {
                    timeslots = timeslotsNew;
                }
            }
        }
        return timeslots;
    }





    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }





    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 481094641)
    public synchronized void resetTimeslots() {
        timeslots = null;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean hasAttendee(){
        return invitee!=null;
    }

    public boolean hasTimeslots(){
        return timeslots!=null;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public String getHostUserUid() {
        return hostUserUid;
    }

    public void setHostUserUid(String hostUserUid) {
        this.hostUserUid = hostUserUid;
    }


}
