package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventEditPresenter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends BaseObservable {

    private String eventEditViewTitle;
    private String eventEditViewLocation;
    private String eventEditViewRepeat;
    private String eventEditViewSuggestTimeSlotFst;
    private String eventEditViewSuggestTimeSlotSnd;
    private String eventEditViewSuggestTimeSlotTrd;
    private String eventEditViewAttendeeString;
    private String eventEditViewUrl;
    private String eventEditViewNote;
    private String eventEditCalendarType;

    private Event eventEditViewEvent;

    private EventEditPresenter eventEditPresenter;
    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.eventEditPresenter = eventEditPresenter;
    }

    public void updateAll(){
        if (eventEditViewEvent.hasEventTitle()){
            setEventEditViewTitle(eventEditViewEvent.getTitle());
        }
        if (eventEditViewEvent.hasEventLocationAddress()){
            setEventEditViewLocation(eventEditViewEvent.getLocationAddress());
        }

        setEventEditViewRepeat(getRepeatString(eventEditViewEvent.getRepeatTypeId()));

        if (eventEditViewEvent.hasProposedTimeslots()){
            setEventEditViewSuggestTimeSlotFst(getSuggestTimeStringFromLong(eventEditViewEvent.getProposedTimeSlots().get(0),eventEditViewEvent.getDuration()));
            setEventEditViewSuggestTimeSlotSnd(getSuggestTimeStringFromLong(eventEditViewEvent.getProposedTimeSlots().get(1),eventEditViewEvent.getDuration()));
            setEventEditViewSuggestTimeSlotTrd(getSuggestTimeStringFromLong(eventEditViewEvent.getProposedTimeSlots().get(2),eventEditViewEvent.getDuration()));
        }

        if (eventEditViewEvent.hasAttendee()){
            setEventEditViewAttendeeString(getAttendeeString(eventEditViewEvent.getAttendees()));
        }
        if (eventEditViewEvent.hasUrl()){
            setEventEditViewUrl(eventEditViewEvent.getUrl());
        }
        if (eventEditViewEvent.hasEventNote()){
            setEventEditViewNote(eventEditViewEvent.getNote());
        }
    }

    private String getSuggestTimeStringFromLong(Long startTime, int duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String startTimeHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE)<10? "0"+String.valueOf(calendar.get(Calendar.MINUTE)): String.valueOf(calendar.get(Calendar.MINUTE));
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(startTime + duration * 60 * 1000);
        String endTimeHour = String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY));
        String endTimeMinute = endCalendar.get(Calendar.MINUTE)<10? "0"+String.valueOf(endCalendar.get(Calendar.MINUTE)) :String.valueOf(endCalendar.get(Calendar.MINUTE));
        String endAmOrPm = endCalendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        return dayOfWeek + " " + day + "/" + month + " " + startTimeHour + ":" + startMinute +
                " " + startAmOrPm + " - " + endTimeHour + ":" + endTimeMinute + endAmOrPm;

    }

    public String getAttendeeString(ArrayList<String> attendeesArrayList) {
        if (attendeesArrayList.size() == 0) {
            return "None";
        } else if (attendeesArrayList.size() == 1)
            return attendeesArrayList.get(0);
        else {
            return String.format("%s and %d more", attendeesArrayList.get(0), attendeesArrayList.size() - 1);
        }
    }


    private String getRepeatString(int repeatTypeId) {
        String[] repeats = {"None", "Daily", "Weekly", "Monthly"};
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

    private String getMonth(int month) {
        switch (month) {
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

    public View.OnClickListener finishEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventEditViewEvent.setTitle(eventEditViewTitle);
                eventEditPresenter.toHostEventDetail(eventEditViewEvent);
            }
        };
    }

    public View.OnClickListener cancelEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventEditPresenter.toHostEventDetail(eventEditViewEvent);
            }
        };
    }

    public void saveEdits(){
        // also need to save others, implement later
        eventEditViewEvent.setTitle(eventEditViewTitle);

    }


    @Bindable
    public String getEventEditViewTitle() {
        return eventEditViewTitle;
    }

    public void setEventEditViewTitle(String eventEditViewTitle) {
        this.eventEditViewTitle = eventEditViewTitle;
        notifyPropertyChanged(BR.eventEditViewTitle);
    }

    @Bindable
    public String getEventEditViewLocation() {
        return eventEditViewLocation;
    }

    public void setEventEditViewLocation(String eventEditViewLocation) {
        this.eventEditViewLocation = eventEditViewLocation;
        notifyPropertyChanged(BR.eventEditViewLocation);
    }

    @Bindable
    public String getEventEditViewRepeat() {
        return eventEditViewRepeat;
    }

    public void setEventEditViewRepeat(String eventEditViewRepeat) {
        this.eventEditViewRepeat = eventEditViewRepeat;
        notifyPropertyChanged(BR.eventEditViewRepeat);
    }

    @Bindable
    public String getEventEditViewSuggestTimeSlotFst() {
        return eventEditViewSuggestTimeSlotFst;
    }

    public void setEventEditViewSuggestTimeSlotFst(String eventEditViewSuggestTimeSlotFst) {
        this.eventEditViewSuggestTimeSlotFst = eventEditViewSuggestTimeSlotFst;
        notifyPropertyChanged(BR.eventEditViewSuggestTimeSlotFst);
    }

    @Bindable
    public String getEventEditViewSuggestTimeSlotSnd() {
        return eventEditViewSuggestTimeSlotSnd;
    }

    public void setEventEditViewSuggestTimeSlotSnd(String eventEditViewSuggestTimeSlotSnd) {
        this.eventEditViewSuggestTimeSlotSnd = eventEditViewSuggestTimeSlotSnd;
        notifyPropertyChanged(BR.eventEditViewSuggestTimeSlotSnd);
    }

    @Bindable
    public String getEventEditViewSuggestTimeSlotTrd() {
        return eventEditViewSuggestTimeSlotTrd;
    }

    public void setEventEditViewSuggestTimeSlotTrd(String eventEditViewSuggestTimeSlotTrd) {
        this.eventEditViewSuggestTimeSlotTrd = eventEditViewSuggestTimeSlotTrd;
        notifyPropertyChanged(BR.eventEditViewSuggestTimeSlotTrd);
    }

    @Bindable
    public String getEventEditViewAttendeeString() {
        return eventEditViewAttendeeString;
    }

    public void setEventEditViewAttendeeString(String eventEditViewAttendeeString) {
        this.eventEditViewAttendeeString = eventEditViewAttendeeString;
        notifyPropertyChanged(BR.eventEditViewAttendeeString);
    }

    @Bindable
    public String getEventEditViewUrl() {
        return eventEditViewUrl;
    }

    public void setEventEditViewUrl(String eventEditViewUrl) {
        this.eventEditViewUrl = eventEditViewUrl;
        notifyPropertyChanged(BR.eventEditViewUrl);
    }

    @Bindable
    public String getEventEditViewNote() {
        return eventEditViewNote;
    }

    public void setEventEditViewNote(String eventEditViewNote) {
        this.eventEditViewNote = eventEditViewNote;
        notifyPropertyChanged(BR.eventEditViewNote);
    }

    @Bindable
    public Event getEventEditViewEvent() {
        return eventEditViewEvent;
    }

    public void setEventEditViewEvent(Event eventEditViewEvent) {
        this.eventEditViewEvent = eventEditViewEvent;
        notifyPropertyChanged(BR.eventEditViewEvent);
        updateAll();
    }

    @Bindable
    public String getEventEditCalendarType() {
        return eventEditCalendarType;
    }

    public void setEventEditCalendarType(String eventEditCalendarType) {
        this.eventEditCalendarType = eventEditCalendarType;
        notifyPropertyChanged(BR.eventEditCalendarType);
    }
}
