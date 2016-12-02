package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends CommonViewModel {
    private MainInboxPresenter presenter;
    private List<Message> messages;

    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
    }


    public void setData(List<Message> messages){
        this.messages = messages;
    }

    public String getTag1(int position, List<Message> messages){
        Message message = messages.get(position);
        Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)){
            if (event.getStatus().equals("confirmed")){
                int goingNum = message.getNum1();
                return goingNum + " " + presenter.getContext().getString(R.string.going);
            }else{
                int acceptNum = message.getNum1();
                return acceptNum + " " + presenter.getContext().getString(R.string.accept);
            }
        }
        return "this is invitee";
    }

    public String getTag2(int position, List<Message> messages){
        Message message = messages.get(position);
        Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)){
            if (event.getStatus().equals("confirmed")){
                int noReplyNum = message.getNum3();
                return noReplyNum + " " + presenter.getContext().getString(R.string.no_reply);
            }else{
                int rejectNum = message.getNum2();
                return rejectNum + " " + presenter.getContext().getString(R.string.reject);
            }
        }
        return "this is invitee";
    }

    public String getTag3(int position, List<Message> messages){
        Message message = messages.get(position);
        Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)){
            if (event.getStatus().equals("confirmed")){
                return null;
            }else{
                int noReplyNum = message.getNum3();
                return noReplyNum + " " + presenter.getContext().getString(R.string.no_reply);
            }
        }
        return "this is invitee";
    }

    public String getTitle(int position, List<Message> messages){
        Message message = messages.get(position);
        return message.getTitle();
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
        Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
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
        Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
        if (event.getStatus().equals("confirmed")){
            view.setVisibility(View.INVISIBLE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
    }

    public String getTime(int position, List<Message> messages){
        // need to change later
        Message message = messages.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = sdf.parse(message.getUpdatedAt());
            Calendar calendar = Calendar.getInstance();

        }catch (Exception e){
            throw new RuntimeException("format updated time error");
        }

        return message.getUpdatedAt();
    }

    public String getSubtitle1(int position, List<Message> messages){
        Message message = messages.get(position);
        return message.getSubtitle1();
    }

    public String getSubtitle2(int position, List<Message> messages){
        Message message = messages.get(position);
        return message.getSubtitle2();
    }

    @Bindable
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyPropertyChanged(BR.messages);
    }
}
