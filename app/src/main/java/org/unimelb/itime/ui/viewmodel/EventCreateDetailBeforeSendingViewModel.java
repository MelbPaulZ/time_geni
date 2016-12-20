package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingViewModel extends CommonViewModel {
    private Event newEvDtlEvent;
    private ObservableField<Boolean> evDtlIsEventRepeat ;
    private EventCreateDetailBeforeSendingViewModel viewModel;
    private CharSequence alertTimes[] = null;
    private EventCreateDetailBeforeSendingMvpView mvpView;
    private int tempYear,tempMonth,tempDay,tempHour,tempMin;
    private EventCreateDetailBeforeSendingPresenter presenter;
    private ObservableField<Boolean> isEndRepeatChange;
    private ObservableField<Boolean> isAllDay;
    private PickerTask currentTask;
    private int startVisibility, endVisibility;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private int editYear, editMonth, editDay, editHour, editMinute;
    public enum PickerTask{
        START_TIME, END_TIME, END_REPEAT
    }

    private UserUtil userUtil;
    private CalendarUtil calendarUtil;
    private EventManager eventManager;

    public EventCreateDetailBeforeSendingViewModel(EventCreateDetailBeforeSendingPresenter presenter) {
        this.presenter = presenter;
        eventManager = EventManager.getInstance(getContext());
        newEvDtlEvent = eventManager.getCurrentEvent();
        evDtlIsEventRepeat = new ObservableField<>(false);
        isEndRepeatChange = new ObservableField<>(false);
        this.viewModel = this;
        isAllDay = new ObservableField<>(false);
        mvpView = presenter.getView();
        initDialog();
        userUtil = UserUtil.getInstance(getContext());
        calendarUtil = CalendarUtil.getInstance(getContext());
    }

    private void initDialog(){
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                switch (currentTask){
                    case END_TIME:
                        onEndDateSelected(year, monthOfYear, dayOfMonth);
                        break;
                    case START_TIME:
                        onStartDateSelected(year, monthOfYear, dayOfMonth);
                        break;
                    case END_REPEAT:
                        onEndRepeatSelected(year, monthOfYear, dayOfMonth);
                        break;
                }
            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (currentTask){
                    case END_TIME:
                        onEndTimeSelected(hourOfDay, minute);
                        break;
                    case START_TIME:
                        onStartTimeSelected(hourOfDay, minute);
                        break;
                }
            }
        };
    }


    private void onEndDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(newEvDtlEvent.getEndTime());
        timePickerDialog = new TimePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        editYear = year;
        editMonth = monthOfYear;
        editDay = dayOfMonth;
    }

    private void onStartDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(newEvDtlEvent.getStartTime());
        timePickerDialog = new TimePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        editYear = year;
        editMonth = monthOfYear;
        editDay = dayOfMonth;
    }

    private void onEndRepeatSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        newEvDtlEvent.getRule().setUntil(c.getTime());
        newEvDtlEvent.setRecurrence(newEvDtlEvent.getRule().getRecurrence());
        notifyPropertyChanged(BR.newEvDtlEvent);
    }

    private void onEndTimeSelected(int hour, int minute){
        Calendar c = Calendar.getInstance();
        editHour = hour;
        editMinute = minute;
        c.set(editYear, editMonth, editDay, editHour, editMinute);
        newEvDtlEvent.setEndTime(c.getTimeInMillis());
        notifyPropertyChanged(BR.newEvDtlEvent);
    }

    private void onStartTimeSelected(int hour, int minute){
        Calendar c = Calendar.getInstance();
        editHour = hour;
        editMinute = minute;
        c.set(editYear, editMonth, editDay, editHour, editMinute);
        newEvDtlEvent.setStartTime(c.getTimeInMillis());
        notifyPropertyChanged(BR.newEvDtlEvent);
    }

    @Bindable
    public boolean getIsAllDay() {
        return isAllDay.get();
    }


    public void setIsAllDay(ObservableField<Boolean> isAllDay) {
        this.isAllDay.set(isAllDay.get());
        if (this.isAllDay.get()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newEvDtlEvent.getStartTime());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            newEvDtlEvent.setStartTime(calendar.getTimeInMillis());
            newEvDtlEvent.setEndTime(newEvDtlEvent.getStartTime() + 3600 * 1000 * 24);
            setNewEvDtlEvent(newEvDtlEvent);
        }else{
            Calendar calendar = Calendar.getInstance();
            newEvDtlEvent.setStartTime(calendar.getTimeInMillis());
            newEvDtlEvent.setEndTime(calendar.getTimeInMillis() + 3600 * 1000);
            setNewEvDtlEvent(newEvDtlEvent);
        }
        notifyPropertyChanged(BR.isAllDay);
    }


    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] repeats;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_repeat));
                repeats = EventUtil.getRepeats(getContext(), newEvDtlEvent);
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set event recurrence
                        EventUtil.changeEventFrequency(newEvDtlEvent, i);
                        setNewEvDtlEvent(newEvDtlEvent);
                    }
                });
                builder.show();
            }

        };
    }

    public String getRepeatString(Event event){
        return EventUtil.getRepeatString(getContext(), event);
    }

    public View.OnClickListener pickAlertTime(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_alert_time));
                alertTimes = EventUtil.getAlertTimes(getContext());
                builder.setItems(alertTimes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newEvDtlEvent.setReminder(i);
                        setNewEvDtlEvent(newEvDtlEvent);
                    }
                });
                builder.show();
            }
        };
    }


    public View.OnClickListener pickPhoto(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.pickPhoto(getContext().getString(R.string.tag_create_event_before_sending));
                }
            }
        };
    }


    public View.OnClickListener onClickProposedTimeslots(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickProposedTimeslots();
                }
            }
        };
    }

    public View.OnClickListener pickInvitees(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.pickInvitees();
                }
            }
        };
    }

    public void setPhotos(ArrayList<String> photos){
        newEvDtlEvent.setPhoto(EventUtil.fromStringToPhotoUrlList(getContext(), photos));
        setNewEvDtlEvent(newEvDtlEvent);
    }

    @Bindable
    public Context getContext(){
        return presenter.getContext();
    }

    public View.OnClickListener onClickSend(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newEvDtlEvent.getTitle().equals("")){
                    newEvDtlEvent.setTitle(getContext().getString(R.string.new_event));
                }

                // pending Timeslots filtered out timeslots which not is not chosed by host
                List<Timeslot> pendingTimeslots = new ArrayList<>();
                for (Timeslot timeSlot : newEvDtlEvent.getTimeslot()){
                    if (timeSlot.getStatus().equals(Timeslot.STATUS_PENDING)){
                        pendingTimeslots.add(timeSlot);
                    }
                }

                // set event start time and end time, using the latest timeslot
                Timeslot displayTimeslot = TimeSlotUtil.getLatestTimeSlot(pendingTimeslots);
                if (displayTimeslot!=null) {
                    newEvDtlEvent.setStartTime(displayTimeslot.getStartTime());
                    newEvDtlEvent.setEndTime(displayTimeslot.getEndTime());
                }

                newEvDtlEvent.setRecurrence(newEvDtlEvent.getRule().getRecurrence());
                newEvDtlEvent.setTimeslot(pendingTimeslots);
                newEvDtlEvent.setHostUserUid(UserUtil.getInstance(getContext()).getUserUid());
                EventUtil.addSelfInInvitee(getContext(), newEvDtlEvent);
                if(!newEvDtlEvent.hasPhoto()){
                    newEvDtlEvent.setPhoto(new ArrayList<PhotoUrl>());
                }

                // todo: need to check whether host is in invitees
                if (newEvDtlEvent.getInvitee().size()>1){
                    newEvDtlEvent.setEventType("group");
                }

                // todo: delete it after finishing calendar
                newEvDtlEvent.setCalendarUid(userUtil.getUserUid());
                newEvDtlEvent.setRecurringEventUid(newEvDtlEvent.getEventUid());
                newEvDtlEvent.setRecurringEventId(newEvDtlEvent.getEventUid());
                newEvDtlEvent.setStatus("pending");

                presenter.insertNewEventToServer(newEvDtlEvent);

                eventManager.setCurrentEvent(newEvDtlEvent);
                if (mvpView!=null){
                    mvpView.onClickSend();
                }
            }
        };
    }

    // TODO: 11/12/2016 implement starttime, endtime, endRepeat change
    public View.OnClickListener onChangeTime(final PickerTask task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask = task;
                Calendar c = Calendar.getInstance();
                switch (currentTask){
                    case END_REPEAT:
                        if (newEvDtlEvent.getRule().getUntil()!=null){
                            c.setTime(newEvDtlEvent.getRule().getUntil());
                        }
                        break;
                    case END_TIME:
                        c.setTimeInMillis(newEvDtlEvent.getEndTime());
                        break;
                    case START_TIME:
                        c.setTimeInMillis(newEvDtlEvent.getStartTime());
                }
                datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
    }

    public View.OnClickListener onClickCalendarType(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final CharSequence types[] = EventUtil.getCalendarTypes(getContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                    builder.setTitle("Choose a calendar type");
                    builder.setItems(types, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            newEvDtlEvent.setCalendarUid(calendarUtil.getCalendar().get(i).getCalendarUid());
                            viewModel.setNewEvDtlEvent(newEvDtlEvent);
                        }
                    });
                    builder.show();
                }
        };
    }

    public View.OnClickListener onClickCancel(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickCancel();
                }
            }
        };
    }

    public View.OnClickListener changeLocation(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.changeLocation();
                }
            }
        };
    }

