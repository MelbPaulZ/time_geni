package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import me.tatarka.bindingcollectionadapter.ItemView;
import com.android.databinding.library.baseAdapters.BR;
/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends CommonViewModel {

    private Event eventEditViewEvent;
    private EventEditPresenter presenter;
    private ObservableField<Boolean> editEventIsRepeat = new ObservableField<>();
    private EventEditMvpView mvpView;
    private ObservableField<Boolean> isAllDayEvent= new ObservableField<>();
    private PickerTask currentTask;
    private int startTimeVisibility, endTimeVisibility;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private int editYear, editMonth, editDay, editHour, editMinute;

    private List<Timeslot> timeslotList = new ArrayList<>();
    private ItemView itemView = ItemView.of(BR.timeslot, R.layout.timeslot_listview_show);

    public enum PickerTask{
        START_TIME, END_TIME, END_REPEAT
    }


    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.presenter = eventEditPresenter;
        mvpView = presenter.getView();
        editEventIsRepeat = new ObservableField<>(false); // TODO: 11/12/2016 change this and isAlldayEvent later.... needs to be bind with event
        isAllDayEvent = new ObservableField<>(false);
        initDialog();
    }


    private void initDialog(){
        Calendar c = Calendar.getInstance();
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
//
//        datePickerDialog = new DatePickerDialog(getContext(),
//                AlertDialog.THEME_HOLO_LIGHT, dateSetListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
//
//        timePickerDialog = new TimePickerDialog(getContext(),
//                AlertDialog.THEME_HOLO_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
    }


    private void onEndDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(eventEditViewEvent.getEndTime());
        timePickerDialog = new TimePickerDialog(getContext(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        editYear = year;
        editMonth = monthOfYear;
        editDay = dayOfMonth;
    }

    private void onStartDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(eventEditViewEvent.getStartTime());
        timePickerDialog = new TimePickerDialog(getContext(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        editYear = year;
        editMonth = monthOfYear;
        editDay = dayOfMonth;
    }

    private void onEndRepeatSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        eventEditViewEvent.getRule().setUntil(c.getTime());
        eventEditViewEvent.setRecurrence(eventEditViewEvent.getRule().getRecurrence());
    }

    private void onEndTimeSelected(int hour, int minute){

    }

    private void onStartTimeSelected(int hour, int minute){

    }



    public Context getContext(){
        return presenter.getContext();
    }


    // click cancel button on edit event page
    public View.OnClickListener cancelEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                        mvpView.toHostEventDetail();
                }
            }
        };
    }

    // click done btn
    public View.OnClickListener finishEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // popup alertDialog to choose whether change all or just one
                if (EventManager.getInstance().getCurrentEvent().getRecurrence().length>0){
                    // the event is repeat event
                    final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View root = inflater.inflate(R.layout.repeat_event_change_alert_view, null);
                    alertDialog.setView(root);
                    alertDialog.setTitle(getContext().getString(R.string.change_all_repeat_or_just_this_event));
                    alertDialog.show();

                    TextView button_change_all = (TextView) root.findViewById(R.id.alert_message_change_all_button);
                    button_change_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeAllEvent(eventEditViewEvent);
                            alertDialog.dismiss();
                        }
                    });

                    TextView button_only_this = (TextView) root.findViewById(R.id.alert_message_only_this_event_button);
                    button_only_this.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeOnlyThisEvent(eventEditViewEvent);
                            alertDialog.dismiss();

                        }
                    });

                }else {
                    if (mvpView!=null){
                        // set event type
                        EventUtil.addSelfInInvitee(getContext(), eventEditViewEvent);
                        eventEditViewEvent.setEventType(EventUtil.getEventType(eventEditViewEvent, UserUtil.getUserUid()));
                        eventEditViewEvent.setRecurrence(eventEditViewEvent.getRule().getRecurrence()); // set the repeat string
                        eventEditViewEvent.setStatus("pending");
                        presenter.updateEvent(eventEditViewEvent);
                        // this if might change later, because the host can be kicked??????
//                        if (eventEditViewEvent.getEventType().equals(getContext().getString(R.string.group))) {
//                            mvpView.toHostEventDetail(eventEditViewEvent);
//                        }else{
//                            mvpView.toSoloEventDetail(eventEditViewEvent);
//                        }
                    }
                }
            }
        };
    }

    private void changeAllEvent(Event event){
        if (mvpView!=null){
            // set event type
            event.setRecurrence(event.getRule().getRecurrence()); // set the repeat string
            EventUtil.addSelfInInvitee(getContext(), event);
            event.setEventType(EventUtil.getEventType(event, UserUtil.getUserUid()));

            // next find original event(the first event of repeat event)
            Event orgEvent = EventManager.getInstance().findOrgByUUID(event.getEventUid());
            Event copyEventSendToServer = EventManager.getInstance().copyCurrentEvent(event);
            copyEventSendToServer.setStartTime(orgEvent.getStartTime()); // set the starttime and endtime as origin event
            copyEventSendToServer.setEndTime(orgEvent.getEndTime());

            presenter.updateEvent(copyEventSendToServer);
            // this if might change later, because the host can be kicked??????
//            if (event.getEventType().equals(getContext().getString(R.string.group))) {
//                mvpView.toHostEventDetail(event);
//            }else{
//                mvpView.toSoloEventDetail(event);
//            }
        }
    }

    private void changeOnlyThisEvent(Event event){
        if (mvpView!=null){
            // set event type
            event.setRecurrence(event.getRule().getRecurrence()); // set the repeat string
            EventUtil.addSelfInInvitee(getContext(), event);
            event.setEventType(EventUtil.getEventType(event, UserUtil.getUserUid()));

            // next find original event(the first event of repeat event)
            Event orgEvent = EventManager.getInstance().findOrgByUUID(event.getEventUid());
            orgEvent.getRule().addEXDate(new Date(event.getStartTime()));
            orgEvent.setRecurrence(orgEvent.getRule().getRecurrence());
            // here change the event as a new event
            EventUtil.regenerateRelatedUid(event);
            event.setRecurringEventUid(orgEvent.getEventUid());
            event.setRecurringEventId(orgEvent.getEventId());
            presenter.updateOnlyThisEvent(orgEvent,event);
            // this if might change later, because the host can be kicked??????
//            if (event.getEventType().equals(getContext().getString(R.string.group))) {
//                mvpView.toHostEventDetail(event);
//            }else{
//                mvpView.toSoloEventDetail(event);
//            }
        }
    }


    public View.OnClickListener editTimeSlot(){
        // chuan can shu next to do
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.toTimeSlotView(eventEditViewEvent);// tiao zhuan wei zhi
                }
            }
        };
    }



    public View.OnClickListener changeLocation(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.changeLocation();
                }
            }
        };
    }



    public String getRepeatString(Event event){
        return EventUtil.getRepeatString(getContext(), event);
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] repeats;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_repeat));
                repeats = EventUtil.getRepeats(getContext(), eventEditViewEvent);
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set event recurrence
                        EventUtil.changeEventFrequency(eventEditViewEvent, i);
                        setEventEditViewEvent(eventEditViewEvent);
                    }
                });
                builder.show();
            }

        };
    }

    public View.OnClickListener onChooseAlertTime(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] alertTimes;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_alert_time));
                alertTimes = EventUtil.getAlertTimes(getContext());
                builder.setItems(alertTimes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventEditViewEvent.setReminder(i);
                        setEventEditViewEvent(eventEditViewEvent);
                    }
                });
                builder.show();
            }
        };
    }

    @Bindable
    public boolean getIsAllDayEvent() {
        return isAllDayEvent.get();
    }


    public void setIsAllDayEvent(ObservableField<Boolean> isAllDayEvent) {
        this.isAllDayEvent.set(isAllDayEvent.get());
        if (this.isAllDayEvent.get()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(eventEditViewEvent.getStartTime());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            eventEditViewEvent.setStartTime(calendar.getTimeInMillis());
            eventEditViewEvent.setEndTime(eventEditViewEvent.getStartTime() + 3600 * 1000 * 24);
            setEventEditViewEvent(eventEditViewEvent);
        }else{
            Calendar calendar = Calendar.getInstance();
            eventEditViewEvent.setStartTime(calendar.getTimeInMillis());
            eventEditViewEvent.setEndTime(calendar.getTimeInMillis() + 3600 * 1000);
            setEventEditViewEvent(eventEditViewEvent);
        }
//        notifyPropertyChanged(BR.isAllDayEvent);
    }

    public View.OnClickListener onChooseCalendarType(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] calendarType;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_alert_time));
                calendarType = EventUtil.getCalendarTypes(getContext());
                builder.setItems(calendarType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventEditViewEvent.setCalendarUid((String) calendarType[i]);
                        setEventEditViewEvent(eventEditViewEvent);
                    }
                });
                builder.show();
            }
        };
    }

    public View.OnClickListener toInviteePicker(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.toInviteePicker(eventEditViewEvent);
                }
            }
        };
    }

    public View.OnClickListener onClickDelete(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteEvent(eventEditViewEvent);
            }
        };
    }

    public View.OnClickListener pickPhoto(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.toPhotoPicker();
                }
            }
        };
    }

    public void setPhotos(ArrayList<String> photos){
        eventEditViewEvent.setPhoto(EventUtil.fromStringToPhotoUrlList(photos));
        setEventEditViewEvent(eventEditViewEvent);
    }

    @Bindable
    public Event getEventEditViewEvent() {
        return eventEditViewEvent;
    }

    public void setEventEditViewEvent(Event eventEditViewEvent) {
        this.eventEditViewEvent = eventEditViewEvent;
        if (eventEditViewEvent.hasTimeslots()) {
            timeslotList = eventEditViewEvent.getTimeslot();
        }
        notifyPropertyChanged(BR.eventEditViewEvent);
        notifyPropertyChanged(BR.startTimeVisibility);
        notifyPropertyChanged(BR.endTimeVisibility);
        notifyPropertyChanged(BR.editEventIsRepeat);
        notifyPropertyChanged(BR.timeslotList);
    }

    @Bindable
    public Boolean getEditEventIsRepeat() {
        if (eventEditViewEvent.hasRecurrence()){
            editEventIsRepeat.set(true);
        }else{
            editEventIsRepeat.set(false);
        }
        return editEventIsRepeat.get();
    }

    public void setEditEventIsRepeat(Boolean editEventIsRepeat) {
        this.editEventIsRepeat.set(editEventIsRepeat);
        notifyPropertyChanged(BR.editEventIsRepeat);
    }

    // TODO: 11/12/2016 implement starttime, endtime, endRepeat change
    public View.OnClickListener onChangeTime(final PickerTask task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask = task;
                datePickerDialog.show();
            }
        };
    }


    @Bindable
    public int getStartTimeVisibility() {
        if (!EventUtil.hasOtherInviteeExceptSelf(eventEditViewEvent)){
            startTimeVisibility =  View.VISIBLE;
        }else{
            startTimeVisibility =  View.GONE;
        }
        return startTimeVisibility;
    }

    public void setStartTimeVisibility(int startTimeVisibility) {
        this.startTimeVisibility = startTimeVisibility;
        notifyPropertyChanged(BR.startTimeVisibility);
    }

    @Bindable
    public int getEndTimeVisibility() {
        return getStartTimeVisibility();
    }

    public void setEndTimeVisibility(int endTimeVisibility) {
        this.endTimeVisibility = endTimeVisibility;
        notifyPropertyChanged(BR.endTimeVisibility);
    }

    @Bindable
    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    @Bindable
    public ItemView getItemView() {
        return itemView;
    }
}
