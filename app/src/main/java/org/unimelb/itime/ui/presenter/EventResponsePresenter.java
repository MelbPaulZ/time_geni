package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.EventResponse;
import org.unimelb.itime.restfulapi.EventResponseApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventResponseMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class EventResponsePresenter extends MvpBasePresenter<EventResponseMvpView> {

    private Context context;
    private final static String TAG = "EventResponsePresenter ";
    private EventResponseApi api;
    public final static int TASK_LOAD_REMOTE = 0;
    public final static int TASK_LOAD_LOCAL = 1;

    public EventResponsePresenter(Context context){
        this.context = context;
        this.api = HttpUtil.createService(context, EventResponseApi.class);
    }

    public Context getContext(){
        return this.context;
    }


    public void loadData(String eventUid){
        Observable<HttpResult<List<EventResponse>>> observable = api.list(eventUid, "");
        Subscriber<HttpResult<List<EventResponse>>> subscriber = new Subscriber<HttpResult<List<EventResponse>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<EventResponse>> ret) {
                if(ret.getStatus() == 1){
                    if(getView() != null){
                        getView().onTaskSuccess(TASK_LOAD_REMOTE, ret.getData());
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }




}