//    *********************************************************************


    @Bindable
    public Event getNewEvDtlEvent() {
        return newEvDtlEvent;
    }

    public void setNewEvDtlEvent(Event newEvDtlEvent) {
        this.newEvDtlEvent = newEvDtlEvent;
        notifyPropertyChanged(BR.newEvDtlEvent);
        notifyPropertyChanged(BR.startVisibility);
        notifyPropertyChanged(BR.endVisibility);
    }

    @Bindable
    public boolean getEvDtlIsEventRepeat() {
        return evDtlIsEventRepeat.get();
    }

    public void setIsEventRepeat(boolean isEventRepeat) {
        this.evDtlIsEventRepeat.set(isEventRepeat);
        notifyPropertyChanged(BR.evDtlIsEventRepeat);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }

    @Bindable
    public boolean isEndRepeatChange() {
        return isEndRepeatChange.get();
    }

    public void setEndRepeatChange(boolean endRepeatChange) {
        isEndRepeatChange.set(endRepeatChange);
        notifyPropertyChanged(BR.endRepeatChange);
    }

    @Bindable
    public int getStartVisibility() {
        if (!EventUtil.hasOtherInviteeExceptSelf(getContext(), newEvDtlEvent)){
            startVisibility =  View.VISIBLE;
        }else{
            startVisibility =  View.GONE;
        }
        return startVisibility;
    }

    public void setStartVisibility(int startVisibility) {
        this.startVisibility = startVisibility;
        notifyPropertyChanged(BR.startVisibility);
    }


    @Bindable
    public int getEndVisibility() {
        return getStartVisibility();
    }

    public void setEndVisibility(int endVisibility) {
        this.endVisibility = endVisibility;
        notifyPropertyChanged(BR.endVisibility);
    }

    //    *******************************************************************************************************


}
