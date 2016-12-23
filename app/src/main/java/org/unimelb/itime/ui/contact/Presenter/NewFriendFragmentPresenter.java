package org.unimelb.itime.ui.contact.Presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.contact.Beans.RequestFriend;
import org.unimelb.itime.ui.contact.MvpView.NewFriendMvpView;
import org.unimelb.itime.ui.contact.ViewModel.NewFriendViewModel;
import org.unimelb.itime.ui.contact.ViewModel.RequestFriendItemViewModel;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 37925 on 2016/12/14.
 */

public class NewFriendFragmentPresenter extends MvpBasePresenter<NewFriendMvpView> {

    private static final String TAG = "Friend Request";
    private Context context;
    private FriendRequestApi requestApi;
    private ContactApi contactApi;

    public NewFriendFragmentPresenter(Context context){
        this.context = context;
        requestApi = HttpUtil.createService(context,FriendRequestApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
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
        Observable<HttpResult<Void>> observable = requestApi.confirm(request.getRequest().getFreqUid());
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
                    callBack.fail();
                }else {
                    callBack.accept();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void getRequestFriendList(final NewFriendViewModel.RequestListCallBack callBack){
        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.list();
        Subscriber<HttpResult<List<FriendRequest>>> subscriber = new Subscriber<HttpResult<List<FriendRequest>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<FriendRequest>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    callBack.fail();
                }else {
                    for(FriendRequest request:result.getData()) {
                        getContact(request, callBack);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void getContact(final FriendRequest request, final NewFriendViewModel.RequestListCallBack callBack){
        Observable<HttpResult<Contact>> observable = contactApi.get(request.getUserUid());
        Subscriber<HttpResult<Contact>> subscriber = new Subscriber<HttpResult<Contact>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Contact> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    callBack.fail();
                }else {
                    request.setContact(result.getData());
                    callBack.success(new RequestFriend(request));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


}
