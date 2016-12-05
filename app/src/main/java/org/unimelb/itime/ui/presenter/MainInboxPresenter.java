package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Message;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */
public class MainInboxPresenter extends MvpBasePresenter<MainInboxMvpView> {
    private String TAG = "MainInboxPresenter";
    private Context context;
    private MessageApi messageApi;

    public MainInboxPresenter(Context context) {
        this.context = context;
        messageApi = HttpUtil.createService(context, MessageApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void updateMessage(Message message){
        int isRead = message.getIsRead() == true? 1 : 0;
        ArrayList<String> messageList = new ArrayList<>();
        messageList.add(message.getMessageUid());
        Observable<HttpResult<Void>> observable = messageApi.read(messageList, isRead);
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
}
