package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;


import org.unimelb.itime.R;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.EventUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends CommonViewModel {
    private MainInboxPresenter presenter;
    private Message message;
    private String timeString = "";
    private final String TAG = "InboxViewModel";

//    private int tag3Visible = View.VISIBLE;



    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
    }

    public String getTag1(Context context, Message message) {
        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
            int goingNum = message.getNum1();
            return goingNum + " " + context.getString(R.string.going);
        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)) {
            int acceptNum = message.getNum1();
            return acceptNum + " " + context.getString(R.string.accept);
        } else if (message.getTemplate().equals(Message.TPL_HOST_DELETED)) {
            int acceptNum = message.getNum1();
            int totalNum = message.getNum1() + message.getNum2() + message.getNum3();
            return acceptNum + "/" + totalNum + " " + context.getString(R.string.going);
        }
        return "type not find" + message.getTemplate();

    }

    public String getTag2(Context context, Message message) {
        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
            int noReplyNum = message.getNum3();
            return noReplyNum + " " + context.getString(R.string.no_reply);
        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)){
            int rejectNum = message.getNum2();
            return rejectNum + " " + context.getString(R.string.reject);
        } else if (message.getTemplate().equals(Message.TPL_HOST_DELETED)){
            int notGoingNum = message.getNum2();
            int totalNum = message.getNum1() + message.getNum2() + message.getNum3();
            return notGoingNum + "/" + totalNum + " " + context.getString(R.string.Not_going);
        }
        return "type not find" + message.getTemplate();

    }

    public String getTag3(Context context, Message message) {
        Log.i("message3", "getTag3: " + message.getTitle());
        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
            return "";
        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)){
            int noReplyNum = message.getNum3();
            return noReplyNum + " " + context.getString(R.string.no_reply);
        }else if (message.getTemplate().equals(Message.TPL_HOST_DELETED)){
            int noReplyNum = message.getNum3();
            int totalNum = message.getNum1() + message.getNum2() + message.getNum3();
            return noReplyNum + "/" + totalNum + " " + context.getString(R.string.no_reply);
        }
        return "type not find" + message.getTemplate();

    }


    @BindingAdapter({"bind:dotVisible"})
    public static void setDotVisible(ImageView view, Message message) {
        if (message.isRead()) {
            Log.i("reddot", "setDotVisible: + gone");
            view.setVisibility(View.GONE);
        } else {
            Log.i("reddot", "setDotVisible: + visible");
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter({"bind:message", " bind:tagNum"})
    public static void setTagBackground(TextView view, Message message, int tagNum) {
        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
            if (tagNum == 1) {
                view.setBackgroundResource(R.drawable.inbox_host_tag_green);
            } else if (tagNum == 2) {
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            } else if (tagNum == 3) {
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            }
        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)){
            if (tagNum == 1) {
                view.setBackgroundResource(R.drawable.inbox_host_tag_blue);
            } else if (tagNum == 2) {
                view.setBackgroundResource(R.drawable.inbox_host_tag_red);
            } else if (tagNum == 3) {
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            }
        } else if (message.getTemplate().equals(Message.TPL_HOST_DELETED)){
            // all gray for deleted tag
            view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
        }
    }


    // for the tag3 visibility
    @BindingAdapter({"bind:visible"})
    public static void setVisible(TextView view, Message message) {
        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
            view.setVisibility(View.GONE);
        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)){
            view.setVisibility(View.VISIBLE);
        } else if (message.getTemplate().equals(Message.TPL_HOST_DELETED)){
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter({"bind:crossLine"})
    public static void setCrossLine(TextView view, Message message){
        if (message.getTemplate().equals(Message.TPL_HOST_DELETED) || message.getTemplate().equals(Message.TPL_INVITEE_DELETED)){
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            view.setPaintFlags(view.getPaintFlags() &(~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }



    @Bindable
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
//        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
//            setTag3Visible(View.GONE);
//        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)){
//            setTag3Visible(View.VISIBLE);
//        } else if (message.getTemplate().equals(Message.TPL_HOST_DELETED)){
//            setTag3Visible(View.VISIBLE);
//        }
        setTimeString(message.getUpdatedAt());
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {

        Calendar updateTimeCalendar = EventUtil.parseTimeStringToCalendar(timeString);
        Calendar now = Calendar.getInstance();
        this.timeString = getDatesRelationType(now, updateTimeCalendar);
        notifyPropertyChanged(BR.timeString);
    }

    /**
     *
     * @param todayM is current time
     * @param comparedTime is compared time
     * @return
     */
    private String getDatesRelationType(Calendar todayM, Calendar comparedTime){

        Calendar todayMCalendar = EventUtil.getBeginOfDayCalendar(todayM);
        Calendar currentDayMCalendar = EventUtil.getBeginOfDayCalendar(comparedTime);

        long delta = currentDayMCalendar.getTimeInMillis() - todayMCalendar.getTimeInMillis();
        long oneDay = 24 * 60 * 60 * 1000;
        if (delta==0){
            int hour = comparedTime.get(Calendar.HOUR_OF_DAY);
            int min = comparedTime.get(Calendar.MINUTE);
            String h = hour<10? "0"+hour : ""+ hour;
            String m = min<10? "0"+min : "" + min;
            return h + ":" +  m;
        }else if (delta == oneDay){
            return presenter.getContext().getString(R.string.Yesterday);
        }else{
            int day = comparedTime.get(Calendar.DAY_OF_MONTH);
            int month = comparedTime.get(Calendar.MONTH) + 1;
            return month + "-" + day;
        }


//        // -2 no relation, 1 tomorrow, 0 today, -1 yesterday
//        int type = -2;
//        int dayM = 24 * 60 * 60 * 1000;
//        long diff = (currentDayMCalendar.getTimeInMillis() - todayMCalendar.getTimeInMillis());
//        if (diff >0 && diff <= dayM){
//            type = 1;
//        }else if(diff < 0 && diff >= -dayM){
//            type = -1;
//        }else if (diff == 0){
//            type = 0;
//        }
//
//        switch (type){
//            case 1:
//                mention = "Tomorrow";
//                break;
//            case 0:
//                mention = "Today";
//                break;
//            case -1:
//                mention = "Yesterday";
//                break;
//            default:
//                mention = "";
//                break;
//        }
//
//        return mention;
    }

//    @Bindable
//    public int getTag3Visible() {
//        return tag3Visible;
//    }
//
//    public void setTag3Visible(int tag3Visible) {
//        this.tag3Visible = tag3Visible;
//        notifyPropertyChanged(BR.tag3Visible);
//    }

}
