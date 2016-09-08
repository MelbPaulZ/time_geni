package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventDetailForHostPresenter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailForHostViewModel extends BaseObservable {
    private EventDetailForHostPresenter presenter;
    private Event EvDtlHostEvent;
    private String EvDtlHostTitleStr;
    private String EvDtlHostRepeatStr;
    private String EvDtlHostLocaStr;
    private String EvDtlHostTimeSltFst;
    private String EvDtlHostTimeSltSnd;
    private String EvDtlHostTimeSltTrd;
    private String EvDtlHostAtdStr;
    private String EvDtlHostUrlStr;
    private String EvDtlHostNoteStr;

    private boolean isSelectTimeSlot;
    private boolean[] timeSlotChooseArray = {false, false, false};
    private ArrayList<View> timeslots = new ArrayList<>();

    public EventDetailForHostViewModel(EventDetailForHostPresenter presenter) {
        this.presenter = presenter;
    }

    public void updateAll(){
        if (EvDtlHostEvent.hasEventTitle()){
            setEvDtlHostTitleStr(EvDtlHostEvent.getTitle());
        }
        EvDtlHostRepeatStr = getRepeatString(EvDtlHostEvent.getRepeatTypeId());
        if (EvDtlHostEvent.hasEventLocationAddress()){
            setEvDtlHostLocaStr(EvDtlHostEvent.getLocationAddress());
        }
        if (EvDtlHostEvent.hasProposedTimeslots()){
            setEvDtlHostTimeSltFst(getSuggestTimeStringFromLong(EvDtlHostEvent.getProposedTimeSlots().get(0), EvDtlHostEvent.getDuration()));
            setEvDtlHostTimeSltSnd(getSuggestTimeStringFromLong(EvDtlHostEvent.getProposedTimeSlots().get(1), EvDtlHostEvent.getDuration()));
            setEvDtlHostTimeSltTrd(getSuggestTimeStringFromLong(EvDtlHostEvent.getProposedTimeSlots().get(2), EvDtlHostEvent.getDuration()));
        }
        if (EvDtlHostEvent.hasAttendee()){
            setEvDtlHostAtdStr(getAttendeeString(EvDtlHostEvent.getAttendees()));
        }
        if (EvDtlHostEvent.hasUrl()){
            setEvDtlHostUrlStr(EvDtlHostEvent.getUrl());
        }
        if (EvDtlHostEvent.hasEventNote()){
            setEvDtlHostNoteStr(EvDtlHostEvent.getNote());
        }
    }

    public View.OnClickListener editEvent(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toEditEvent(EvDtlHostEvent);
            }
        };
    }

    public View.OnClickListener onHostTimeSlotSelect1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotChooseArray[0] == false) {
                    setTimeSlotChooseArray(new boolean[]{true, false, false});
                    for (View timeslot : timeslots) {
                        timeslot.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                        timeslots.remove(timeslot);
                    }

                    view.setBackgroundResource(R.drawable.icon_event_attendee_selected);
                    timeslots.add(view);
                } else {
                    timeSlotChooseArray[0] = false;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                    timeslots.remove(view);
                }

                if (isHasSelectAtLeastOneTimeSlot()) {
                    setSelectTimeSlot(true);
                } else {
                    setSelectTimeSlot(false);
                }
            }
        };
    }


    public View.OnClickListener onHostTimeSlotSelect2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotChooseArray[1] == false) {
//                    timeSlotChooseArray[1] = true;
                    setTimeSlotChooseArray(new boolean[]{false, true, false});

                    for (View timeslot : timeslots) {
                        timeslot.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                        timeslots.remove(timeslot);
                    }

                    view.setBackgroundResource(R.drawable.icon_event_attendee_selected);
                    timeslots.add(view);
                } else {
                    timeSlotChooseArray[1] = false;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                    timeslots.remove(view);
                }

                if (isHasSelectAtLeastOneTimeSlot()) {
                    setSelectTimeSlot(true);
                } else {
                    setSelectTimeSlot(false);
                }
            }
        };
    }


    public View.OnClickListener onHostTimeSlotSelect3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSlotChooseArray[2] == false) {
//                    timeSlotChooseArray[2] = true;
                    setTimeSlotChooseArray(new boolean[]{false, false, true});
                    for (View timeslot : timeslots) {
                        timeslot.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                        timeslots.remove(timeslot);
                    }

                    view.setBackgroundResource(R.drawable.icon_event_attendee_selected);
                    timeslots.add(view);
                } else {
                    timeSlotChooseArray[2] = false;
                    view.setBackgroundResource(R.drawable.icon_event_attendee_unselected);
                    timeslots.remove(view);
                }

                if (isHasSelectAtLeastOneTimeSlot()) {
                    setSelectTimeSlot(true);
                } else {
                    setSelectTimeSlot(false);
                }
            }
        };
    }


    public View.OnClickListener onClickConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to save data later, implement later
                presenter.toWeekView();
            }
        };
    }

    public View.OnClickListener onClickBack(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                presenter.toEventDetailHost(); // wrong
                presenter.toWeekView();
            }
        };
    }

    public View.OnClickListener toAttendeeView1(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getProposedTimeSlots().get(0));
            }
        };
    }

    public View.OnClickListener toAttendeeView2(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getProposedTimeSlots().get(1));
            }
        };
    }

    public View.OnClickListener toAttendeeView3(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getProposedTimeSlots().get(2));
            }
        };
    }



    private boolean isHasSelectAtLeastOneTimeSlot() {
        for (Boolean timeslotSelect : timeSlotChooseArray) {
            if (timeslotSelect)
                return true;
        }
        return false;
    }


    public void setSelectTimeSlot(boolean selectTimeSlot) {
        isSelectTimeSlot = selectTimeSlot;
        notifyPropertyChanged(BR.selectTimeSlot);
    }

    private String getSuggestTimeStringFromLong(Long startTime, int duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String startTimeHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE)<10? "0" +String.valueOf(calendar.get(Calendar.MINUTE)) : String.valueOf(calendar.get(Calendar.MINUTE));
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

