package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.messageevent.MessageEventTime;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewVIewModel extends BaseObservable {
    private String eventTitleString;
    private EventCreateNewPresenter presenter;
    private String eventLocationString;
    private String eventAttendeeInfoString;
    private String eventRepeatString;
    private String eventEndRepeatString;
    private String eventStartTimeString;
    private String eventEndTimeString;
    private String eventAlertTimeString;
    private String eventCalendarTypeString;
    private String eventUrlString;
    private String eventNoteString;
    private Event event;

    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMinute;

    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMinute;

    private PickDateFromType pickDateFromType;
    private ObservableField<Boolean> isEventRepeat;
    private boolean isEndTimeChanged = false;



    public EventCreateNewVIewModel(EventCreateNewPresenter presenter) {
        this.presenter = presenter;
        event = new Event();
        isEventRepeat = new ObservableField<>(false);
        EventBus.getDefault().register(this);
    }

    public View.OnClickListener test() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
// ****************************************************

    public View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submit(event);
            }
        };
    }

    public View.OnClickListener pickStartDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDateFromType = PickDateFromType.STARTTIME;
                presenter.pickDate(pickDateFromType);
            }
        };
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence repeats[] = new CharSequence[]{"Never", "EvenyDay", "EveryWeek", "EvenyTwoWeeks","EventMonth","EvenyYear"};
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle("Choose a repeat type");
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setEventRepeatString((String) repeats[i]);
                        if (repeats[i].equals("Daily") || repeats[i].equals("Weekly") || repeats[i].equals("Monthly")){
                            setIsEventRepeat(true);
                            Log.i("choose", (String) repeats[i]);
                        }
                    }
                });
                builder.show();
                //
            }

        };
    }

    public View.OnClickListener chooseCalendarType() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence types[] = new CharSequence[]{"Work", "Private", "Group", "Public"};
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle("Choose a calendar type");
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        setEventCalendarTypeString((String) types[i]);
                    }
                });
                builder.show();
            }
        };
    }



    public View.OnClickListener gotoUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getEventUrlString();
                Log.i("on click",url);
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }

    public enum PickDateFromType{
        STARTTIME, ENDTIME, ENDREPEAT
    }


    public View.OnClickListener pickEndDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDateFromType =PickDateFromType.ENDTIME;
                presenter.pickDate(pickDateFromType);
            }
        };
    }

    public View.OnClickListener pickEndRepeatDate(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDateFromType = PickDateFromType.ENDREPEAT;
                presenter.pickDate(pickDateFromType);
            }
        };
    }

    public View.OnClickListener cancelNewEvent(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.gotoWeekViewCalendar();
            }
        };
    }

    public View.OnClickListener locationPicker(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.pickLocation();
            }
        };
    }

    public View.OnClickListener attendeePicker(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.pickAttendee();
            }
        };
    }


    @Subscribe
    public void getDateChanged(MessageEventDate messageEventDate) {
        switch (pickDateFromType){
            case STARTTIME:
                startYear = messageEventDate.year;
                startMonth = messageEventDate.month;
                startDay = messageEventDate.day;
                break;
            case ENDTIME:
                endYear = messageEventDate.year;
                endMonth = messageEventDate.month;
                endDay = messageEventDate.day;
                break;
            case ENDREPEAT:
                setEventEndRepeatString(getSelectDayString(messageEventDate.year,messageEventDate.month, messageEventDate.day));
                break;
            default:
                break;
        }

    }

    @Subscribe
    public void getLocationChanged(MessageLocation messageLocation){
        setEventLocationString(messageLocation.locatioinString);
    }

    @Subscribe
    public void getTimeChanged(MessageEventTime messageEventTime) {
        if (pickDateFromType == PickDateFromType.STARTTIME) {
            startHour = messageEventTime.hour;
            startMinute = messageEventTime.mintue;
            setEventStartTimeString(getSelectDayTimeString(startYear, startMonth, startDay, startHour, startMinute));
            if (!isEndTimeChanged) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(startYear, startMonth, startDay, startHour + 1, startMinute);
                setEventEndTimeString(getSelectDayTimeString(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
            }
        } else if (pickDateFromType == PickDateFromType.ENDTIME){
            isEndTimeChanged = true;
            endHour = messageEventTime.hour;
            endMinute = messageEventTime.mintue;
            setEventEndTimeString(getSelectDayTimeString(endYear, endMonth, endDay, endHour, endMinute));
        }

    }

    private String getSelectDayString(int year, int month, int day){
        String eventYear = String.valueOf(year);
        String eventMonth = getMonth(month);
        String eventDay = String.valueOf(day);
        return eventDay + " " + eventMonth + " "+eventYear;
    }


    private String getSelectDayTimeString(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        String eventDayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String eventDayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String eventMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String eventHour = String.valueOf(hour);
        String eventMinute = String.valueOf(minute);
        String amOrPm = hour >= 12 ? "PM" : "AM";

        return eventDayOfWeek + " " + eventDayOfMonth + "/" +
                eventMonth + " " + eventHour + ":" + eventMinute + " " + amOrPm;
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


//    ****************************************************************

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public String getEventTitleString() {
        return eventTitleString;
    }


    public void setEventTitleString(String eventTitleString) {
        this.eventTitleString = eventTitleString;
        notifyPropertyChanged(BR.eventTitleString);
    }


    @Bindable
    public String getEventLocationString() {
        return eventLocationString;
    }

    public void setEventLocationString(String eventLocationString) {
        this.eventLocationString = eventLocationString;
        notifyPropertyChanged(BR.eventLocationString);
    }

    @Bindable
    public String getEventAttendeeInfoString() {
        return eventAttendeeInfoString;
    }

    public void setEventAttendeeInfoString(String eventAttendeeInfoString) {
        this.eventAttendeeInfoString = eventAttendeeInfoString;
        notifyPropertyChanged(BR.eventAttendeeInfoString);
    }

    @Bindable
    public String getEventRepeatString() {
        return eventRepeatString;
    }

    public void setEventRepeatString(String eventRepeatString) {
        this.eventRepeatString = eventRepeatString;
        notifyPropertyChanged(BR.eventRepeatString);
    }

    @Bindable
    public String getEventStartTimeString() {
        return eventStartTimeString;
    }

    public void setEventStartTimeString(String eventStartTimeString) {
        this.eventStartTimeString = eventStartTimeString;
        notifyPropertyChanged(BR.eventStartTimeString);
    }

    @Bindable
    public String getEventEndTimeString() {
        return eventEndTimeString;
    }

    public void setEventEndTimeString(String eventEndTimeString) {
        this.eventEndTimeString = eventEndTimeString;
        notifyPropertyChanged(BR.eventEndTimeString);
    }

    @Bindable
    public String getEventAlertTimeString() {
        return eventAlertTimeString;
    }

    public void setEventAlertTimeString(String eventAlertTimeString) {
        this.eventAlertTimeString = eventAlertTimeString;
        notifyPropertyChanged(BR.eventAlertTimeString);
    }

    @Bindable
    public String getEventCalendarTypeString() {
        return eventCalendarTypeString;
    }

    public void setEventCalendarTypeString(String eventCalendarTypeString) {
        this.eventCalendarTypeString = eventCalendarTypeString;
        notifyPropertyChanged(BR.eventCalendarTypeString);
    }

    @Bindable
    public String getEventUrlString() {
        return eventUrlString;
    }

    public void setEventUrlString(String eventUrlString) {
        this.eventUrlString = eventUrlString;
        notifyPropertyChanged(BR.eventUrlString);
    }

    @Bindable
    public String getEventNoteString() {
        return eventNoteString;
    }

    public void setEventNoteString(String eventNoteString) {
        this.eventNoteString = eventNoteString;
        notifyPropertyChanged(BR.eventNoteString);
    }

    @Bindable
    public String getEventEndRepeatString() {
        return eventEndRepeatString;
    }

    public void setEventEndRepeatString(String eventEndRepeatString) {
        this.eventEndRepeatString = eventEndRepeatString;
        notifyPropertyChanged(BR.eventEndRepeatString);
    }

    @Bindable
    public Boolean getIsEventRepeat() {
        return isEventRepeat.get();
    }

    public void setIsEventRepeat(Boolean value) {
        this.isEventRepeat.set(value);
        notifyPropertyChanged(BR.isEventRepeat);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }
}
