package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventDetailPresenter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 29/08/2016.
 */
public class EventDetailViewModel extends BaseObservable {
    private EventDetailPresenter presenter;
    private Event eventDetailEvent;
    private String eventDetailTitleString;
    private String eventDetailRepeatString;
    private String eventDetailLocationString;
    private String eventDetailSuggestTimeSlotFst;
    private String eventDetailSuggestTimeSlotSnd;
    private String eventDetailSuggestTimeSlotTrd;
    private String eventDetailAttendeeString;
    private String eventDetailUrl;
    private String eventDetailNote;
    private int numberOfAttendee;


    public EventDetailViewModel(EventDetailPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public Event getEventDetailEvent() {
        return eventDetailEvent;
    }

    public void setEventDetailEvent(Event eventDetailEvent) {
        this.eventDetailEvent = eventDetailEvent;
        updateAll(eventDetailEvent);

    }

    private void updateAll(Event event){
        if (event.getTitle()!=null)
            setEventDetailTitleString(event.getTitle());

        setEventDetailRepeatString(getRepeatString(event.getRepeatTypeId()));

        if (event.getLocationAddress()!=null)
            setEventDetailLocationString(event.getLocationAddress());

        if (event.getProposedTimeSlots()!=null){
            setEventDetailSuggestTimeSlotFst(getSuggestTimeStringFromLong(event.getProposedTimeSlots().get(0), event.getDuration()));
            setEventDetailSuggestTimeSlotSnd(getSuggestTimeStringFromLong(event.getProposedTimeSlots().get(1), event.getDuration()));
            setEventDetailSuggestTimeSlotTrd(getSuggestTimeStringFromLong(event.getProposedTimeSlots().get(2), event.getDuration()));
        }
        if (event.getAttendees()!=null)
            setEventDetailAttendeeString(getAttendeeString(event.getAttendees()));

        if (event.getUrl()!=null)
            setEventDetailUrl(event.getUrl());
        if (event.getNote()!=null)
            setEventDetailNote(event.getNote());
    }


    private String getSuggestTimeStringFromLong(Long startTime,int duration){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String startTimeHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(startTime + duration * 60 * 1000);
        String endTimeHour = String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY));
        String endTimeMinute = String.valueOf(endCalendar.get(Calendar.MINUTE));
        String endAmOrPm = endCalendar.get(Calendar.HOUR_OF_DAY) >=12? "PM": "AM";

        return dayOfWeek + " " + day + "/" + month + " " + startTimeHour + ":" + startMinute +
                " " + startAmOrPm + " - " + endTimeHour + ":" + endTimeMinute + endAmOrPm;

    }

    public String getAttendeeString(ArrayList<String> attendeesArrayList){
        if (attendeesArrayList.size() == 0){
            return "None";
        }else if (attendeesArrayList.size() == 1)
            return attendeesArrayList.get(0);
        else{
            return String.format("%s and %d more",attendeesArrayList.get(0),attendeesArrayList.size()-1);
        }
    }


    private String getRepeatString(int repeatTypeId){
        String[] repeats={"None","Daily","Weekly","Monthly"};
        return repeats[repeatTypeId];
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "SUN";
            case 2:
                return "MON";
            case 3:
                return "TUE";
            case 4:
                return "WED";
            case 5:
                return "FRI";
            case 6:
                return "SAT";
            case 7:
                return "SUN";
        }
        return "error get day of week";
    }

    private String getMonth(int month){
        switch (month){
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "July";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "error get month";
        }
    }

    @Bindable
    public String getEventDetailTitleString() {
        return eventDetailTitleString;
    }

    public void setEventDetailTitleString(String eventDetailTitleString) {
        this.eventDetailTitleString = eventDetailTitleString;
    }

    @Bindable
    public String getEventDetailRepeatString() {
        return eventDetailRepeatString;
    }

    public void setEventDetailRepeatString(String eventDetailRepeatString) {
        this.eventDetailRepeatString = eventDetailRepeatString;
    }

    @Bindable
    public String getEventDetailLocationString() {
        return eventDetailLocationString;
    }

    public void setEventDetailLocationString(String eventDetailLocationString) {
        this.eventDetailLocationString = eventDetailLocationString;
    }

    @Bindable
    public String getEventDetailSuggestTimeSlotFst() {
        return eventDetailSuggestTimeSlotFst;
    }

    public void setEventDetailSuggestTimeSlotFst(String eventDetailSuggestTimeSlotFst) {
        this.eventDetailSuggestTimeSlotFst = eventDetailSuggestTimeSlotFst;
    }

    @Bindable
    public String getEventDetailSuggestTimeSlotSnd() {
        return eventDetailSuggestTimeSlotSnd;
    }

    public void setEventDetailSuggestTimeSlotSnd(String eventDetailSuggestTimeSlotSnd) {
        this.eventDetailSuggestTimeSlotSnd = eventDetailSuggestTimeSlotSnd;
    }

    @Bindable
    public String getEventDetailSuggestTimeSlotTrd() {
        return eventDetailSuggestTimeSlotTrd;
    }

    public void setEventDetailSuggestTimeSlotTrd(String eventDetailSuggestTimeSlotTrd) {
        this.eventDetailSuggestTimeSlotTrd = eventDetailSuggestTimeSlotTrd;
    }

    @Bindable
    public String getEventDetailAttendeeString() {
        return eventDetailAttendeeString;
    }

    public void setEventDetailAttendeeString(String eventDetailAttendeeString) {
        this.eventDetailAttendeeString = eventDetailAttendeeString;
    }

    @Bindable
    public String getEventDetailUrl() {
        return eventDetailUrl;
    }

    public void setEventDetailUrl(String eventDetailUrl) {
        this.eventDetailUrl = eventDetailUrl;
    }

    @Bindable
    public String getEventDetailNote() {
        return eventDetailNote;
    }

    public void setEventDetailNote(String eventDetailNote) {
        this.eventDetailNote = eventDetailNote;
    }

    public int getNumberOfAttendee() {
        return numberOfAttendee;
    }

    public void setNumberOfAttendee(int numberOfAttendee) {
        this.numberOfAttendee = numberOfAttendee;
    }
}
