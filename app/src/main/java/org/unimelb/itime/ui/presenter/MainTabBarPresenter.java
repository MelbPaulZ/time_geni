package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestResult;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yinchuandong on 16/08/2016.
 */
public class MainTabBarPresenter extends MvpBasePresenter<MainTabBarView>{

    private static final String TAG = "MainTabBar";
    private Context context;

    public MainTabBarPresenter(Context context){
        this.context = context;
    }

    public void showFragmentById(int pageId) {
        MainTabBarView tabBarView = getView();
        if (tabBarView == null){
            return;
        }
        // call back ui
        tabBarView.showFragmentById(pageId);
    }

    public void getRequestCount(){
        FriendRequestApi requestApi = HttpUtil.createService(context, FriendRequestApi.class);
        Observable<HttpResult<FriendRequestResult>> observable = requestApi.list();
        Subscriber<HttpResult<FriendRequestResult>> subscriber = new Subscriber<HttpResult<FriendRequestResult>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpResult<FriendRequestResult> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {
                        int count = 0;
                        for(FriendRequest request:result.getData().getReceive()){
                            if(!request.isRead()){
                                count++;
                            }
                        }

                    for(FriendRequest request:result.getData().getSend()){
                        if(!request.isRead()){
                            count++;
                        }
                    }
                    EventBus.getDefault().post(new MessageNewFriendRequest(count));
                    }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
