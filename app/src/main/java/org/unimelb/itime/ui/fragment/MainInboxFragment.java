package org.unimelb.itime.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainInboxFragment extends BaseUiAuthFragment{
    private MessageApi msgApi;

    public MainInboxFragment() {
        msgApi = HttpUtil.createService(getContext(), MessageApi.class);
    }

    @Override
    public MvpPresenter createPresenter() {
        return new MainInboxPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_inbox, container, false);
    }

    public void postMessageUpdate(Message msg){
        Observable<HttpResult<String>> observable = msgApi.read(msg.getEventUid(),msg.isRead() ? 1 : 0);
        Subscriber<HttpResult<String>> subscriber = new Subscriber<HttpResult<String>>(){

            public static final String TAG = "postMessageUpdate";

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<String> stringHttpResult) {
                Log.i(TAG, "onNext: ");

            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }
}
