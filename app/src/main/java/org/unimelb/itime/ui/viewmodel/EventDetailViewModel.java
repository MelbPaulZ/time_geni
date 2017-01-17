package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.SlotResponse;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.mvpview.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.fesky.library.widget.ios.ActionSheetDialog;
import me.tatarka.bindingcollectionadapter.ItemView;

import static org.unimelb.itime.util.EventUtil.isUserHostOfEvent;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailViewModel extends CommonViewModel {
    private EventPresenter<EventDetailMvpView> presenter;
    private Event event;
    private LayoutInflater inflater;
    private EventDetailMvpView mvpView;
    private Context context;
    private ObservableBoolean isLeftBtnSelected = new ObservableBoolean(false), isRightBtnSelected = new ObservableBoolean(false);
    private String leftBtnText = "" , rightBtnText = "";

    private int hostConfirmVisibility, hostUnconfirmVisibility, inviteeConfirmVisibility,
    inviteeUnconfirmVisibility, soloInvisible;


    public EventDetailViewModel(EventPresenter<EventDetailMvpView> presenter) {
        this.presenter = presenter;
        this.context = getContext();
        this.inflater = LayoutInflater.from(getContext());
        this.wrapperTimeSlotList = new ArrayList<>();
        mvpView = presenter.getView();
    }

    public Context getContext() {
        return presenter.getContext();
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

    public View.OnClickListener toResponse(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toResponse();
                }
            }
        };
    }

    public View.OnClickListener createEventFromThisTemplate(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the template for new event
                Event cpyEvent = EventUtil.copyEvent(event);
                String eventUid = AppUtil.generateUuid();
                cpyEvent.setEventUid(eventUid);
                for (Invitee invitee : cpyEvent.getInvitee()){
                    invitee.setEventUid(eventUid);
                    invitee.setStatus(Invitee.STATUS_NEEDSACTION); // maybe need to check if it is host
                    String inviteeUid = AppUtil.generateUuid();
                    invitee.setInviteeUid(inviteeUid);
                    invitee.setSlotResponses(new ArrayList<SlotResponse>());
                }

                cpyEvent.setTimeslot(new ArrayList<Timeslot>());
                cpyEvent.setPhoto(new ArrayList<PhotoUrl>());

                if (mvpView!=null){
                    mvpView.createEventFromThisTemplate(cpyEvent);
                }
            }
        };
    }

    public View.OnClickListener onClickUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = event.getUrl();
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }

    public View.OnClickListener onClickHostConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timeslot timeslot = null;
                for (SubTimeslotViewModel viewModel: wrapperTimeSlotList){
                    if (viewModel.getWrapper().isSelected()){
                        timeslot = (Timeslot) viewModel.getWrapper().getTimeSlot();
                        break;
                    }
                }
                presenter.confirmEvent(event.getCalendarUid(), event.getEventUid(), timeslot.getTimeslotUid());
            }
        };
    }


    public View.OnClickListener acceptEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
                    // todo implement update only this
                    presenter.acceptEvent(event.getCalendarUid(),
                            event.getEventUid(),
                            EventPresenter.UPDATE_ALL,
                            orgEvent.getStartTime());
                }else {
                    HashMap<String, Object> params = new HashMap<>();
                    ArrayList<String> timeslotUids=  new ArrayList<>();
                    for (SubTimeslotViewModel viewModel: wrapperTimeSlotList){
                        if(viewModel.getWrapper().isSelected()){
                            Timeslot timeslot = (Timeslot) viewModel.getWrapper().getTimeSlot();
                            timeslotUids.add(timeslot.getTimeslotUid());
                        }
                    }
                    params.put("timeslots", timeslotUids);
                    presenter.acceptTimeslots(
                            event.getCalendarUid(),
                            event.getEventUid(),
                            params);
                }
            }
        };
    }


    public View.OnClickListener quitEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                if (EventUtil.isEventConfirmed(context, event)) {
                    // TODO: 4/1/17 repeat event popup window
                    if (event.getRecurrence().length==0) {
                        // non-repeat event quit
                        presenter.quitEvent(event.getCalendarUid(), event.getEventUid(), EventPresenter.UPDATE_ALL, orgEvent.getStartTime());
                    }else{
                        // repeat event quit
                        popupRepeatQuit(orgEvent);
                    }
                }else {
                    presenter.rejectTimeslots(event.getCalendarUid(), event.getEventUid());
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
                                presenter.quitEvent(event.getCalendarUid(),
                                        event.getEventUid(),
                                        EventPresenter.UPDATE_THIS,
                                        orgEvent.getStartTime());
                            }
                        })
                .addSheetItem(getContext().getString(R.string.event_quit_repeat_text2), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int i) {
                                presenter.quitEvent(event.getCalendarUid(),
                                        event.getEventUid(),
                                        EventPresenter.UPDATE_ALL,
                                        orgEvent.getStartTime());
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
                        if (EventUtil.isEventConfirmed(context, event)) {
                            presenter.quitEvent(event.getCalendarUid(),
                                    event.getEventUid(),
                                    EventPresenter.UPDATE_ALL,
                                    orgEvent.getStartTime());
                        }else {
                            presenter.rejectTimeslots(event.getCalendarUid(), event.getEventUid());
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
        Invitee me = EventUtil.getSelfInInvitees(context, event);
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            // event has been confirmed by host
            if (me.getStatus().equals(Invitee.STATUS_ACCEPTED)){
                setIsLeftBtnSelected(true);
                setLeftBtnText(context.getString(R.string.accepted));
            }else{
                setIsLeftBtnSelected(false);
                setLeftBtnText(context.getString(R.string.accept));
            }
        }else if (event.getStatus().equals(Event.STATUS_PENDING) ||
                event.getStatus().equals(Event.STATUS_UPDATING)){
            if (me.getStatus().equals(Invitee.STATUS_ACCEPTED)){
                setIsLeftBtnSelected(true);
                setLeftBtnText(context.getString(R.string.accepted));
            }else{
                setIsLeftBtnSelected(false);
                setLeftBtnText(context.getString(R.string.accept));
            }
        }else if (event.getStatus().equals(Event.STATUS_CANCELLED)){
            setIsLeftBtnSelected(false);
            setLeftBtnText(context.getString(R.string.accept));
        }
    }



    public boolean getLeftBtnClickable(List<SubTimeslotViewModel> viewModels){
        Invitee me = EventUtil.getSelfInInvitees(context, this.event);
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
                if (TimeSlotUtil.isAtLeastOneWrapperSelected(viewModels)){
                    return true;
                }else {
                    return false;
                }
            }
        }
        return true;
    }



    // right buttons
    private void resetRightBtn(){
        Invitee me = EventUtil.getSelfInInvitees(context, event);
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            // event has been confirmed by host
            if (me.getStatus().equals(Invitee.STATUS_DECLINED)){
                setIsRightBtnSelected(true);
                setRightBtnText(context.getString(R.string.quitted));
            }else{
                setIsRightBtnSelected(false);
                setRightBtnText(context.getString(R.string.quit));
            }
        }else if (event.getStatus().equals(Event.STATUS_PENDING) || event.getStatus().equals(Event.STATUS_UPDATING)){
            if (me.getStatus().equals(Invitee.STATUS_DECLINED)){
                setIsRightBtnSelected(true);
                setRightBtnText(context.getString(R.string.all_rejected));
            }else{
                setIsRightBtnSelected(false);
                setRightBtnText(context.getString(R.string.reject_all));
            }
        }else if (event.getStatus().equals(Event.STATUS_CANCELLED)){
            setIsRightBtnSelected(true);
            setRightBtnText(context.getString(R.string.quitted));
        }
    }

    public boolean getRightBtnClickable(List<SubTimeslotViewModel> viewModels){
        Invitee me = EventUtil.getSelfInInvitees(context, this.event);
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
            }else if (TimeSlotUtil.isAtLeastOneWrapperSelected(viewModels)){
                return false;
            }else{
                Log.i("123", "getRightBtnClickable: ");
                return true;
            }
        }
        // TODO: 3/1/17 cancelled panduan
        return true;
    }

