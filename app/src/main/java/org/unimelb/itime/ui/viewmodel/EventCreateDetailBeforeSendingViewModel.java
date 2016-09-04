package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingViewModel extends BaseObservable {
    private Event newEvDtlEvent;
    private String newEvDtlTitleStr;
    private String newEvDtlLocationStr;
    private String newEvDtlRepeatStr;
    private String newEvDtlAttendeeStr;
    private String newEvDtlTimeSlotFst;
    private String newEvDtlTimeSlotSnd;
    private String newEvDtlTimeSlotTrd;
    private String newEvDtlALtTimeStr;
    private String newEvDtlCldTpStr;
    private String newEvDtlUrlStr;
    private String newEvDtlNotes;


    private EventCreateDetailBeforeSendingPresenter presenter;

    public EventCreateDetailBeforeSendingViewModel(EventCreateDetailBeforeSendingPresenter presenter,Event event) {
        this.presenter = presenter;
        this.newEvDtlEvent = event;
        updateAllInfo();
    }

    public void updateAllInfo(){
        if (newEvDtlEvent.hasEventTitle())
            setNewEvDtlTitleStr(newEvDtlEvent.getTitle());
        if (newEvDtlEvent.hasEventLocationAddress())
            setNewEvDtlLocationStr(newEvDtlEvent.getLocationAddress());

        setNewEvDtlRepeatStr(getRepeatString(newEvDtlEvent.getRepeatTypeId()));
        if (newEvDtlEvent.hasAttendee())
            setNewEvDtlAttendeeStr(getAttendeeString(newEvDtlEvent.getAttendees()));

        setNewEvDtlTimeSlotFst(getSuggestTimeStringFromLong(
                newEvDtlEvent.getProposedTimeSlots().get(0),newEvDtlEvent.getDuration()));
        setNewEvDtlTimeSlotSnd(getSuggestTimeStringFromLong(
                newEvDtlEvent.getProposedTimeSlots().get(1), newEvDtlEvent.getDuration()));
        setNewEvDtlTimeSlotTrd(getSuggestTimeStringFromLong(
                newEvDtlEvent.getProposedTimeSlots().get(2), newEvDtlEvent.getDuration()));
        if (newEvDtlEvent.hasCalendarTypedId())
            setNewEvDtlCldTpStr(newEvDtlEvent.getCalendarTypeId());
        if (newEvDtlEvent.hasUrl())
            setNewEvDtlUrlStr(newEvDtlEvent.getUrl());
        if (newEvDtlEvent.hasEventNote())
            setNewEvDtlNotes(newEvDtlEvent.getNote());
    }


    private String getSuggestTimeStringFromLong(Long startTime,int duration){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String startTimeHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE)<10? "0" + String.valueOf(calendar.get(Calendar.MINUTE)) : String.valueOf(calendar.get(Calendar.MINUTE));
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(startTime + duration * 60 * 1000);
        String endTimeHour = String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY));
        String endTimeMinute = endCalendar.get(Calendar.MINUTE)<10? "0" + String.valueOf(endCalendar.get(Calendar.MINUTE)) : String.valueOf(endCalendar.get(Calendar.MINUTE));
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
//    *********************************************************************

    @Bindable
    public String getNewEvDtlTitleStr() {
        return newEvDtlTitleStr;
    }

    public void setNewEvDtlTitleStr(String newEvDtlTitleStr) {
        this.newEvDtlTitleStr = newEvDtlTitleStr;
        notifyPropertyChanged(BR.newEvDtlTitleStr);
    }

    @Bindable
    public String getNewEvDtlLocationStr() {
        return newEvDtlLocationStr;
    }

    public void setNewEvDtlLocationStr(String newEvDtlLocationStr) {
        this.newEvDtlLocationStr = newEvDtlLocationStr;
        notifyPropertyChanged(BR.newEvDtlLocationStr);
    }

    @Bindable
    public String getNewEvDtlRepeatStr() {
        return newEvDtlRepeatStr;
    }

    public void setNewEvDtlRepeatStr(String newEvDtlRepeatStr) {
        this.newEvDtlRepeatStr = newEvDtlRepeatStr;
        notifyPropertyChanged(BR.newEvDtlRepeatStr);
    }

    @Bindable
    public String getNewEvDtlAttendeeStr() {
        return newEvDtlAttendeeStr;
    }

    public void setNewEvDtlAttendeeStr(String newEvDtlAttendeeStr) {
        this.newEvDtlAttendeeStr = newEvDtlAttendeeStr;
        notifyPropertyChanged(BR.newEvDtlAttendeeStr);
    }

    @Bindable
    public String getNewEvDtlTimeSlotFst() {
        return newEvDtlTimeSlotFst;
    }

    public void setNewEvDtlTimeSlotFst(String newEvDtlTimeSlotFst) {
        this.newEvDtlTimeSlotFst = newEvDtlTimeSlotFst;
        notifyPropertyChanged(BR.newEvDtlTimeSlotFst);
    }

    @Bindable
    public String getNewEvDtlTimeSlotSnd() {
        return newEvDtlTimeSlotSnd;
    }

    public void setNewEvDtlTimeSlotSnd(String newEvDtlTimeSlotSnd) {
        this.newEvDtlTimeSlotSnd = newEvDtlTimeSlotSnd;
        notifyPropertyChanged(BR.newEvDtlTimeSlotSnd);
    }

    @Bindable
    public String getNewEvDtlTimeSlotTrd() {
        return newEvDtlTimeSlotTrd;
    }

    public void setNewEvDtlTimeSlotTrd(String newEvDtlTimeSlotTrd) {
        this.newEvDtlTimeSlotTrd = newEvDtlTimeSlotTrd;
        notifyPropertyChanged(BR.newEvDtlTimeSlotTrd);
    }


    @Bindable
    public String getNewEvDtlCldTpStr() {
        return newEvDtlCldTpStr;
    }

    public void setNewEvDtlCldTpStr(String newEvDtlCldTpStr) {
        this.newEvDtlCldTpStr = newEvDtlCldTpStr;
        notifyPropertyChanged(BR.newEvDtlCldTpStr);
    }

    @Bindable
    public String getNewEvDtlUrlStr() {
        return newEvDtlUrlStr;
    }

    public void setNewEvDtlUrlStr(String newEvDtlUrlStr) {
        this.newEvDtlUrlStr = newEvDtlUrlStr;
        notifyPropertyChanged(BR.newEvDtlUrlStr);
    }

    @Bindable
    public String getNewEvDtlNotes() {
        return newEvDtlNotes;
    }

    public void setNewEvDtlNotes(String newEvDtlNotes) {
        this.newEvDtlNotes = newEvDtlNotes;
        notifyPropertyChanged(BR.newEvDtlNotes);
    }

    @Bindable
    public Event getNewEvDtlEvent() {
        return newEvDtlEvent;
    }

    @Bindable
    public String getNewEvDtlALtTimeStr() {
        return newEvDtlALtTimeStr;
    }

    public void setNewEvDtlALtTimeStr(String newEvDtlALtTimeStr) {
        this.newEvDtlALtTimeStr = newEvDtlALtTimeStr;
        notifyPropertyChanged(BR.newEvDtlALtTimeStr);
    }

    public void setNewEvDtlEvent(Event newEvDtlEvent) {
        this.newEvDtlEvent = newEvDtlEvent;
        notifyPropertyChanged(BR.newEvDtlEvent);
        updateAllInfo();
    }


//    *******************************************************************************************************


}
