package org.unimelb.itime.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

//import com.android.databinding.library.baseAdapters.BR;

//import org.unimelb.itime.BR;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by Paul on 23/08/2016.
 */
public class Event extends BaseObservable implements ITimeEventInterface {
    private String eventId;
    private String eventTitle;
    private String eventNote;
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
    private String calendarTypedId;
    private Boolean isInfiniteRepeat;
    private Boolean isDeleted;
    private int repeatEndsTime;
    private String hostEventId;
    private int userStatusId;
    private ArrayList<String> attendees; // need to be checked
    private ArrayList<String> eventPhotos;
    private String url;
    private int duration;
//    attendee repeat?
    private ArrayList<Long> proposedTimeslots;

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
                 int repeatEndsTime,
                 String hostEventId,
                 int userStatusId,
                 ArrayList<String> attendees,
                 ArrayList<String> eventPhotos,
                 String url,
                 int duration,
                 ArrayList<Long> proposedTimeslots) {
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
        this.attendees = attendees;
        this.eventPhotos = eventPhotos;
        this.url = url;
        this.duration = duration;
        this.proposedTimeslots = proposedTimeslots;
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

    }

    @Override
    public long getStartTime() {
        return 0;
    }

    @Override
    public void setEndTime(long l) {

    }

    @Override
    public long getEndTime() {
        return 0;
    }

    @Override
    public void setEventType(int i) {

    }

    @Override
    public int getEventType() {
        return 0;
    }

    @Override
    public void setStatus(int i) {

    }

    @Override
    public int getStatus() {
        return 0;
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


    public void setRepeatEndsTime(Integer integer) {
        repeatEndsTime = integer;
    }


    public Integer getRepeatEndsTime() {
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


    public void setEventPhotos(ArrayList<String> arrayList) {
        eventPhotos = arrayList;
    }


    public ArrayList<String> getEventPhotos() {
        return eventPhotos;
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
        return duration;
    }


    public void setAttendees(ArrayList<String> arrayList) {
        attendees = arrayList;
    }


    public ArrayList<String> getAttendees() {
        return attendees;
    }


    public void setProposedTimeSlots(ArrayList<Long> arrayList) {
        proposedTimeslots = arrayList;
    }


    public ArrayList<Long> getProposedTimeSlots() {
        return proposedTimeslots;
    }
}
