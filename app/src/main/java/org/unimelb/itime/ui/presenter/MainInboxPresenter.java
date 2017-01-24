package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.managers.MessageManager;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.util.HttpUtil;

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
    public static final int TASK_MSG_READ = 1;
    public static final int TASK_MSG_DELETE = 2;
    public static final int TASK_EVENT_GET = 4;

    private String TAG = "MainInboxPresenter";
    private Context context;
    private MessageApi messageApi;
    private EventApi eventApi;
    private MessageFilter filter;

    public MainInboxPresenter(Context context) {
        this.context = context;
        messageApi = HttpUtil.createService(context, MessageApi.class);
        eventApi = HttpUtil.createService(context, EventApi.class);
        filter = new MessageFilter();
    }

    public Context getContext() {
        return context;
    }

    public void markAsRead(Message message){
        if(getView() != null){
            getView().onTaskStart(TASK_MSG_READ);
        }
        int isRead = message.getIsRead() ? 1 : 0;
        ArrayList<String> messageList = new ArrayList<>();
        messageList.add(message.getMessageUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageUids", messageList);
        map.put("isRead", isRead);
        Observable<HttpResult<Void>> observable = messageApi.read(map);
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_MSG_READ, e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<Void> ret) {
                if(getView() == null){
                    return;
                }
                if(ret.getStatus() == 1){
                    getView().onTaskSuccess(TASK_MSG_READ, null);
                }else{
                    getView().onTaskError(TASK_MSG_READ, ret.getInfo());
                }
            }
        };
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

        MessageManager.getInstance().insertWaitMessage(message);
        Observable<HttpResult<Void>> observable = messageApi.delete(map);
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_MSG_DELETE, e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<Void> ret) {
                if(getView() == null){
                    return;
                }
                if(ret.getStatus() == 1){
                    MessageManager.getInstance().deleteWaitMessage(message);
                    getView().onTaskSuccess(TASK_MSG_DELETE, null);
                }else{
                    getView().onTaskError(TASK_MSG_DELETE, ret.getInfo());
                }
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
                    getView().onTaskSuccess(TASK_EVENT_GET, Arrays.asList(ret.getData()));
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
