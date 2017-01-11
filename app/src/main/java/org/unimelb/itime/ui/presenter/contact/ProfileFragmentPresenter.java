package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.ui.viewmodel.contact.RequestFriendItemViewModel;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

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
    DBManager dbManager;

    public ProfileFragmentPresenter(Context context) {
        this.context = context;
        dbManager = DBManager.getInstance(getContext());
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

    public void acceptRequest(final FriendRequest request, final ProfileFragmentViewModel.AcceptCallBack callBack){
        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.confirm(request.getFreqUid());
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
                    FriendRequest resultRequest = result.getData().get(0);
                    resultRequest.setUserDetail(request.getUserDetail());
                    dbManager.insertFriendRequest(resultRequest);
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void blockUser(final Contact user, final ProfileFragmentViewModel.BlockCallBack callBack){
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
                    user.setBlockLevel(result.getData().getBlockLevel());
                    dbManager.updateContact(user);
                    EventBus.getDefault().post(new MessageRemoveContact(user));
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void unblockUser(final Contact user, final ProfileFragmentViewModel.UnblockCallBack callBack){
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
                    user.setBlockLevel(result.getData().getBlockLevel());
                    dbManager.updateContact(user);
                    EventBus.getDefault().post(new MessageAddContact(user));
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void deleteUser(final Contact user, final ProfileFragmentViewModel.DeleteCallBack callBack){
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
                    user.setStatus(result.getData().getStatus());
                    user.setRelationship(user.getRelationship()-1);
                    dbManager.updateContact(user);
                    EventBus.getDefault().post(new MessageRemoveContact(result.getData()));
                    callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void addUser(Contact user, final ProfileFragmentViewModel.AddCallBack callBack){
        Observable<HttpResult<Void>> observable = requestApi.send(user.getContactUid(), FriendRequest.SOURCE_ITIME);
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
                   callBack.success();
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void inviteUser(Contact user){
        if(getView()!=null) {
            getView().goToInviteFragment(user);
        }
    }

    public void gotoEditAlias(Contact contact) {
        if(getView()!=null){
            getView().goToEditAlias(contact);
        }
    }
}
