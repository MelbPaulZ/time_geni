package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.greendao.query.QueryBuilder;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.GreenDaoUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends BaseObservable {

    private Event eventEditViewEvent;

    private EventEditPresenter presenter;

    private CharSequence repeats[] = null;

    private ObservableField<Boolean> editEventIsRepeat ;

    private EventEditViewModel viewModel;

    private String tag;

    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.presenter = eventEditPresenter;
        editEventIsRepeat = new ObservableField<>();
        viewModel = this;
        tag = getContext().getString(R.string.tag_host_event_edit);

    }

    public Context getContext(){
        return presenter.getContext();
    }


    // click cancel button on edit event page
    public View.OnClickListener cancelEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventDao eventDao = GreenDaoUtil.getDaoSession(getContext()).getEventDao();
                String uid = eventEditViewEvent.getEventUid();
                Event event = DBManager.getInstance(getContext()).getEvent(eventEditViewEvent.getEventUid());
                event.getInvitee();
                event.getTimeslots();
                if (event.hasAttendee() && event.getInvitee().size()>0) {
                    presenter.toHostEventDetail(event);
                }else{
                    presenter.toSoloEventDetail();
                }
            }
        };
    }

    // click done btn
    public View.OnClickListener finishEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventEditViewEvent.hasAttendee() && eventEditViewEvent.getInvitee().size()>0) {
                    presenter.toHostEventDetail(eventEditViewEvent);
                }else{
                    presenter.toSoloEventDetail(eventEditViewEvent);
                }
            }
        };
    }

    public View.OnClickListener editTimeSlot(){
        // chuan can shu next to do
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventEditMvpView mvpView = presenter.getView();
                if (mvpView!=null){
                    mvpView.toTimeSlotView(tag, eventEditViewEvent);// tiao zhuan wei zhi
                }
            }
        };
    }

    public View.OnClickListener changeLocation(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.changeLocation();
            }
        };
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(eventEditViewEvent.getStartTime());
                String dayOfWeek = EventUtil.getDayOfWeekFull(getContext(), calendar.get(Calendar.DAY_OF_WEEK));

                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle("Choose a repeat type");
                repeats = new CharSequence[]{
                        getContext().getString(R.string.repeat_never),
                        getContext().getString(R.string.repeat_everyday),
                        String.format(getContext().getString(R.string.repeat_everyweek), dayOfWeek),
                        String.format(getContext().getString(R.string.repeat_every_month)),
                        String.format(getContext().getString(R.string.repeat_every_year))};
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!repeats[i].equals(getContext().getString(R.string.repeat_never))) {
                            setEditEventIsRepeat(true);
                            if (eventEditViewEvent.getRepeatEndsTime()==0){
                                eventEditViewEvent.setRepeatEndsTime(eventEditViewEvent.getStartTime()+24*3600000);//default another day
                            }
//                            newEvDtlEvent.setRepeatTypeId(i);
                            viewModel.setEventEditViewEvent(eventEditViewEvent);
                        }
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
                alertTimes = new CharSequence[]{
                        getContext().getString(R.string.none),
                        getContext().getString(R.string.ten_minutes_before),
                        getContext().getString(R.string.one_hour_before),
                        getContext().getString(R.string.one_week_before)
                };
                builder.setItems(alertTimes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // need to add later
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
                CharSequence[] calendarType;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_alert_time));
                calendarType = new CharSequence[]{
                        getContext().getString(R.string.calendar_type_work),
                        getContext().getString(R.string.calendar_type_private)
                };
                builder.setItems(calendarType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // need to add later
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
                presenter.toInviteePicker(tag);
            }
        };
    }

    public View.OnClickListener pickPhoto(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toPhotoPicker(tag);
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
