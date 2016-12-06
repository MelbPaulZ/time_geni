package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailGroupFragment;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventDetailGroupPresenter;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailViewModel extends CommonViewModel {
    private EventDetailGroupPresenter presenter;
    private Event evDtlHostEvent;
    private LayoutInflater inflater;
    private EventDetailGroupMvpView mvpView;
    private Map<String, List<EventDetailGroupFragment.StatusKeyStruct>> adapterData;
    private String tag;
    private Context context;

    public void setEvAdapterEvent(Map<String, List<EventDetailGroupFragment.StatusKeyStruct>> adapterData){
        this.adapterData = adapterData;
    }

    @Bindable
    public Map<String, List<EventDetailGroupFragment.StatusKeyStruct>> getEvAdapterEvent(){
        return this.adapterData;
    }

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
                Timeslot selectedTimeSlot = TimeSlotUtil.getSelectedTimeSlots(context, evDtlHostEvent.getTimeslot()).get(0);
                selectedTimeSlot.setIsConfirmed(1);
                evDtlHostEvent.setStatus(getContext().getString(R.string.confirmed));
                EventManager.getInstance().getWaitingEditEventList().add(evDtlHostEvent); // add event to waiting list, for server response
                presenter.confirmEvent(evDtlHostEvent, selectedTimeSlot.getTimeslotUid());
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
                // here, invitee responses
                List<Timeslot> acceptedTimeslots = new ArrayList<>();
                for (Timeslot timeslot: evDtlHostEvent.getTimeslot()){
                    if (timeslot.getStatus().equals(context.getString(R.string.timeslot_status_accept))){
                        acceptedTimeslots.add(timeslot);
                    }
                }
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
                    // this is a host event
                        Timeslot timeslot = evDtlHostEvent.getTimeslot().get(position);
                    if (timeslot.getIsConfirmed()==0){
                        for (Timeslot ts: evDtlHostEvent.getTimeslot()){
                            ts.setIsConfirmed(0);
                        }
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


    public String getPeopleNum(Timeslot timeslot, Map<String, List<EventDetailGroupFragment.StatusKeyStruct>> adapterData){

        List<EventDetailGroupFragment.StatusKeyStruct> structs = adapterData.get(timeslot.getTimeslotUid());
        int count = 0;
        for (EventDetailGroupFragment.StatusKeyStruct struct: structs
             ) {
            if (struct.getStatus().equals("accepted")){
                count = struct.getInviteeList().size();
                break;
            }
        }

        return count + "";
    }

}