//    ***************************************************************

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        resetState();
        notifyPropertyChanged(BR.event);
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
                    presenter.quitEvent(
                            event.getCalendarUid(),
                            event.getEventUid(),
                            EventPresenter.UPDATE_ALL,
                            orgEvent.getStartTime());

                }
            }
        };
    }



    /**
     * this method use for update event by wrapperlists
     * @param wrapperTimeSlot
     * @param posStr if the timeslot is selected, update status to posStr
     * @param negStr if the timeslot is unselected, update status to negStr
     */
    private void updateTimeslotStatus(WrapperTimeSlot wrapperTimeSlot, String posStr, String negStr){
        Timeslot ts = (Timeslot) wrapperTimeSlot.getTimeSlot();
        Timeslot timeslot = TimeSlotUtil.getTimeSlot(event, ts);
        if (wrapperTimeSlot.isSelected()){
            timeslot.setStatus(posStr);
        }else{
            timeslot.setStatus(negStr);
        }
    }

    @BindingAdapter({"bind:url"})
    public static void bindUrlHelper(ImageView view,String url){
        EventUtil.bindUrlHelper(view.getContext(), url, view, new CircleTransform());
    }

    // following get visibility in dif status of event

    @Bindable
    public int getHostConfirmVisibility() {
        if (EventUtil.isGroupEvent(context, event) &&
                EventUtil.isEventConfirmed(context, event) &&
                EventUtil.isUserHostOfEvent(context, event))
            return View.VISIBLE;
        else
            return View.GONE;
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
        if (!EventUtil.isGroupEvent(context, event)){
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


    /**
     * for timeslot view
     */
    private List<SubTimeslotViewModel> wrapperTimeSlotList;
    private ItemView timeslotItemView = ItemView.of(BR.itemVM, R.layout.listview_timeslot_pick);

    @Bindable
    public List<SubTimeslotViewModel> getWrapperTimeSlotList() {
        return wrapperTimeSlotList;
    }

    public void setWrapperTimeSlotList(List<SubTimeslotViewModel> wrapperTimeSlotList) {
        this.wrapperTimeSlotList = wrapperTimeSlotList;
        notifyPropertyChanged(BR.wrapperTimeSlotList);
    }

    @Bindable
    public ItemView getTimeslotItemView() {
        return timeslotItemView;
    }

    public void setTimeslotItemView(ItemView timeslotItemView) {
        this.timeslotItemView = timeslotItemView;
        notifyPropertyChanged(BR.timeslotItemView);
    }

    /**
     * the view model of timeslot item view
     */
    public static class SubTimeslotViewModel extends BaseObservable{
        private WrapperTimeSlot wrapper;
        private EventDetailMvpView mvpView;
        private boolean iconSelected;
        private boolean isHostEvent;
        private Map<String, List<EventUtil.StatusKeyStruct>> replyData;

        public boolean isHostEvent() {
            return isHostEvent;
        }

        public void setHostEvent(boolean hostEvent) {
            isHostEvent = hostEvent;
        }


        public SubTimeslotViewModel(EventDetailMvpView mvpView) {
            this.mvpView = mvpView;
        }

        @Bindable
        public WrapperTimeSlot getWrapper() {
            return wrapper;
        }

        public void setWrapper(WrapperTimeSlot wrapper) {
            this.wrapper = wrapper;
            notifyPropertyChanged(BR.wrapper);
        }

        public View.OnClickListener onLeftPartClicked(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wrapper.setSelected(!wrapper.isSelected());
                    setIconSelected(wrapper.isSelected());
                    // call back to fragment, and let outter viewmodel reload page
                    if (mvpView!=null){
                        mvpView.onTimeslotClick(wrapper);
                    }
                }
            };
        }


        @Bindable
        public boolean isIconSelected() {
            return iconSelected;
        }

        public void setIconSelected(boolean iconSelected) {
            this.iconSelected = iconSelected;
            notifyPropertyChanged(BR.iconSelected);
        }

        public View.OnClickListener onRightPartClicked(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                    if (mvpView!=null){
                        mvpView.viewInviteeResponse((Timeslot) wrapper.getTimeSlot());
                    }
                }
            };
        }

        public EventDetailMvpView getMvpView() {
            return mvpView;
        }

        public String getPeopleNum(ITimeTimeSlotInterface timeslot, Map<String, List<EventUtil.StatusKeyStruct>> adapterData){

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

        @Bindable
        public Map<String, List<EventUtil.StatusKeyStruct>> getReplyData() {
            return replyData;
        }

        public void setReplyData(Map<String, List<EventUtil.StatusKeyStruct>> replyData) {
            this.replyData = replyData;
        }

    }


    /**
     * for invitee photo
     */

    private List<Invitee> inviteeList = new ArrayList<>();
    private ItemView inviteeItemView = ItemView.of(BR.item, R.layout.listview_invitee_photo);

    @Bindable
    public List<Invitee> getInviteeList() {
        return inviteeList;
    }

    public void setInviteeList(List<Invitee> inviteeList) {
        this.inviteeList = inviteeList;
        notifyPropertyChanged(BR.inviteeList);
    }

    @Bindable
    public ItemView getInviteeItemView() {
        return inviteeItemView;
    }

    public void setInviteeItemView(ItemView inviteeItemView) {
        this.inviteeItemView = inviteeItemView;
        notifyPropertyChanged(BR.inviteeItemView);
    }
}
