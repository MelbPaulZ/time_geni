package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewVIewModel extends CommonViewModel {
    private EventCreateNewPresenter presenter;
    private EventCreateNewMvpView mvpView;
    private Event event;

    private String tag;

    private ObservableField<Boolean> isEventRepeat;
    private boolean isEndTimeChanged = false;

    private CharSequence repeats[] = null;
    EventCreateNewVIewModel viewModel;
    private boolean isEndRepeatChanged = false;
    private ObservableField<Boolean> isAllDay;

    private int tempYear,tempMonth,tempDay,tempHour,tempMin;
    private static final int CHANGE_STARTTIME = 0;
    private static final int CHANGE_ENDTIME = 1;
    private static final int CHANGE_ENDREPEAT_TIME = 2;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private PickerTask currentTask;
    private int editYear, editMonth, editDay, editHour, editMinute;

    public enum PickerTask{
        START_TIME, END_TIME, END_REPEAT
    }

    private final String TAG = "EventCreateNewViewModel";
    private EventManager eventManager;

    public EventCreateNewVIewModel(EventCreateNewPresenter presenter) {
        this.presenter = presenter;
        eventManager = EventManager.getInstance(getContext());
        mvpView = presenter.getView();
        init();
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

    private void onEndDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getEndTime());
        timePickerDialog = new TimePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_LIGHT, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        editYear = year;
        editMonth = monthOfYear;
        editDay = dayOfMonth;
    }

    private void onStartDateSelected(int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getStartTime());
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
        event.getRule().setUntil(c.getTime());
        event.setRecurrence(event.getRule().getRecurrence());
        notifyPropertyChanged(BR.event);
    }

    private void onEndTimeSelected(int hour, int minute){
        Calendar c = Calendar.getInstance();
        editHour = hour;
        editMinute = minute;
        c.set(editYear, editMonth, editDay, editHour, editMinute);
        event.setEndTime(c.getTimeInMillis());
        isEndTimeChanged = true;
        notifyPropertyChanged(BR.event);
    }

    private void onStartTimeSelected(int hour, int minute){
        Calendar c = Calendar.getInstance();
        editHour = hour;
        editMinute = minute;
        c.set(editYear, editMonth, editDay, editHour, editMinute);
        event.setStartTime(c.getTimeInMillis());
        if (!isEndTimeChanged){
            event.setEndTime(c.getTimeInMillis() + 24 * 60 * 60 * 1000);
        }
        notifyPropertyChanged(BR.event);
    }

    public View.OnClickListener onChangeTime(final PickerTask task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask = task;
                Calendar c = Calendar.getInstance();
                switch (currentTask){
                    case END_REPEAT:
                        if (event.getRule().getUntil()!=null){
                            c.setTime(event.getRule().getUntil());
                        }
                        break;
                    case END_TIME:
                        c.setTimeInMillis(event.getEndTime());
                        break;
                    case START_TIME:
                        c.setTimeInMillis(event.getStartTime());
                }
                datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
    }


    public Context getContext() {
        return presenter.getContext();
    }

    public void init() {
        isEventRepeat = new ObservableField<>(false);
        isAllDay = new ObservableField<>(false);
        tag = presenter.getContext().getString(R.string.tag_create_event);
        this.viewModel = this;
        setEvent(eventManager.getCurrentEvent());
        repeats = EventUtil.getRepeats(getContext(), event);
    }


    public void updateRepeats(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        String dayOfWeek = EventUtil.getDayOfWeekFull(getContext(), calendar.get(Calendar.DAY_OF_WEEK));
        repeats[2] = String.format(getContext().getString(R.string.repeat_everyweek), dayOfWeek);
    }


    public void setPhotos(ArrayList<String> urls){
        event.setPhoto(EventUtil.fromStringToPhotoUrlList(getContext(), urls));
        setEvent(event);
    }


    @BindingAdapter({"android:src"})
    public static void loadBackground(ImageView view, String photoUrl1){
        File f = new File(photoUrl1);
        int size = DensityUtil.dip2px(view.getContext(), 40);
        Picasso.with(view.getContext())
                .load(f)
                .resize(size ,size)
                .centerCrop()
                .into(view);
    }



    public String getRepeatString(Event event){
        return EventUtil.getRepeatString(getContext(), event);
    }

    public View.OnClickListener onClickRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] repeats;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_repeat));
                repeats = EventUtil.getRepeats(getContext(), event);
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set event recurrence
                        EventUtil.changeEventFrequency(event, i);
                        setEvent(event);
                    }
                });
                builder.show();
            }
        };
    }

    public View.OnClickListener chooseCalendarType() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence types[] = EventUtil.getCalendarTypes(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle("Choose a calendar type");
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        event.setCalendarUid(CalendarUtil.getInstance(getContext()).getCalendar().get(i).getCalendarUid());
                        viewModel.setEvent(event);
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
                String url = event.getUrl();
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }


