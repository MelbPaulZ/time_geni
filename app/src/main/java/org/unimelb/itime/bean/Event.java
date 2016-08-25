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


    @Override
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
    public void setNote(String s) {
        eventNote = s;
    }

    @Override
    public String getNote() {
        return eventNote;
    }

    @Override
    public void setLocationAddress(String s) {
        currentLocationAddress = s;
    }

    @Override
    public String getLocationAddress() {
        return currentLocationAddress;
    }

    @Override
    public void setLocationNote(String s) {
        currentLocationNote = s;
    }

    @Override
    public String getLocationNote() {
        return currentLocationNote;
    }

    @Override
    public void setLocationLatitude(double v) {
        currentLocationLatitude = v;
    }

    @Override
    public Double getLocationLatitude() {
        return currentLocationLatitude;
    }

    @Override
    public void setLocationLongitude(double v) {
        currentLocationLongitude = v;
    }

    @Override
    public Double getLocationLongitude() {
        return currentLocationLongitude;
    }

    @Override
    public void setRepeatTypeId(int i) {
        currentRepeatTypeId = i;
    }

    @Override
    public int getRepeatTypeId() {
        return currentRepeatTypeId;
    }

    @Override
    public void setUserId(String s) {
        userId = s;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setAlertTimeId(int i) {
        alerTimeId = i;
    }

    @Override
    public int getAlertTimeId() {
        return alerTimeId;
    }

    @Override
    public void setEventTypeId(int i) {
        eventTypeId = i;
    }

    @Override
    public int getEventTypeId() {
        return eventTypeId;
    }

    @Override
    public void setVisibilityTypeId(int i) {
        visibilityTypeId = i;
    }

    @Override
    public int getVisibilityTypeId() {
        return visibilityTypeId;
    }

    @Override
    public void setEventSourceId(int i) {
        eventSourceId = i;
    }

    @Override
    public int getEventSourceId() {
        return eventSourceId;
    }

    @Override
    public void setCalendarTypeId(String s) {
        calendarTypedId = s;
    }

    @Override
    public String getCalendarTypeId() {
        return calendarTypedId;
    }

    @Override
    public void setIsInfiniteRepeat(Boolean aBoolean) {
        isInfiniteRepeat = aBoolean;
    }

    @Override
    public Boolean getIsInfiniteRepeat() {
        return isInfiniteRepeat;
    }

    @Override
    public void setIsDeleted(Boolean aBoolean) {
        isDeleted = aBoolean;
    }

    @Override
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @Override
    public void setRepeatEndsTime(Integer integer) {
        repeatEndsTime = integer;
    }

    @Override
    public Integer getRepeatEndsTime() {
        return repeatEndsTime;
    }

    @Override
    public void setHostEventId(String s) {
        hostEventId = s;
    }

    @Override
    public String getHostEventId() {
        return hostEventId;
    }

    @Override
    public void setUserStatusId(int i) {
        userStatusId = i;
    }

    @Override
    public int getUserStatusId() {
        return userStatusId;
    }

    @Override
    public void setEventPhotos(ArrayList<String> arrayList) {
        eventPhotos = arrayList;
    }

    @Override
    public ArrayList<String> getEventPhotos() {
        return eventPhotos;
    }

    @Override
    public void setUrl(String s) {
        url = s;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setDuration(int i) {
        duration = i;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setAttendees(ArrayList<String> arrayList) {
        attendees = arrayList;
    }

    @Override
    public ArrayList<String> getAttendees() {
        return attendees;
    }

    @Override
    public void setProposedTimeSlots(ArrayList<Long> arrayList) {
        proposedTimeslots = arrayList;
    }

    @Override
    public ArrayList<Long> getProposedTimeSlots() {
        return proposedTimeslots;
    }
}
