package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends CommonViewModel {

    private Event eventEditViewEvent;
    private EventEditPresenter presenter;
    private CharSequence repeats[] = null;
    private ObservableField<Boolean> editEventIsRepeat ;
    private EventEditViewModel viewModel;
    private String tag;
    private EventEditMvpView mvpView;

    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.presenter = eventEditPresenter;
        editEventIsRepeat = new ObservableField<>();
        viewModel = this;
        tag = getContext().getString(R.string.tag_host_event_edit);
        mvpView = presenter.getView();
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
                    if (eventEditViewEvent.getEventType().equals(getContext().getString(R.string.group))) {
                        mvpView.toHostEventDetail();
                    }else{
                        mvpView.toSoloEventDetail();
                    }
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
                        eventEditViewEvent.setEventType(EventUtil.getEventType(eventEditViewEvent, UserUtil.getUserUid()));
                        eventEditViewEvent.setRecurrence(eventEditViewEvent.getRule().getRecurrence()); // set the repeat string
                        EventUtil.addSelfInInvitee(getContext(), eventEditViewEvent);
                        presenter.updateEvent(eventEditViewEvent);
                        // this if might change later, because the host can be kicked??????
                        if (eventEditViewEvent.getEventType().equals(getContext().getString(R.string.group))) {
                            mvpView.toHostEventDetail(eventEditViewEvent);
                        }else{
                            mvpView.toSoloEventDetail(eventEditViewEvent);
                        }
                    }
                }
            }
        };
    }

    private void changeAllEvent(Event event){
        if (mvpView!=null){
            // set event type
            event.setEventType(EventUtil.getEventType(event, UserUtil.getUserUid()));
            event.setRecurrence(event.getRule().getRecurrence()); // set the repeat string
            EventUtil.addSelfInInvitee(getContext(), event);

            // next find original event(the first event of repeat event)
            Event orgEvent = EventManager.getInstance().findOrgByUUID(event.getEventUid());
            Event copyEventSendToServer = EventManager.getInstance().copyCurrentEvent(event);
            copyEventSendToServer.setStartTime(orgEvent.getStartTime()); // set the starttime and endtime as origin event
            copyEventSendToServer.setEndTime(orgEvent.getEndTime());

            presenter.updateEvent(copyEventSendToServer);
            // this if might change later, because the host can be kicked??????
            if (event.hasAttendee() && event.getInvitee().size()>1) {
                mvpView.toHostEventDetail(event);
            }else{
                mvpView.toSoloEventDetail(event);
            }
        }
    }

    private void changeOnlyThisEvent(Event event){
        if (mvpView!=null){
            // set event type
            event.setEventType(EventUtil.getEventType(event, UserUtil.getUserUid()));
            event.setRecurrence(event.getRule().getRecurrence()); // set the repeat string
            EventUtil.addSelfInInvitee(getContext(), event);

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
            if (event.hasAttendee() && event.getInvitee().size()>1) {
                mvpView.toHostEventDetail(event);
            }else{
                mvpView.toSoloEventDetail(event);
            }
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
                    mvpView.toInviteePicker();
                }
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
        notifyPropertyChanged(BR.eventEditViewEvent);
    }

    @Bindable
    public Boolean getEditEventIsRepeat() {
        return editEventIsRepeat.get();
    }

    public void setEditEventIsRepeat(Boolean editEventIsRepeat) {
        this.editEventIsRepeat.set(editEventIsRepeat);
        notifyPropertyChanged(BR.editEventIsRepeat);
    }
}
