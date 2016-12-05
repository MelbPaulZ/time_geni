package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.databinding.library.baseAdapters.BR;


import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends CommonViewModel {
    private MainInboxPresenter presenter;
    private Message message;

    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
    }

    public String getTag1(Context context){
        Event event = EventManager.getInstance().findEventByUid(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)){
            if (event.getStatus().equals("confirmed")){
                int goingNum = message.getNum1();
                return goingNum + " " + context.getString(R.string.going);
            }else{
                int acceptNum = message.getNum1();
                return acceptNum + " " + context.getString(R.string.accept);
            }
        }
        return "this is invitee";
    }

    public String getTag2(Context context){
        Event event = EventManager.getInstance().findEventByUid(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)){
            if (event.getStatus().equals("confirmed")){
                int noReplyNum = message.getNum3();
                return noReplyNum + " " + context.getString(R.string.no_reply);
            }else{
                int rejectNum = message.getNum2();
                return rejectNum + " " + context.getString(R.string.reject);
            }
        }
        return "this is invitee";
    }

    public String getTag3(Context context){
        Event event = EventManager.getInstance().findEventByUid(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)){
            if (event.getStatus().equals("confirmed")){
                return null;
            }else{
                int noReplyNum = message.getNum3();
                return noReplyNum + " " + context.getString(R.string.no_reply);
            }
        }
        return "this is invitee";
    }


    @BindingAdapter({"bind:dotVisible"})
    public static void setDotVisible(ImageView view, Message message){
        if (message.isRead()){
            Log.i("reddot", "setDotVisible: + gone");
            view.setVisibility(View.GONE);
        }else{
            Log.i("reddot", "setDotVisible: + visible");
            view.setVisibility(View.VISIBLE);
        }
//        view.requestLayout();
    }

    @BindingAdapter({"bind:message"," bind:tagNum"})
    public static void setTagBackground(TextView view, Message message, int tagNum){
        Event event = EventManager.getInstance().findEventByUid(message.getEventUid());
        if (event.getStatus().equals("confirmed")){
            if (tagNum == 1){
                view.setBackgroundResource(R.drawable.inbox_host_tag_green);
            }else if (tagNum == 2){
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            }else if (tagNum ==3 ){
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            }
        }else{
            if (tagNum == 1){
                view.setBackgroundResource(R.drawable.inbox_host_tag_blue);
            }else if (tagNum == 2){
                view.setBackgroundResource(R.drawable.inbox_host_tag_red);
            }else if (tagNum == 3){
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            }
        }
    }



    @BindingAdapter({"bind:visible"})
    public static void setVisible(TextView view, Message message){
        Event event = EventManager.getInstance().findEventByUid(message.getEventUid());
        if (event.getStatus().equals("confirmed")){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
    }

    @Bindable
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }



}
