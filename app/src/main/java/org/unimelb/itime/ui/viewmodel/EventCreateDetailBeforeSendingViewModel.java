package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingViewModel extends CommonViewModel {
    private Event newEvDtlEvent;
    private CharSequence repeats[] = null;
    private ObservableField<Boolean> evDtlIsEventRepeat ;
    private EventCreateDetailBeforeSendingViewModel viewModel;
    private CharSequence alertTimes[] = null;
    private EventCreateDetailBeforeSendingMvpView mvpView;
    private int tempYear,tempMonth,tempDay,tempHour,tempMin;
    private EventCreateDetailBeforeSendingPresenter presenter;
    private ObservableField<Boolean> isEndRepeatChange;

    public EventCreateDetailBeforeSendingViewModel(EventCreateDetailBeforeSendingPresenter presenter) {
        this.presenter = presenter;
        newEvDtlEvent = EventManager.getInstance().getCurrentEvent();
        evDtlIsEventRepeat = new ObservableField<>(false);
        isEndRepeatChange = new ObservableField<>(false);
        this.viewModel = this;
        mvpView = presenter.getView();
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        };
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


    public View.OnClickListener onClickEndRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.fragment_event_date_picker, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setOutsideTouchable(false);
                popupView.findViewById(R.id.date_picker_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker datePicker = (DatePicker) popupView.findViewById(R.id.date_picker);
                        tempYear = datePicker.getYear();
                        tempMonth = datePicker.getMonth();
                        tempDay = datePicker.getDayOfMonth();

                        // after click the next button, popup a new time picker window
                        final View timePickerView = inflater.inflate(R.layout.fragment_event_time_picker, null);
                        final PopupWindow timePopupWindow = new PopupWindow(timePickerView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                        timePopupWindow.setOutsideTouchable(false);
                        timePickerView.findViewById(R.id.time_picker_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePicker timePicker = (TimePicker) timePickerView.findViewById(R.id.time_picker);
                                tempMin = timePicker.getCurrentMinute();
                                tempHour = timePicker.getCurrentHour();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(tempYear,tempMonth,tempDay,tempHour,tempMin, 0);
                                newEvDtlEvent.setRepeatEndsTime(calendar.getTimeInMillis());
                                viewModel.setNewEvDtlEvent(newEvDtlEvent);
                                popupWindow.dismiss();
                                timePopupWindow.dismiss();
                            }
                        });
                        timePopupWindow.showAtLocation(timePickerView, Gravity.CENTER, 0, 0);
                        timePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                // if click outside on time picker, the date picker has to dismiss
                                popupWindow.dismiss();
                            }
                        });
                    }
                });
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
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
        newEvDtlEvent.setPhoto(EventUtil.fromStringToPhotoUrlList(photos));
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
                if (newEvDtlEvent.getTitle()==null){
                    newEvDtlEvent.setTitle(getContext().getString(R.string.new_event));
                }

                // pending Timeslots filtered out timeslots which not is not chosed by host
                List<Timeslot> pendingTimeslots = new ArrayList<>();
                for (Timeslot timeSlot : newEvDtlEvent.getTimeslot()){
                    if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))){
                        pendingTimeslots.add(timeSlot);
                    }
                }

                // set event start time and end time, using the latest timeslot
                Timeslot displayTimeslot = TimeSlotUtil.getLatestTimeSlot(pendingTimeslots);
                newEvDtlEvent.setStartTime(displayTimeslot.getStartTime());
                newEvDtlEvent.setEndTime(displayTimeslot.getEndTime());


                newEvDtlEvent.setTimeslot(pendingTimeslots);
                newEvDtlEvent.setHostUserUid(UserUtil.getUserUid());
                if(!newEvDtlEvent.hasPhoto()){
                    newEvDtlEvent.setPhoto(new ArrayList<PhotoUrl>());
                }

                // todo: need to check whether host is in invitees
                if(newEvDtlEvent.hasAttendee()){
                    Invitee host = new Invitee();
                    User user = UserUtil.getInstance().getUser();
                    host.setEventUid(newEvDtlEvent.getEventUid());
                    host.setInviteeUid(AppUtil.generateUuid());
                    host.setUserUid(user.getUserUid());
                    host.setUserId(user.getUserId());
                    host.setStatus("accepted");
                    host.setAliasPhoto(user.getPhoto());
                    host.setAliasName(user.getPersonalAlias());
                    newEvDtlEvent.getInvitee().add(host);
                    newEvDtlEvent.setEventType("group");
                }

                // todo: delete it after finishing calendar
                newEvDtlEvent.setCalendarUid(UserUtil.getUserUid());
                newEvDtlEvent.setRecurringEventUid(newEvDtlEvent.getEventUid());
                newEvDtlEvent.setRecurringEventId(newEvDtlEvent.getEventUid());
                newEvDtlEvent.setStatus("pending");

                presenter.addEvent(newEvDtlEvent);
                if (mvpView!=null){
                    mvpView.onClickSend();
                }
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
                            newEvDtlEvent.setCalendarUid((String) types[i]);
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

//    *******************************************************************************************************


}
