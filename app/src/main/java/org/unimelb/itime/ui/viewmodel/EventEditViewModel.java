package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.fesky.library.widget.ios.ActionSheetDialog;
import me.tatarka.bindingcollectionadapter.ItemView;

import static org.unimelb.itime.R.string.repeat;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends CommonViewModel {

    private Event eventEditViewEvent;
    private EventCommonPresenter<EventEditMvpView> presenter;
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
    private EventManager eventManager;
    private boolean isEndTimeChanged = false;


    public EventEditViewModel(EventCommonPresenter<EventEditMvpView> eventEditPresenter) {
        this.presenter = eventEditPresenter;
        eventManager = EventManager.getInstance(getContext());
        mvpView = presenter.getView();
        editEventIsRepeat = new ObservableField<>(false); // TODO: 11/12/2016 change this and isAlldayEvent later.... needs to be bind with event
        isAllDayEvent = new ObservableField<>(false);
        initDialog();
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

    /**
     * check the focus of email edit text
     * @return
     */
    public View.OnFocusChangeListener onEditFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showKeyBoard((EditText) view);
                }else{
                    closeKeyBoard((EditText) view);
                }
            }
        };
    }


    private void onEndDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(eventEditViewEvent.getEndTime());
        timePickerDialog = new TimePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        editYear = year;
        editMonth = monthOfYear;
        editDay = dayOfMonth;
    }

    private void onStartDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(eventEditViewEvent.getStartTime());
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
        eventEditViewEvent.getRule().setUntil(c.getTime());
        eventEditViewEvent.setRecurrence(eventEditViewEvent.getRule().getRecurrence());
        notifyPropertyChanged(BR.eventEditViewEvent);
    }

    private void onEndTimeSelected(int hour, int minute){
        Calendar c = Calendar.getInstance();
        editHour = hour;
        editMinute = minute;
        c.set(editYear, editMonth, editDay, editHour, editMinute);
        eventEditViewEvent.setEndTime(c.getTimeInMillis());
        isEndTimeChanged = true;
        notifyPropertyChanged(BR.eventEditViewEvent);
    }

    private void onStartTimeSelected(int hour, int minute){
        Calendar c = Calendar.getInstance();
        editHour = hour;
        editMinute = minute;
        c.set(editYear, editMonth, editDay, editHour, editMinute);
        eventEditViewEvent.setStartTime(c.getTimeInMillis());
        if (!isEndTimeChanged){
            eventEditViewEvent.setEndTime(c.getTimeInMillis() + 60 * 60 * 1000);
        }
        notifyPropertyChanged(BR.eventEditViewEvent);
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
                List<Timeslot> timeslots = EventUtil.getTimeslotFromPending(getContext(), eventEditViewEvent);
                eventEditViewEvent.setTimeslot(timeslots);

                if (eventManager.getCurrentEvent().getRecurrence().length>0){
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
                            eventEditViewEvent.setEventType(EventUtil.getEventType(eventEditViewEvent, UserUtil.getInstance(getContext()).getUserUid()));
                            changeAllEvent(eventEditViewEvent);
                            alertDialog.dismiss();
                        }
                    });

                    TextView button_only_this = (TextView) root.findViewById(R.id.alert_message_only_this_event_button);
                    button_only_this.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventEditViewEvent.setEventType(EventUtil.getEventType(eventEditViewEvent, UserUtil.getInstance(getContext()).getUserUid()));
                            changeOnlyThisEvent(eventEditViewEvent);
                            alertDialog.dismiss();

                        }
                    });

                }else {
                    // set event type
                    EventUtil.addSelfInInvitee(getContext(), eventEditViewEvent);
                    eventEditViewEvent.setEventType(EventUtil.getEventType(eventEditViewEvent, UserUtil.getInstance(getContext()).getUserUid()));

                    // if solo, need to manually set status confirmed, otherwise server do it
                    if (eventEditViewEvent.getEventType().equals(Event.TYPE_SOLO)){
                        eventEditViewEvent.setStatus(Event.STATUS_CONFIRMED);
                    }else {
                        eventEditViewEvent.setStatus(Event.STATUS_PENDING);
                    }

                    Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
                    presenter.updateEvent(eventEditViewEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                    // this if might change later, because the host can be kicked??????
                }
            }
        };
    }

    // TODO: test this change all
    private void changeAllEvent(Event event){
        Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
        presenter.updateEvent(event, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
    }

    private void changeOnlyThisEvent(Event event){
        event.setRecurrence(event.getRule().getRecurrence()); // set the repeat string
        EventUtil.addSelfInInvitee(getContext(), event);
        event.setEventType(EventUtil.getEventType(event, UserUtil.getInstance(getContext()).getUserUid()));

        if (!EventUtil.isGroupEvent(getContext(), event)){
            event.setStatus(Event.STATUS_CONFIRMED);
        }
        // here change the event as a new event
        Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
        presenter.updateEvent(event, EventCommonPresenter.UPDATE_THIS, orgEvent.getStartTime());

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
        notifyPropertyChanged(BR.isAllDayEvent);
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
                        eventEditViewEvent.setCalendarUid(CalendarUtil.getInstance(getContext()).getCalendar().get(i).getCalendarUid());
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
                Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
                if (EventUtil.isEventRepeat(eventEditViewEvent)) {
                    // repeat event delete
                    if (eventEditViewEvent.getRecurrence().length==0) {
                        presenter.deleteEvent(eventEditViewEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                    }else{
                        repeatDeletePopup(orgEvent);
                    }
                }else{
                    // none repeat event delete
                    presenter.deleteEvent(eventEditViewEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                }
            }
        };
    }

    private void repeatDeletePopup(final Event orgEvent){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getContext())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getContext().getString(R.string.event_delete_repeat_text1), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                presenter.deleteEvent(eventEditViewEvent, EventCommonPresenter.UPDATE_THIS, orgEvent.getStartTime());
                            }
                        })
                .addSheetItem(getContext().getString(R.string.event_delete_repeat_text2), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int i) {
                                presenter.deleteEvent(eventEditViewEvent, EventCommonPresenter.UPDATE_FOLLOWING, orgEvent.getStartTime());
                            }
                        });
        actionSheetDialog.show();
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
        eventEditViewEvent.setPhoto(EventUtil.fromStringToPhotoUrlList(getContext(), photos));
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
                Calendar c = Calendar.getInstance();
                switch (currentTask){
                    case END_REPEAT:
                        if (eventEditViewEvent.getRule().getUntil()!=null){
                           c.setTime(eventEditViewEvent.getRule().getUntil());
                        }
                        break;
                    case END_TIME:
                        c.setTimeInMillis(eventEditViewEvent.getEndTime());
                        break;
                    case START_TIME:
                        c.setTimeInMillis(eventEditViewEvent.getStartTime());
                }
                datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
    }


    @Bindable
    public int getStartTimeVisibility() {
        if (!EventUtil.hasOtherInviteeExceptSelf(getContext(), eventEditViewEvent)){
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
