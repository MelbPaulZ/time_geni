package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.adapter.PhotoAdapter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import me.fesky.library.widget.ios.ActionSheetDialog;

import static org.unimelb.itime.R.id.choose;
import static org.unimelb.itime.R.id.time;
import static org.unimelb.itime.util.EventUtil.getSelfInInvitees;
import static org.unimelb.itime.util.EventUtil.isUserHostOfEvent;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailViewModel extends CommonViewModel {
    private EventCommonPresenter<EventDetailGroupMvpView> presenter;
    private Event evDtlHostEvent;
    private LayoutInflater inflater;
    private EventDetailGroupMvpView mvpView;
    private Map<String, List<EventUtil.StatusKeyStruct>> adapterData;
    private Context context;
    private ObservableBoolean isLeftBtnSelected = new ObservableBoolean(false), isRightBtnSelected = new ObservableBoolean(false);
    private String leftBtnText = "" , rightBtnText = "";
    private int hostConfirmVisibility, hostUnconfirmVisibility, inviteeConfirmVisibility,
    inviteeUnconfirmVisibility, soloInvisible;


    public void setEvAdapterEvent(Map<String, List<EventUtil.StatusKeyStruct>> adapterData){
        this.adapterData = adapterData;
    }

    @Bindable
    public Map<String, List<EventUtil.StatusKeyStruct>> getEvAdapterEvent(){
        return this.adapterData;
    }

    public EventDetailViewModel(EventCommonPresenter<EventDetailGroupMvpView> presenter) {
        this.presenter = presenter;
        this.context = getContext();
        this.inflater = LayoutInflater.from(getContext());
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

    public View.OnClickListener gotoGridView(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.gotoGridView();
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
                evDtlHostEvent.setStatus(Event.STATUS_CONFIRMED);
                presenter.confirmEvent(evDtlHostEvent, selectedTimeSlot.getTimeslotUid());

            }
        };
    }

    public View.OnClickListener onClickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.onTaskComplete(EventCommonPresenter.TASK_BACK, null);
                }
            }
        };
    }

    public View.OnClickListener onInviteeClickRightBtn() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();
                inflater = LayoutInflater.from(context);
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

                        Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                        if (EventUtil.isEventConfirmed(context, evDtlHostEvent)) {
                            presenter.quitEvent(evDtlHostEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                        }else {
                            presenter.rejectTimeslots(evDtlHostEvent);
                        }

                    }
                });
                alertDialog.setView(root);
                alertDialog.show();
            }
        };
    }


    public View.OnClickListener acceptEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                if (evDtlHostEvent.getStatus().equals(Event.STATUS_CONFIRMED)){
                    // todo implement update only this
                    presenter.acceptEvent(evDtlHostEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                }else {
                    presenter.acceptTimeslots(evDtlHostEvent);
                }
            }
        };
    }


    public View.OnClickListener quitEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                if (EventUtil.isEventConfirmed(context, evDtlHostEvent)) {
                    // TODO: 4/1/17 repeat event popup window
                    if (evDtlHostEvent.getRecurrence().length==0) {
                        // non-repeat event quit
                        presenter.quitEvent(evDtlHostEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                    }else{
                        // repeat event quit
                        popupRepeatQuit(orgEvent);
                    }
                }else {
                    presenter.rejectTimeslots(evDtlHostEvent);
                }
            }
        };
    }

    private void popupRepeatQuit(final Event orgEvent){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getContext())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getContext().getString(R.string.event_quit_repeat_text1), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                presenter.quitEvent(evDtlHostEvent, EventCommonPresenter.UPDATE_THIS, orgEvent.getStartTime());
                            }
                        })
                .addSheetItem(getContext().getString(R.string.event_quit_repeat_text2), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int i) {
                                presenter.quitEvent(evDtlHostEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                            }
                        });
        actionSheetDialog.show();
    }

    public View.OnClickListener rejectAll(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();
                inflater = LayoutInflater.from(context);
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

                        Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                        if (EventUtil.isEventConfirmed(context, evDtlHostEvent)) {
                            presenter.quitEvent(evDtlHostEvent, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                        }else {
                            presenter.rejectTimeslots(evDtlHostEvent);
                        }

                    }
                });
                alertDialog.setView(root);
                alertDialog.show();
            }
        };
    }




    private void resetState(){
        resetLeftBtn();
        resetRightBtn();
    }

    // left buttons

    public void resetLeftBtn(){
        Invitee me = EventUtil.getSelfInInvitees(context, evDtlHostEvent);
        if (evDtlHostEvent.getStatus().equals(Event.STATUS_CONFIRMED)){
            // event has been confirmed by host
            if (me.getStatus().equals(Invitee.STATUS_ACCEPTED)){
                setIsLeftBtnSelected(true);
                setLeftBtnText(context.getString(R.string.accepted));
            }else{
                setIsLeftBtnSelected(false);
                setLeftBtnText(context.getString(R.string.accept));
            }
        }else if (evDtlHostEvent.getStatus().equals(Event.STATUS_PENDING) ||
                evDtlHostEvent.getStatus().equals(Event.STATUS_UPDATING)){
            if (me.getStatus().equals(Invitee.STATUS_ACCEPTED)){
                setIsLeftBtnSelected(true);
                setLeftBtnText(context.getString(R.string.accepted));
            }else{
                setIsLeftBtnSelected(false);
                setLeftBtnText(context.getString(R.string.accept));
            }
        }else if (evDtlHostEvent.getStatus().equals(Event.STATUS_CANCELLED)){
            setIsLeftBtnSelected(false);
            setLeftBtnText(context.getString(R.string.accept));
        }
    }


    public boolean getLeftBtnClickable(Event event){
        Invitee me = EventUtil.getSelfInInvitees(context, evDtlHostEvent);
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            // event has been confirmed by host
            if (me.getStatus().equals(Invitee.STATUS_ACCEPTED)){
                return false;
            }else{
                return true;
            }
        }else if (event.getStatus().equals(Event.STATUS_PENDING) ||
                event.getStatus().equals(Event.STATUS_UPDATING)){
            if (me.getStatus().equals(Invitee.STATUS_ACCEPTED)){
                return false;
            }else if (me.getStatus().equals(Invitee.STATUS_NEEDSACTION)){
                if (TimeSlotUtil.chooseAtLeastOnTimeSlot(context, event)){
                    return true;
                }else {
                    return false;
                }
            }
        }

        // TODO: 3/1/17 cancelled panduan
        return true;
    }



    // right buttons
    private void resetRightBtn(){
        Invitee me = EventUtil.getSelfInInvitees(context, evDtlHostEvent);
        if (evDtlHostEvent.getStatus().equals(Event.STATUS_CONFIRMED)){
            // event has been confirmed by host
            if (me.getStatus().equals(Invitee.STATUS_DECLINED)){
                setIsRightBtnSelected(true);
                setRightBtnText(context.getString(R.string.quitted));
            }else{
                setIsRightBtnSelected(false);
                setRightBtnText(context.getString(R.string.quit));
            }
        }else if (evDtlHostEvent.getStatus().equals(Event.STATUS_PENDING) || evDtlHostEvent.getStatus().equals(Event.STATUS_UPDATING)){
            if (me.getStatus().equals(Invitee.STATUS_DECLINED)){
                setIsRightBtnSelected(true);
                setRightBtnText(context.getString(R.string.all_rejected));
            }else{
                setIsRightBtnSelected(false);
                setRightBtnText(context.getString(R.string.reject_all));
            }
        }else if (evDtlHostEvent.getStatus().equals(Event.STATUS_CANCELLED)){
            setIsRightBtnSelected(true);
            setRightBtnText(context.getString(R.string.quitted));
        }
    }


    public boolean getRightBtnClickable(Event event){
        Invitee me = EventUtil.getSelfInInvitees(context, evDtlHostEvent);
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            // event has been confirmed by host
            if (me.getStatus().equals(Invitee.STATUS_DECLINED)){
                return false;
            }else{
                return true;
            }
        }else if (event.getStatus().equals(Event.STATUS_PENDING) || event.getStatus().equals(Event.STATUS_UPDATING)){
            if (me.getStatus().equals(Invitee.STATUS_DECLINED)){
                return false;
            }else if (TimeSlotUtil.chooseAtLeastOnTimeSlot(context, event)){
                return false;
            }else{
                return true;
            }
        }
        // TODO: 3/1/17 cancelled panduan
        return true;
    }



    public View.OnClickListener onClickTimeSlot(final Timeslot timeslot){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Timeslot timeSlot = evDtlHostEvent.getTimeslot().get(position);
                if (evDtlHostEvent.getHostUserUid().equals(UserUtil.getInstance(context).getUserUid())){
                    // this is a host event
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
                    if (evDtlHostEvent.getStatus().equals(Event.STATUS_CANCELLED)){
                        rejectedAcceptedSwitch(timeslot);
                    }else{
                        pendingAcceptedSwitch(timeslot);
                    }
                }
                setEvDtlHostEvent(evDtlHostEvent);
                // here show let the editEventFragment know the event is change
            }
        };
    }

    private void pendingAcceptedSwitch(Timeslot timeslot){
        if (timeslot.getStatus().equals(Timeslot.STATUS_PENDING)) {
            timeslot.setStatus(Timeslot.STATUS_ACCEPTED);
        } else if (timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED)) {
            timeslot.setStatus(Timeslot.STATUS_PENDING);
        }
    }

    private void rejectedAcceptedSwitch(Timeslot timeslot){
        if (timeslot.getStatus().equals(Timeslot.STATUS_REJECTED)){
            timeslot.setStatus(Timeslot.STATUS_ACCEPTED);
        }else if (timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED)){
            timeslot.setStatus(Timeslot.STATUS_REJECTED);
        }
    }


    public View.OnClickListener viewInviteeResponse(final Timeslot timeslot){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.viewInviteeResponse(timeslot);
                }
            }
        };
    }

    public View.OnClickListener onClickPhotoGridBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.onClickPhotoGridBack();
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
        resetState();
        notifyPropertyChanged(BR.evDtlHostEvent);
    }


    public String getPeopleNum(Timeslot timeslot, Map<String, List<EventUtil.StatusKeyStruct>> adapterData){

        List<EventUtil.StatusKeyStruct> structs = adapterData.get(timeslot.getTimeslotUid());
        int count = 0;
        for (EventUtil.StatusKeyStruct struct: structs
             ) {
            if (struct.getStatus().equals("accepted")){
                count = struct.getInviteeList().size();
                break;
            }
        }

        return count + "";
    }


    public int confirmTimeVisibility(Event event){
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int timeslotListVisibility(Event event){
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            return View.GONE;
        }else{
            return View.VISIBLE;
        }
    }

    public int getMessageStatusColor(Event event){
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            return context.getResources().getColor(R.color.color_63ADF2);
        }else{
            return context.getResources().getColor(R.color.color_FF9600);
        }
    }

    public View.OnClickListener onClickHostQuit(final Event event){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    // TODO: 8/12/2016 quit event update server and local
                    Toast.makeText(context, "Quit This Event, To do", Toast.LENGTH_SHORT).show();
                    Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                    // TODO: 26/12/2016 implement quit only this?
                    presenter.quitEvent(event, EventCommonPresenter.UPDATE_ALL ,orgEvent.getStartTime());

                }
            }
        };
    }

    @BindingAdapter({"bind:url"})
    public static void bindUrlHelper(ImageView view,String url){
        EventUtil.bindUrlHelper(view.getContext(), url, view, new CircleTransform());
    }

    // following get visibility in dif status of event

    @Bindable
    public int getHostConfirmVisibility() {
        if (EventUtil.isGroupEvent(context, evDtlHostEvent) &&
                EventUtil.isEventConfirmed(context, evDtlHostEvent) &&
                EventUtil.isUserHostOfEvent(context, evDtlHostEvent))
            return View.VISIBLE;
        else
            return View.GONE;
    }

    public void setHostConfirmVisibility(int hostConfirmVisibility) {
        this.hostConfirmVisibility = hostConfirmVisibility;
        notifyPropertyChanged(BR.hostConfirmVisibility);
    }

    @Bindable
    public int getHostUnconfirmVisibility() {
        if (EventUtil.isGroupEvent(context, evDtlHostEvent) &&
                !EventUtil.isEventConfirmed(context, evDtlHostEvent) &&
                EventUtil.isUserHostOfEvent(context, evDtlHostEvent)){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }

    }

    public void setHostUnconfirmVisibility(int hostUnconfirmVisibility) {
        this.hostUnconfirmVisibility = hostUnconfirmVisibility;
        notifyPropertyChanged(BR.hostUnconfirmVisibility);
    }

    //***********************************************************
    public int confirmVisibility(Event event){
        if (event.getEventType().equals(Event.TYPE_SOLO)){
            return View.GONE;
        }
        if (event.getStatus().equals(Event.STATUS_CONFIRMED) ||
                (event.getStatus().equals(Event.STATUS_CANCELLED) && isCancelledEventConfirmed(event))){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int unconfirmVisibility(Event event){
        if (event.getStatus().equals(Event.STATUS_PENDING) ||
                event.getStatus().equals(Event.STATUS_UPDATING) ||
                event.getStatus().equals(Event.STATUS_CANCELLED)){
            if (!isUserHostOfEvent(context, event)){
                Log.i("debug", "unconfirmVisibility: 2");
                return View.VISIBLE;
            }else{
                return View.GONE;
            }
        }else{
            Log.i("debug", "unconfirmVisibility: 1");
            return View.GONE;
        }
    }

    private boolean isCancelledEventConfirmed(Event event){
        if (event.getConfirmedCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public int unconfirmHostVisibility(Event event){
        if ((event.getStatus().equals(Event.STATUS_PENDING) ||
                event.getStatus().equals(Event.STATUS_UPDATING) )&&
                isUserHostOfEvent(context, event)){
            return View.VISIBLE;
        }else if ((event.getStatus().equals(Event.STATUS_CANCELLED) && !isCancelledEventConfirmed(event))){
            if (isUserHostOfEvent(context, event)) {
                return View.VISIBLE;
            }else{
                return View.GONE;
            }
        }
        else{
            return View.GONE;
        }
    }

    public int getLocationVisibility(Event event){
        if (!event.getLocation().equals("")){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int getNoteVisibility(Event event){
        if (!event.getDescription().equals("")){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    //***********************************************************


    @Bindable
    public int getSoloInvisible() {
        if (!EventUtil.isGroupEvent(context, evDtlHostEvent)){
            return View.GONE;
        }else{
            return View.VISIBLE;
        }
    }

    public void setSoloInvisible(int soloInvisible) {
        this.soloInvisible = soloInvisible;
        notifyPropertyChanged(BR.soloInvisible);
    }

    @Bindable
    public ObservableBoolean getIsLeftBtnSelected() {
        return isLeftBtnSelected;
    }

    public void setIsLeftBtnSelected(boolean isLeftBtnSelected) {
        this.isLeftBtnSelected.set(isLeftBtnSelected);

    }

    @Bindable
    public ObservableBoolean getIsRightBtnSelected() {
        return isRightBtnSelected;
    }

    public void setIsRightBtnSelected(boolean isRightBtnSelected) {
        this.isRightBtnSelected.set(isRightBtnSelected);
    }

    @Bindable
    public String getLeftBtnText() {
        return leftBtnText;
    }

    public void setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
        notifyPropertyChanged(BR.leftBtnText);
    }

    @Bindable
    public String getRightBtnText() {
        return rightBtnText;
    }

    public void setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
    }

    // set grid view data binding
    @BindingAdapter("app:event")
    public static void setGradView(GridView gridView, Event event){

        List<String> urls = new ArrayList<>();
        if (event!=null) {
            for (PhotoUrl photoUrl : event.getPhoto()) {
                urls.add(photoUrl.getUrl());
            }
            PhotoAdapter adapter = new PhotoAdapter(gridView.getContext(), R.id.gridview_photo, urls);
            gridView.setAdapter(adapter);
        }
    }
}
