package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestResult;
import org.unimelb.itime.bean.RequestReadBody;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.ui.mvpview.contact.NewFriendMvpView;
import org.unimelb.itime.ui.viewmodel.contact.NewFriendViewModel;
import org.unimelb.itime.ui.viewmodel.contact.RequestFriendItemViewModel;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by 37925 on 2016/12/14.
 */

public class NewFriendFragmentPresenter extends MvpBasePresenter<NewFriendMvpView> {

    private static final String TAG = "Friend Request";
    private Context context;
    private FriendRequestApi requestApi;
    private ContactApi contactApi;
    private DBManager dbManager;

    public NewFriendFragmentPresenter(Context context){
        this.context = context;
        requestApi = HttpUtil.createService(context,FriendRequestApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        dbManager = DBManager.getInstance(context);
    }
    public void onBackPressed(){
        if(getView()!=null)
            getView().getActivity().onBackPressed();
    }

    public int getMatchColor(){
        if(getView()!=null){
            return getView().getActivity().getResources().getColor(R.color.matchColor);
        }
        return -1;
    }

    public Context getContext() {
        return context;
    }

    public void acceptRequest(final RequestFriend request, final RequestFriendItemViewModel.AcceptCallBack callBack){
        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.confirm(request.getRequest().getFreqUid());
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
                    callBack.fail();
                }else {
                    dbManager.insertFriendRequest(result.getData().get(0));
                    callBack.accept();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    // Set requests status to read
    public void setRead(String[] requestIds){
        Observable<HttpResult<Void>> observable = requestApi.read(new RequestReadBody(requestIds));
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Void> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {

                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void getRequestFriendList(final NewFriendViewModel.RequestListCallBack callBack) {
        List<FriendRequest> requests = dbManager.getAllFriendRequest();
        if(requests!=null){
            List<RequestFriend> list = new ArrayList<>();
            for(FriendRequest request:requests) {
                list.add(new RequestFriend(request));
            }
            callBack.success(list);
        }
        getRequestFriendListFromServer(callBack);
    }

    public void getRequestFriendListFromServer(final NewFriendViewModel.RequestListCallBack callBack){
        Observable<HttpResult<FriendRequestResult>> observable = requestApi.list();
        Observable<List<FriendRequest>> dbObservable = observable.map(new Func1<HttpResult<FriendRequestResult>, List<FriendRequest>>() {
            @Override
            public List<FriendRequest> call(HttpResult<FriendRequestResult> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    return null;
                }else {
                    for(FriendRequest request:result.getData().getSend()) {
                        dbManager.insertUser(request.getUser());
                        dbManager.insertFriendRequest(request);
                    }
                    for(FriendRequest request:result.getData().getReceive()) {
                        dbManager.insertUser(request.getUser());
                        dbManager.insertFriendRequest(request);
                    }
                    return DBManager.getInstance(context).getAllFriendRequest();
                }
            }
        });

        Subscriber<List<FriendRequest>> subscriber = new Subscriber<List<FriendRequest>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                e.printStackTrace();
                callBack.fail();
            }

            @Override
            public void onNext(List<FriendRequest> list) {
                if (list == null){
                    callBack.fail();
                }else {
                    List<String> unreadIds = new ArrayList<>();
                    List<RequestFriend> result = new ArrayList<>();
                    for(FriendRequest request:list) {
                        if(!request.isRead()){
                            unreadIds.add(request.getFreqUid());
                        }
                        result.add(new RequestFriend(request));
                    }

                    if(unreadIds.size()!=0) {
                        String[] array = new String[unreadIds.size()];
                        array = unreadIds.toArray(array);
                        //setRead(array);
                    }
                    EventBus.getDefault().post(new MessageNewFriendRequest(0));
                    callBack.success(result);
                }
            }
        };
        HttpUtil.subscribe(dbObservable, subscriber);
    }


}
