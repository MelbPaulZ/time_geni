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



    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.presenter = eventEditPresenter;
        editEventIsRepeat = new ObservableField<>();
        viewModel = this;

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
                presenter.toHostEventDetail(event); // try to do
            }
        };
    }

    public View.OnClickListener finishEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
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