//    public boolean inviteeVisibility(Switch view, Event event){
//        if (view.isChecked()){
//            event.setInviteeVisibility(1);
//            return true;
//        }else{
//            event.setInviteeVisibility(0);
//            return false;
//        }
//    }
    @BindingAdapter({"bind:check"})
    public static void inviteeVisibilityCheck(final Switch view, final Event event){
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    event.setInviteeVisibility(1);
                    Log.i("see each other", "inviteeVisibilityCheck: " + event.getInviteeVisibility());
                }else{
                    event.setInviteeVisibility(0);
                    Log.i("see each other", "inviteeVisibilityCheck: " + event.getInviteeVisibility());
                }
            }
        });
    }


    // click cancel button
    public View.OnClickListener onClickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.gotoWeekViewCalendar();
                }
            }
        };
    }

    public View.OnClickListener onClickLocation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null){
                    mvpView.pickLocation();
                }
            }
        };
    }

    // click done btn
    public View.OnClickListener onClickDone() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.getTitle().equals("")) {
                    event.setTitle(presenter.getContext().getString(R.string.new_event));
                }
                event.setRecurrence(event.getRule().getRecurrence());
                EventUtil.addSelfInInvitee(getContext(), event);
                EventUtil.addSoloEventBasicInfo(getContext(), event);
                eventManager.setCurrentEvent(event);
                presenter.insertNewEventToServer(event);
                if (mvpView!=null){
                    mvpView.toCreateSoloEvent();
                }
            }
        };
    }

    public View.OnClickListener onClickInvitee() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.pickInvitee();
                }
            }
        };
    }


    public View.OnClickListener pickPhoto(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = getContext().getString(R.string.tag_create_event);
                if (presenter.getView() != null){
                    presenter.getView().pickPhoto(tag);
                }
            }
        };
    }

    public View.OnClickListener pickAlertTime(){
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
                        event.setReminder(i);
                        setEvent(event);
                    }
                });
                builder.show();
            }
        };
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
    public Boolean getIsEventRepeat() {
        return isEventRepeat.get();
    }

    public void setIsEventRepeat(Boolean value) {
        this.isEventRepeat.set(value);
        notifyPropertyChanged(BR.isEventRepeat);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }


    @Bindable
    public boolean getIsAllDay() {
        return isAllDay.get();
    }


    public void setIsAllDay(ObservableField<Boolean> isAllDay) {
        this.isAllDay.set(isAllDay.get());
        if (this.isAllDay.get()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(event.getStartTime());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            event.setStartTime(calendar.getTimeInMillis());
            event.setEndTime(event.getStartTime() + 3600 * 1000 * 24);
            setEvent(event);
        }else{
            Calendar calendar = Calendar.getInstance();
            event.setStartTime(calendar.getTimeInMillis());
            event.setEndTime(calendar.getTimeInMillis() + 3600 * 1000);
            setEvent(event);
        }
        notifyPropertyChanged(BR.isAllDay);
    }


    @Bindable
    public static int getChangeStarttime() {
        return CHANGE_STARTTIME;
    }

    @Bindable
    public static int getChangeEndtime() {
        return CHANGE_ENDTIME;
    }

    @Bindable
    public static int getChangeEndrepeatTime(){
        return CHANGE_ENDREPEAT_TIME;
    }
}
