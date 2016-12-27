package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

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

    public void refreshTabStatus(int pageId) {
        MainTabBarView tabBarView = getView();
        if (tabBarView == null){
            return;
        }
        // call back ui
        tabBarView.refreshTabStatus(pageId);
    }

    public void getRequestCount(){
        FriendRequestApi requestApi = HttpUtil.createService(context, FriendRequestApi.class);
        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.list();
        Subscriber<HttpResult<List<FriendRequest>>> subscriber = new Subscriber<HttpResult<List<FriendRequest>>>() {
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
            public void onNext(HttpResult<List<FriendRequest>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {
                        int count = 0;
                        for(FriendRequest request:result.getData()){
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
