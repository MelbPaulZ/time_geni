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
    private long[] proposedTimeslots;

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
                 long[] proposedTimeslots) {
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
        return null;
    }

    @Override
    public void setTitle(String s) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setNote(String s) {

    }

    @Override
    public String getNote() {
        return null;
    }

    @Override
    public void setLocationAddress(String s) {

    }

    @Override
    public String getLocationAddress() {
        return null;
    }

    @Override
    public void setLocationNote(String s) {

    }

    @Override
    public String getLocationNote() {
        return null;
    }

    @Override
    public void setLocationLatitude(double v) {

    }

    @Override
    public Double getLocationLatitude() {
        return null;
    }

    @Override
    public void setLocationLongitude(double v) {

    }

    @Override
    public Double getLocationLongitude() {
        return null;
    }

    @Override
    public void setRepeatTypeId(int i) {

    }

    @Override
    public int getRepeatTypeId() {
        return 0;
    }

    @Override
    public void setUserId(String s) {

    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public void setAlertTimeId(int i) {

    }

    @Override
    public int getAlertTimeId() {
        return 0;
    }

    @Override
    public void setEventTypeId(int i) {

    }

    @Override
    public int getEventTypeId() {
        return 0;
    }

    @Override
    public void setVisibilityTypeId(int i) {

    }

    @Override
    public int getVisibilityTypeId() {
        return 0;
    }

    @Override
    public void setEventSourceId(int i) {

    }

    @Override
    public int getEventSourceId() {
        return 0;
    }

    @Override
    public void setCalendarTypeId(String s) {

    }

    @Override
    public String getCalendarTypeId() {
        return null;
    }

    @Override
    public void setIsInfiniteRepeat(Boolean aBoolean) {

    }

    @Override
    public Boolean getIsInfiniteRepeat() {
        return null;
    }

    @Override
    public void setIsDeleted(Boolean aBoolean) {

    }

    @Override
    public Boolean getIsDeleted() {
        return null;
    }

    @Override
    public void setRepeatEndsTime(Integer integer) {

    }

    @Override
    public Integer getRepeatEndsTime() {
        return null;
    }

    @Override
    public void setHostEventId(String s) {

    }

    @Override
    public String getHostEventId() {
        return null;
    }

    @Override
    public void setUserStatusId(int i) {

    }

    @Override
    public int getUserStatusId() {
        return 0;
    }

    @Override
    public void setEventPhotos(ArrayList<URL> arrayList) {

    }

    @Override
    public ArrayList<URL> getEventPhotos() {
        return null;
    }

    @Override
    public void setUrl(String s) {

    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public void setDuration(int i) {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void setAttendees(ArrayList<String> arrayList) {

    }

    @Override
    public ArrayList<String> getAttendees() {
        return null;
    }

    @Override
    public void setProposedTimeSlots(ArrayList<Long> arrayList) {

    }

    @Override
    public ArrayList<Long> getProposedTimeSlots() {
        return null;
    }
}
