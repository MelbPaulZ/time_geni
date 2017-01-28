package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Paul on 3/10/16.
 */
public class MainInboxPresenter extends MvpBasePresenter<MainInboxMvpView> implements Filterable {
    public static final int TASK_MSG_READ_ONE = 1;
    public static final int TASK_MSG_DELETE = 2;
    public static final int TASK_MSG_CLEAR = 3;
    public static final int TASK_EVENT_GET = 4;
    public static final int TASK_MSG_READ_MANY = 5;

    private String TAG = "MainInboxPresenter";
    private Context context;
    private MessageApi messageApi;
    private EventApi eventApi;
    private MessageFilter filter;
    private TokenUtil tokenUtil;
    private UserUtil userUtil;
    private DBManager dbManager;
    private EventManager eventManager;

    public MainInboxPresenter(Context context) {
        this.context = context;
        messageApi = HttpUtil.createService(context, MessageApi.class);
        eventApi = HttpUtil.createService(context, EventApi.class);
        filter = new MessageFilter();

        tokenUtil = TokenUtil.getInstance(context);
        dbManager = DBManager.getInstance(context);
        eventManager = EventManager.getInstance(context);
        userUtil = UserUtil.getInstance(context);
    }

    public Context getContext() {
        return context;
    }

    /**
     * save the message and syncToken to db
     */
    private class MessageSaver implements Func1<HttpResult<List<Message>>, HttpResult<List<Message>>> {
        @Override
        public HttpResult<List<Message>> call(HttpResult<List<Message>> ret) {
            if(ret.getStatus() == 1 && ret.getData().size() > 0){
                dbManager.insertMessageList(ret.getData());
                tokenUtil.setMessageToken(userUtil.getUserUid(), ret.getSyncToken());
            }
            return ret;
        }
    }

    private class MessageSubscriber extends Subscriber<HttpResult<List<Message>>>{
        private int task;

        public MessageSubscriber(int task){
            this.task = task;
        }

        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
            if(getView() != null){
                getView().onTaskError(task, e.getMessage());
            }
        }

        @Override
        public void onNext(HttpResult<List<Message>> ret) {
            if(getView() == null){
                return;
            }
            if(ret.getStatus() == 1){
                getView().onTaskSuccess(task, ret.getData());
            }else{
                getView().onTaskError(task, ret.getInfo());
            }
        }
    }

    public void markAsRead(Message message){
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        markAsRead(messageList, true);
    }

    public void markAsRead(List<Message> messageList, boolean isRead){
        if(getView() != null){
            getView().onTaskStart(messageList.size() > 1 ? TASK_MSG_READ_MANY : TASK_MSG_READ_ONE);
        }
        ArrayList<String> uids = new ArrayList<>();
        for(Message msg: messageList){
            uids.add(msg.getMessageUid());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageUids", uids);
        map.put("isRead", isRead ? 1 : 0);
        String syncToken = tokenUtil.getMessageToken(userUtil.getUserUid());
        Observable<HttpResult<List<Message>>> observable = messageApi.read(map, syncToken).map(new MessageSaver());
        Subscriber<HttpResult<List<Message>>> subscriber = new MessageSubscriber(messageList.size() > 1 ? TASK_MSG_READ_MANY : TASK_MSG_READ_ONE);
        HttpUtil.subscribe(observable, subscriber);
    }

    public void clearAll(){
        if(getView() != null){
            getView().onTaskStart(TASK_MSG_CLEAR);
        }
        String syncToken = tokenUtil.getMessageToken(userUtil.getUserUid());
        Observable<HttpResult<List<Message>>> observable = messageApi.clear(syncToken).map(new MessageSaver());
        Subscriber<HttpResult<List<Message>>> subscriber = new MessageSubscriber(TASK_MSG_CLEAR);
        HttpUtil.subscribe(observable, subscriber);
    }

    public void delete(final Message message){
        if(getView() != null){
            getView().onTaskStart(TASK_MSG_DELETE);
        }
        ArrayList<String> messageList = new ArrayList<>();
        messageList.add(message.getMessageUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("messageUids", messageList);
        String syncToken = tokenUtil.getMessageToken(userUtil.getUserUid());
        Observable<HttpResult<List<Message>>> observable = messageApi.delete(map, syncToken).map(new MessageSaver());
        Subscriber<HttpResult<List<Message>>> subscriber = new MessageSubscriber(TASK_MSG_DELETE);

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchMessages() {
        final String token = tokenUtil.getMessageToken(userUtil.getUserUid());
        Observable<HttpResult<List<Message>>> observable = messageApi.get(token)
                .map(new Func1<HttpResult<List<Message>>, HttpResult<List<Message>>>() {
                    @Override
                    public HttpResult<List<Message>> call(HttpResult<List<Message>> ret) {
                        if (ret.getStatus() == 1) {
                            List<Message> msgs = ret.getData();
                            dbManager.insertMessageList(msgs);
                            Log.i(TAG, "call: new message size:" + msgs.size());
                            tokenUtil.setMessageToken(userUtil.getUserUid(), ret.getSyncToken());
                            EventBus.getDefault().post(new MessageInboxMessage(ret.getData()));

                        }
                        return ret;
                    }
                });
        Subscriber<HttpResult<List<Message>>> subscriber = new Subscriber<HttpResult<List<Message>>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "messageApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "messageApi" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Message>> ret) {

            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }


    public void fetchEvent(String calendarUid, String eventUid){
        if (getView() != null){
            getView().onTaskStart(TASK_EVENT_GET);
        }
        Observable<HttpResult<Event>> observable = eventApi
            .get(calendarUid, eventUid)
            .map(new Func1<HttpResult<Event>, HttpResult<Event>>() {
                @Override
                public HttpResult<Event> call(HttpResult<Event> ret) {
                    if(ret.getStatus() == 1){
                        EventManager.getInstance(context).updateDB(Arrays.asList(ret.getData()));
                    }
                    return ret;
                }
            });
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "eventApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "eventApi" + e.getMessage());
                if (getView() != null){
                    getView().onTaskError(TASK_EVENT_GET, null);
                }
            }

            @Override
            public void onNext(HttpResult<Event> ret) {
                if (getView() != null){
                    return;
                }
                if(ret.getStatus() == 1){
                    getView().onTaskSuccess(TASK_EVENT_GET, ret.getData());
                }else{
                    getView().onTaskError(TASK_EVENT_GET, ret.getInfo());
                }
            }

        };
        HttpUtil.subscribe(observable, subscriber);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class MessageFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Message> matchList = new ArrayList<>();
                for (Message message : DBManager.getInstance(context).getAllMessages()) {
                    if (message.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        matchList.add(message);
                    }
                }

                filterResults.values = matchList;
                filterResults.count = matchList.size();
            } else {
                filterResults.values = DBManager.getInstance(context).getAllMessages();
                filterResults.count = DBManager.getInstance(context).getAllMessages().size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(getView() != null){
                getView().onFilterMessage((List<Message>) results.values);
            }
        }
    }


}
