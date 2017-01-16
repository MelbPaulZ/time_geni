package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.managers.MessageManager;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */
public class MainInboxPresenter extends MvpBasePresenter<MainInboxMvpView> {
    public static final int TASK_EVENT_GET = 4;

    private String TAG = "MainInboxPresenter";
    private Context context;
    private MessageApi messageApi;
    private EventApi eventApi;

    public MainInboxPresenter(Context context) {
        this.context = context;
        messageApi = HttpUtil.createService(context, MessageApi.class);
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void updateMessage(Message message){
        int isRead = message.getIsRead() == true? 1 : 0;
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
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<Void> voidHttpResult) {
                Log.i(TAG, "onNext: " + voidHttpResult.getStatus());
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void deleteMessage(final Message message){
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
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Void> voidHttpResult) {
                MessageManager.getInstance().deleteWaitMessage(message);
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvent(String calendarUid, String eventUid){
        if (getView() != null){
            getView().onTaskStart(TASK_EVENT_GET);
        }
        Observable<HttpResult<Event>> observable = eventApi.get(
                calendarUid
                , eventUid);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "eventApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "eventApi" + e.getMessage());
                if (getView() != null){
                    getView().onTaskError(TASK_EVENT_GET);
                }
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                EventManager.getInstance(context).updateDB(Arrays.asList(eventHttpResult.getData()));
                if (getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_GET,Arrays.asList(eventHttpResult.getData()));
                }
            }

        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