//    ***************************************************************

    @Bindable
    public Event getEvDtlHostEvent() {
        return EvDtlHostEvent;
    }

    public void setEvDtlHostEvent(Event evDtlHostEvent) {
        EvDtlHostEvent = evDtlHostEvent;
        notifyPropertyChanged(BR.evDtlHostEvent);
        updateAll();
    }

    @Bindable
    public String getEvDtlHostTitleStr() {
        return EvDtlHostTitleStr;
    }

    public void setEvDtlHostTitleStr(String evDtlHostTitleStr) {
        EvDtlHostTitleStr = evDtlHostTitleStr;
        notifyPropertyChanged(BR.evDtlHostTitleStr);
    }

    @Bindable
    public String getEvDtlHostRepeatStr() {
        return EvDtlHostRepeatStr;
    }

    public void setEvDtlHostRepeatStr(String evDtlHostRepeatStr) {
        EvDtlHostRepeatStr = evDtlHostRepeatStr;
        notifyPropertyChanged(BR.evDtlHostRepeatStr);
    }

    @Bindable
    public String getEvDtlHostLocaStr() {
        return EvDtlHostLocaStr;
    }

    public void setEvDtlHostLocaStr(String evDtlHostLocaStr) {
        EvDtlHostLocaStr = evDtlHostLocaStr;
        notifyPropertyChanged(BR.evDtlHostLocaStr);
    }

    @Bindable
    public String getEvDtlHostTimeSltFst() {
        return EvDtlHostTimeSltFst;
    }

    public void setEvDtlHostTimeSltFst(String evDtlHostTimeSltFst) {
        EvDtlHostTimeSltFst = evDtlHostTimeSltFst;
        notifyPropertyChanged(BR.evDtlHostTimeSltFst);
    }

    @Bindable
    public String getEvDtlHostTimeSltSnd() {
        return EvDtlHostTimeSltSnd;
    }

    public void setEvDtlHostTimeSltSnd(String evDtlHostTimeSltSnd) {
        EvDtlHostTimeSltSnd = evDtlHostTimeSltSnd;
        notifyPropertyChanged(BR.evDtlHostTimeSltSnd);
    }

    @Bindable
    public String getEvDtlHostTimeSltTrd() {
        return EvDtlHostTimeSltTrd;
    }

    public void setEvDtlHostTimeSltTrd(String evDtlHostTimeSltTrd) {
        EvDtlHostTimeSltTrd = evDtlHostTimeSltTrd;
        notifyPropertyChanged(BR.evDtlHostTimeSltTrd);
    }

    @Bindable
    public String getEvDtlHostAtdStr() {
        return EvDtlHostAtdStr;
    }

    public void setEvDtlHostAtdStr(String evDtlHostAtdStr) {
        EvDtlHostAtdStr = evDtlHostAtdStr;
        notifyPropertyChanged(BR.evDtlHostAtdStr);
    }

    @Bindable
    public String getEvDtlHostUrlStr() {
        return EvDtlHostUrlStr;
    }

    public void setEvDtlHostUrlStr(String evDtlHostUrlStr) {
        EvDtlHostUrlStr = evDtlHostUrlStr;
        notifyPropertyChanged(BR.evDtlHostUrlStr);
    }

    @Bindable
    public String getEvDtlHostNoteStr() {
        return EvDtlHostNoteStr;
    }

    public void setEvDtlHostNoteStr(String evDtlHostNoteStr) {
        EvDtlHostNoteStr = evDtlHostNoteStr;
        notifyPropertyChanged(BR.evDtlHostNoteStr);
    }

    @Bindable
    public boolean isSelectTimeSlot() {
        return isSelectTimeSlot;
    }

    public void setTimeSlotChooseArray(boolean[] timeSlotChooseArray) {
        this.timeSlotChooseArray = timeSlotChooseArray;

    }

}
