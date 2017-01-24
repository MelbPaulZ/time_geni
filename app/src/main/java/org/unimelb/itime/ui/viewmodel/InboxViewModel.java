package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.SearchBar;

import java.util.ArrayList;
import java.util.List;

import static org.unimelb.itime.ui.presenter.contact.ContextPresenter.getContext;


/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends CommonViewModel {
    private final static String TAG = "InboxViewModel";
    private MainInboxPresenter presenter;
    private DBManager dbManager;
    private List<ItemViewModel> list;


    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
        this.list = new ArrayList<>();
        this.dbManager = DBManager.getInstance(getContext());
    }


    public AdapterView.OnItemClickListener onItemClicked(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = list.get(i).getMessage();
                if (!message.getIsRead()){
                    //unread to read
                    message.setRead(true);
                    message.update();
                    presenter.markAsRead(message);
                }

                // check the event before going to event detail page
                Event event = dbManager.getEvent(message.getEventUid());
                if (event == null){
                    presenter.fetchEvent("-1", message.getEventUid());
                }else {
                    presenter.getView().toEventDetailPage(event);
                }
            }
        };
    }

    public SwipeMenuListView.OnMenuItemClickListener onDeleteClicked(){
        return new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Message msg = DBManager.getInstance(getContext()).getAllMessages().get(position);
                msg.setDeleteLevel(1);
                msg.update();
                presenter.delete(msg);
                return false;
            }
        };
    }

    /**
     * search bar
     * @return
     */
    public SearchBar.OnEditListener onSearching(){
        return new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                presenter.getFilter().filter(text);
            }
        };
    }

    public List<ItemViewModel> getList() {
        return list;
    }

    public void setList(List<ItemViewModel> list) {
        this.list = list;
    }


    /**
     * the view model of each host
     */
    public static class ItemViewModel extends CommonViewModel{
        private MainInboxPresenter presenter;
        private String timeString = "";
        private Message message;
        private Context context;

        public ItemViewModel(MainInboxPresenter presenter, Message message){
            this.presenter = presenter;
            this.context = presenter.getContext();
            setMessage(message);
        }

        public String getTag1() {
            if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED_NORMAL)) {
                int goingNum = message.getNum1();
                return goingNum + " " + context.getString(R.string.going);
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_NORMAL)) {
                int acceptNum = message.getNum1();
                return acceptNum + " " + context.getString(R.string.accept);
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED)) {
                int acceptNum = message.getNum1();
                int totalNum = message.getNum1() + message.getNum2() + message.getNum3();
                return acceptNum + "/" + totalNum + " " + context.getString(R.string.going);
            }
            return "type not find" + message.getTemplate();

        }

        public String getTag2() {
            if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED_NORMAL)) {
                int noReplyNum = message.getNum3();
                return noReplyNum + " " + context.getString(R.string.no_reply);
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_NORMAL)){
                int rejectNum = message.getNum2();
                return rejectNum + " " + context.getString(R.string.reject);
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED)){
                int notGoingNum = message.getNum2();
                int totalNum = message.getNum1() + message.getNum2() + message.getNum3();
                return notGoingNum + "/" + totalNum + " " + context.getString(R.string.Not_going);
            }
            return "type not find" + message.getTemplate();

        }

        public String getTag3() {
            Log.i("message3", "getTag3: " + message.getTitle());
            if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED_NORMAL)) {
                return "";
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_NORMAL)){
                int noReplyNum = message.getNum3();
                return noReplyNum + " " + context.getString(R.string.no_reply);
            }else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED)){
                int noReplyNum = message.getNum3();
                int totalNum = message.getNum1() + message.getNum2() + message.getNum3();
                return noReplyNum + "/" + totalNum + " " + context.getString(R.string.no_reply);
            }
            return "type not find" + message.getTemplate();

        }


        @BindingAdapter({"bind:dotVisible"})
        public static void setDotVisible(ImageView view, Message message) {
            if (message.isHasBadge()) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }

        @BindingAdapter({"bind:message", " bind:tagNum"})
        public static void setTagBackground(TextView view, Message message, int tagNum) {
            if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED_NORMAL)) {
                if (tagNum == 1) {
                    view.setBackgroundResource(R.drawable.inbox_host_tag_green);
                } else if (tagNum == 2) {
                    view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
                } else if (tagNum == 3) {
                    view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
                }
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_NORMAL)){
                if (tagNum == 1) {
                    view.setBackgroundResource(R.drawable.inbox_host_tag_blue);
                } else if (tagNum == 2) {
                    view.setBackgroundResource(R.drawable.inbox_host_tag_red);
                } else if (tagNum == 3) {
                    view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
                }
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED)){
                // all gray for deleted tag
                view.setBackgroundResource(R.drawable.inbox_host_tag_gray);
            }

            //alpha
            view.getBackground().setAlpha(message.isRead()?155:255);
        }

        // for the tag3 visibility
        @BindingAdapter({"bind:visible"})
        public static void setVisible(TextView view, Message message) {
            if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED_NORMAL)) {
                view.setVisibility(View.GONE);
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_NORMAL)){
                view.setVisibility(View.VISIBLE);
            } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED)){
                view.setVisibility(View.VISIBLE);
            }
        }

        @BindingAdapter({"bind:crossLine"})
        public static void setCrossLine(TextView view, Message message){
            if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED) || message.getTemplate().equals(Message.TPL_INVITEE_DELETED)){
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
//        if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED_NORMAL)) {
//            setTag3Visible(View.GONE);
//        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_NORMAL)){
//            setTag3Visible(View.VISIBLE);
//        } else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED_DELETED)){
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

            this.timeString = EventUtil.parseRelativeTime(presenter.getContext(), timeString);
            notifyPropertyChanged(BR.timeString);
        }
    }


}
