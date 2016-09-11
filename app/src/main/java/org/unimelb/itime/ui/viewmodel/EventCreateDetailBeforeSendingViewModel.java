package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingViewModel extends BaseObservable {
    private Event newEvDtlEvent;
    private String tag;
    private CharSequence repeats[] = null;
    private ObservableField<Boolean> evDtlIsEventRepeat ;
    private EventCreateDetailBeforeSendingViewModel viewModel;


    private EventCreateDetailBeforeSendingPresenter presenter;

    public EventCreateDetailBeforeSendingViewModel(EventCreateDetailBeforeSendingPresenter presenter,Event event) {
        this.presenter = presenter;
        this.newEvDtlEvent = event;
        tag = presenter.getContext().getString(R.string.tag_create_event_before_sending);
        evDtlIsEventRepeat = new ObservableField<>(false);
        this.viewModel = this;
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(newEvDtlEvent.getStartTime());
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
                            setIsEventRepeat(true);
                            if (newEvDtlEvent.getRepeatEndsTime()==0){
                                newEvDtlEvent.setRepeatEndsTime(newEvDtlEvent.getStartTime()+24*3600000);//default another day
                            }
//                            newEvDtlEvent.setRepeatTypeId(i);
                            viewModel.setNewEvDtlEvent(newEvDtlEvent);
                        }
                    }
                });
                builder.show();
            }

        };
    }


    public View.OnClickListener pickEndRepeatDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = presenter.getContext().getString(R.string.tag_create_event_before_sending);
                presenter.pickEndRepeatDate(tag);
            }
        };
    }

    @Bindable
    public Context getContext(){
        return presenter.getContext();
    }

    public View.OnClickListener sendEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newEvDtlEvent.getTitle()==null){
                    newEvDtlEvent.setTitle(getContext().getString(R.string.new_event));
                }
                newEvDtlEvent.setHostUserUid(UserUtil.getUserUid());
                presenter.sendEvent(newEvDtlEvent);
            }
        };
    }

    public View.OnClickListener backToTimeSlotView(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.backToTimeSlotView();
            }
        };
    }

    public View.OnClickListener changeLocation(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.changeLocation(tag);
            }
        };
    }

    public View.OnClickListener reviewTimeSlots(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.backToTimeSlotView();
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


//    *******************************************************************************************************


}
