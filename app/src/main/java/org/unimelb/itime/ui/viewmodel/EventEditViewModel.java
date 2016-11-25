package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

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
                    if (eventEditViewEvent.hasAttendee() && eventEditViewEvent.getInvitee().size()>0) {
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
                if (mvpView!=null){
                    // set event type
                    eventEditViewEvent.setEventType(EventUtil.getEventType(eventEditViewEvent, UserUtil.getUserUid()));
                    eventEditViewEvent.setRecurrence(eventEditViewEvent.getRule().getRecurrence()); // set the repeat string
                    EventUtil.addSelfInInvitee(getContext(), eventEditViewEvent);
                    presenter.updateEvent(eventEditViewEvent);
                    // this if might change later, because the host can be kicked??????
                    if (eventEditViewEvent.hasAttendee() && eventEditViewEvent.getInvitee().size()>1) {
                        mvpView.toHostEventDetail(eventEditViewEvent);
                    }else{
                        mvpView.toSoloEventDetail(eventEditViewEvent);
                    }
                }
            }
        };
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

//    @BindingAdapter("imageResource")
//    public static void setImageResource(ImageView imageView, Event event){
//        LinearLayout parent = (LinearLayout) imageView.getParent();
//        int position = parent.indexOfChild(imageView); // get the position
//        if (event==null){
//            // it seems has some problem here, called twice???? ask Chuandong.
//        }else{
//            if (event.hasPhoto() && event.getPhoto().size()>= position+1){
//                imageView.setVisibility(View.VISIBLE);
////                File f = new File(event.getPhoto().get(position).getLocalPath());
//                int size = DensityUtil.dip2px(imageView.getContext(), 40);
//                Picasso.with(imageView.getContext())
//                        .load(event.getPhoto().get(position).getUrl())
//                        .resize(size ,size)
//                        .centerCrop()
//                        .into(imageView);
//            }else{
//                imageView.setVisibility(View.GONE);
//            }
//        }
//    }

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
