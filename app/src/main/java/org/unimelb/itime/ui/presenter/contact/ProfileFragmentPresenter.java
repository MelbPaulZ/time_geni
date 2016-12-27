package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 37925 on 2016/12/14.
 */

public class ProfileFragmentPresenter extends MvpBasePresenter<ProfileMvpView> {
    private static final String TAG = "profile";
    private Context context;
    private UserApi userApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;

    public ProfileFragmentPresenter(Context context) {
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context, FriendRequestApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onBackPressed(){
        getView().getActivity().onBackPressed();
    }

    public void blockUser(ITimeUser user, final ProfileFragmentViewModel.BlockCallBack callBack){
        Observable<HttpResult<Block>> observable = userApi.block(Integer.parseInt(user.getContactUid()));
        Subscriber<HttpResult<Block>> subscriber = new Subscriber<HttpResult<Block>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Block> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    callBack.fail();
                }else {
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void unblockUser(ITimeUser user, final ProfileFragmentViewModel.UnblockCallBack callBack){
        Observable<HttpResult<Block>> observable = userApi.unblock(Integer.parseInt(user.getContactUid()));
        Subscriber<HttpResult<Block>> subscriber = new Subscriber<HttpResult<Block>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Block> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    callBack.fail();
                }else {
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void deleteUser(ITimeUser user, final ProfileFragmentViewModel.DeleteCallBack callBack){
        Observable<HttpResult<Contact>> observable = contactApi.delete(user.getContactUid());
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
                    DBManager dbManager = DBManager.getInstance(context);
                    dbManager.deleteContac(result.getData());
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void addUser(ITimeUser user, final ProfileFragmentViewModel.AddCallBack callBack){
        Observable<HttpResult<FriendRequest>> observable = requestApi.send(user.getContactUid(), FriendRequest.SOURCE_ITIME);
        Subscriber<HttpResult<FriendRequest>> subscriber = new Subscriber<HttpResult<FriendRequest>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<FriendRequest> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    callBack.fail();
                }else {
                   callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void inviteUser(ITimeUser user){
        getView().goToInviteFragment(user);
    }
}
