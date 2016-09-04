package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventDetailForInviteePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;


/**
 * Created by Paul on 29/08/2016.
 */
public class EventDetailViewForInviteeViewModel extends BaseObservable {
    private EventDetailForInviteePresenter presenter;
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

    private boolean[] timeSlotChooseArray;
    private boolean isSelectTimeSlot;

    private LayoutInflater inflater;


    public EventDetailViewForInviteeViewModel(EventDetailForInviteePresenter presenter) {
        this.presenter = presenter;
        timeSlotChooseArray = new boolean[]{false, false, false};
    }

    public View.OnClickListener onClickToWeekViewCalendar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.gotoWeekViewCalendar();
            }
        };
    }

    public View.OnClickListener onTimeSlotSelect1(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotChooseArray[0] == false){
                    timeSlotChooseArray[0] = true;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_selected);
                }else{
                    timeSlotChooseArray[0] = false;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                }

                if (isHasSelectAtLeastOneTimeSlot()){
                    setSelectTimeSlot(true);
                }else{
                    setSelectTimeSlot(false);
                }
            }
        };
    }

    public View.OnClickListener onTimeSlotSelect2(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotChooseArray[1] == false){
                    timeSlotChooseArray[1] = true;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_selected);
                }else{
                    timeSlotChooseArray[1] = false;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                }

                if (isHasSelectAtLeastOneTimeSlot()){
                    setSelectTimeSlot(true);
                }else{
                    setSelectTimeSlot(false);
                }
            }
        };
    }

    public View.OnClickListener onTimeSlotSelect3(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotChooseArray[2] == false){
                    timeSlotChooseArray[2] = true;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_selected);
                }else{
                    timeSlotChooseArray[2] = false;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                }

                if (isHasSelectAtLeastOneTimeSlot()){
                    setSelectTimeSlot(true);
                }else{
                    setSelectTimeSlot(false);
                }
            }
        };
    }


    private boolean isHasSelectAtLeastOneTimeSlot(){
        for (Boolean timeslotSelect: timeSlotChooseArray){
            if (timeslotSelect)
                return true;
        }
        return false;
    }

    public View.OnClickListener onClickRejectAll(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();

                inflater = presenter.getInflater();
                View root = inflater.inflate(R.layout.event_detail_reject_alert_view, null);

                TextView button_cancel = (TextView) root.findViewById(R.id.alert_message_cancel_button);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                TextView button_reject=(TextView) root.findViewById(R.id.alert_message_reject_button);
                button_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence msg = "send reject message";
                        Toast.makeText(presenter.getContext(),msg,Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(root);
                alertDialog.show();
            }
        };
    }

    public View.OnClickListener onClickConfirm(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.confirmAndGotoWeekViewCalendar(eventDetailEvent,timeSlotChooseArray);
            }
        };
    }




    @Bindable
    public Event getEventDetailEvent() {
        return eventDetailEvent;
    }

    public void setEventDetailEvent(Event eventDetailEvent) {
        this.eventDetailEvent = eventDetailEvent;
        notifyPropertyChanged(BR.eventDetailEvent);
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
        String startMinute = calendar.get(Calendar.MINUTE)<10? "0"+String.valueOf(calendar.get(Calendar.MINUTE)): String.valueOf(calendar.get(Calendar.MINUTE));
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(startTime + duration * 60 * 1000);
        String endTimeHour = String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY));
        String endTimeMinute = endCalendar.get(Calendar.MINUTE)<10? "0"+String.valueOf(endCalendar.get(Calendar.MINUTE)) :String.valueOf(endCalendar.get(Calendar.MINUTE));
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
        notifyPropertyChanged(BR.eventDetailTitleString);
    }

    @Bindable
    public String getEventDetailRepeatString() {
        return eventDetailRepeatString;
    }

    public void setEventDetailRepeatString(String eventDetailRepeatString) {
        this.eventDetailRepeatString = eventDetailRepeatString;
        notifyPropertyChanged(BR.eventDetailRepeatString);
    }

    @Bindable
    public String getEventDetailLocationString() {
        return eventDetailLocationString;
    }

    public void setEventDetailLocationString(String eventDetailLocationString) {
        this.eventDetailLocationString = eventDetailLocationString;
        notifyPropertyChanged(BR.eventDetailLocationString);
    }

    @Bindable
    public String getEventDetailSuggestTimeSlotFst() {
        return eventDetailSuggestTimeSlotFst;
    }

    public void setEventDetailSuggestTimeSlotFst(String eventDetailSuggestTimeSlotFst) {
        this.eventDetailSuggestTimeSlotFst = eventDetailSuggestTimeSlotFst;
        notifyPropertyChanged(BR.eventDetailSuggestTimeSlotFst);
    }

    @Bindable
    public String getEventDetailSuggestTimeSlotSnd() {
        return eventDetailSuggestTimeSlotSnd;
    }

    public void setEventDetailSuggestTimeSlotSnd(String eventDetailSuggestTimeSlotSnd) {
        this.eventDetailSuggestTimeSlotSnd = eventDetailSuggestTimeSlotSnd;
        notifyPropertyChanged(BR.eventDetailSuggestTimeSlotSnd);
    }

    @Bindable
    public String getEventDetailSuggestTimeSlotTrd() {
        return eventDetailSuggestTimeSlotTrd;
    }

    public void setEventDetailSuggestTimeSlotTrd(String eventDetailSuggestTimeSlotTrd) {
        this.eventDetailSuggestTimeSlotTrd = eventDetailSuggestTimeSlotTrd;
        notifyPropertyChanged(BR.eventDetailSuggestTimeSlotTrd);
    }

    @Bindable
    public String getEventDetailAttendeeString() {
        return eventDetailAttendeeString;
    }

    public void setEventDetailAttendeeString(String eventDetailAttendeeString) {
        this.eventDetailAttendeeString = eventDetailAttendeeString;
        notifyPropertyChanged(BR.eventDetailAttendeeString);
    }

    @Bindable
    public String getEventDetailUrl() {
        return eventDetailUrl;
    }

    public void setEventDetailUrl(String eventDetailUrl) {
        this.eventDetailUrl = eventDetailUrl;
        notifyPropertyChanged(BR.eventDetailUrl);
    }

    @Bindable
    public String getEventDetailNote() {
        return eventDetailNote;
    }

    public void setEventDetailNote(String eventDetailNote) {
        this.eventDetailNote = eventDetailNote;
        notifyPropertyChanged(BR.eventDetailNote);
    }

    @Bindable
    public boolean isSelectTimeSlot() {
        return isSelectTimeSlot;
    }

    public void setSelectTimeSlot(boolean selectTimeSlot) {
        isSelectTimeSlot = selectTimeSlot;
        notifyPropertyChanged(BR.selectTimeSlot);
    }

}
