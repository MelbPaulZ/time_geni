package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventDetailGroupPresenter;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailViewModel extends BaseObservable {
    private EventDetailGroupPresenter presenter;
    private Event evDtlHostEvent;
    private LayoutInflater inflater;
    private EventDetailGroupMvpView mvpView;

    private String tag;
    private Context context;



    public EventDetailViewModel(EventDetailGroupPresenter presenter) {
        this.presenter = presenter;
        this.inflater = presenter.getInflater();
        tag = presenter.getContext().getString(R.string.tag_host_event_detail);
        this.context = getContext();
        mvpView = presenter.getView();
    }

    public Context getContext() {
        return presenter.getContext();
    }

    public View.OnClickListener onClickEdit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.toEditEvent();
                }
            }
        };
    }

    public View.OnClickListener onClickViewInCalendar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.viewInCalendar();
                }
            }
        };
    }

    public View.OnClickListener onClickUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = evDtlHostEvent.getUrl();
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }

    public View.OnClickListener onClickHostConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timeslot newTimeSlot = TimeSlotUtil.getSelectedTimeSlots(context, evDtlHostEvent.getTimeslot()).get(0);
                presenter.confirmEvent(evDtlHostEvent, newTimeSlot);
                if (mvpView!=null){
                    mvpView.toCalendar();
                }
            }
        };
    }

    public View.OnClickListener onClickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.toCalendar();
                }
            }
        };
    }

    public View.OnClickListener onClickRejectAll() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();

                inflater = presenter.getInflater();
                View root = inflater.inflate(R.layout.event_detail_reject_alert_view, null);

                TextView button_cancel = (TextView) root.findViewById(R.id.alert_message_cancel_button);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                TextView button_reject = (TextView) root.findViewById(R.id.alert_message_reject_button);
                button_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence msg = "send reject message";
                        Toast.makeText(presenter.getContext(), msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        // here should add presenter change event status as reject
                        if (mvpView!=null){
                            mvpView.toCalendar();
                        }
                    }
                });
                alertDialog.setView(root);
                alertDialog.show();
            }
        };
    }

    // this is for invitee click accept
    public View.OnClickListener onClickAccept() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Timeslot newTimeSlot = TimeSlotUtil.getSelectedTimeSlots(context, evDtlHostEvent.getTimeslot()).get(0);
//                evDtlHostEvent.setStartTime(newTimeSlot.getStartTime());
//                evDtlHostEvent.setEndTime(newTimeSlot.getEndTime());
                if (mvpView!=null){
                    mvpView.toCalendar();
                }

            }
        };
    }


    public View.OnClickListener onClickTimeSlot(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timeslot timeSlot = evDtlHostEvent.getTimeslot().get(position);
                if (evDtlHostEvent.getHostUserUid().equals(UserUtil.getUserUid())){
                        Timeslot timeslot = evDtlHostEvent.getTimeslot().get(position);
                    for (Timeslot ts: evDtlHostEvent.getTimeslot()){
                        ts.setIsConfirmed(0);
                    }
                    if (timeslot.getIsConfirmed()==0){
                        timeslot.setIsConfirmed(1);
                    }else{
                        timeslot.setIsConfirmed(0);
                    }
                }else {
                    // can choose any number of timeslots
                    if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_pending))) {
                        timeSlot.setStatus(getContext().getString(R.string.timeslot_status_accept));
                    } else if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_accept))) {
                        timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
                    }
                }
                setEvDtlHostEvent(evDtlHostEvent);
                // here show let the editEventFragment know the event is change
            }
        };
    }


    public View.OnClickListener viewInviteeResponse(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.viewInviteeResponse(evDtlHostEvent.getTimeslot().get(position));
                }
            }
        };
    }


//    ***************************************************************

    @Bindable
    public Event getEvDtlHostEvent() {
        return evDtlHostEvent;
    }

    public void setEvDtlHostEvent(Event evDtlHostEvent) {
        this.evDtlHostEvent = evDtlHostEvent;
        notifyPropertyChanged(BR.evDtlHostEvent);
    }

    public void unselectRestTimeSlots(int notChangePostion){
        for (int i = 0; i < evDtlHostEvent.getTimeslot().size() ; i ++){
            if (i != notChangePostion){
                evDtlHostEvent.getTimeslot().get(i).setStatus(context.getString(R.string.timeslot_status_pending));
            }
        }
    }


}
