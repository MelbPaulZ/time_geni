package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.messageevent.contact.MessageBlockContact;
import org.unimelb.itime.messageevent.contact.MessageUnblockContact;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 37925 on 2016/12/14.
 */

public class ProfilePresenter extends MvpBasePresenter<ProfileMvpView> {
    private static final String TAG = "profile";
    public static final int TASK_ADD = 0;
    public static final int TASK_DELETE = 1;
    public static final int TASK_BLOCK = 2;
    public static final int TASK_UNBLOCK = 3;
    public static final int TASK_ACCEPT = 4;
    public static final int TASK_CONTACT = 5;
    public static final int TASK_STRANGER = 6;
    public static final int TASK_REQUEST = 7;

    private Context context;
    private UserApi userApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;
    DBManager dbManager;

    public ProfilePresenter(Context context) {
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

    public void acceptRequest(final FriendRequest request){
        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.confirm(request.getFreqUid());
        Subscriber<HttpResult<List<FriendRequest>>> subscriber = new Subscriber<HttpResult<List<FriendRequest>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                if(getView()!=null) {
                    getView().onTaskError(TASK_ACCEPT, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<FriendRequest>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {
                    FriendRequest resultRequest = result.getData().get(0);
                    resultRequest.setUserDetail(request.getUserDetail());
                    dbManager.insertFriendRequest(resultRequest);
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_ACCEPT, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void blockUser(final Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Block>> observable = userApi.block(Integer.parseInt(user.getContactUid()));
        Subscriber<HttpResult<Block>> subscriber = new Subscriber<HttpResult<Block>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                if(getView()!=null) {
                    getView().onTaskError(TASK_BLOCK, null);
                }
            }

            @Override
            public void onNext(HttpResult<Block> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_BLOCK, null);
                    }
                }else {
                    user.setBlockLevel(result.getData().getBlockLevel());
                    dbManager.updateContact(user);
                    Block block = result.getData();
                    block.setUserDetail(user.getUserDetail());
                    dbManager.insertBlock(block);
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_BLOCK, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void unblockUser(final Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Block>> observable = userApi.unblock(Integer.parseInt(user.getContactUid()));
        Subscriber<HttpResult<Block>> subscriber = new Subscriber<HttpResult<Block>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                if(getView()!=null) {
                    getView().onTaskError(TASK_UNBLOCK, null);
                }
            }

            @Override
            public void onNext(HttpResult<Block> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_UNBLOCK, null);
                    }
                }else {
                    user.setBlockLevel(result.getData().getBlockLevel());
                    dbManager.updateContact(user);
                    dbManager.deleteBlock(result.getData());
                    EventBus.getDefault().post(new MessageAddContact(user));
                    EventBus.getDefault().post(new MessageUnblockContact(user));
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_UNBLOCK, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void deleteUser(final Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Contact>> observable = contactApi.delete(user.getContactUid());
        Subscriber<HttpResult<Contact>> subscriber = new Subscriber<HttpResult<Contact>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                if(getView()!=null) {
                    getView().onTaskError(TASK_DELETE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Contact> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_DELETE, null);
                    }
                }else {
                    user.setStatus(result.getData().getStatus());
                    user.setRelationship(user.getRelationship()-1);
                    dbManager.updateContact(user);
                    EventBus.getDefault().post(new MessageRemoveContact(result.getData()));
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_DELETE, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void addUser(Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Void>> observable = requestApi.send(user.getContactUid(), FriendRequest.SOURCE_ITIME);
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                if(getView()!=null) {
                    getView().onTaskError(TASK_ADD, null);
                }
            }

            @Override
            public void onNext(HttpResult<Void> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_ADD, null);
                    }
                }else {
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_ADD, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void inviteUser(){
        if(getView()!=null) {
            getView().goToInviteFragment();
        }
    }

    public void gotoEditAlias() {
        if(getView()!=null){
            getView().goToEditAlias();
        }
    }

    public void getRequest(String userId){
        List<FriendRequest> requests = dbManager.getAllFriendRequest();
        for(FriendRequest request: requests){
            if (request.getUserDetail().getUserId().equals(userId)){
                getView().onTaskSuccess(TASK_REQUEST, new Contact(request.getUserDetail()));
                return;
            }
        }
    }

    public void getContact(String userId){
        if(userId==null || userId.equals("")){
            return;
        }

        userId = userId.trim();
        if(getView()!=null) {
            getView().onTaskStart(TASK_CONTACT);
            List<Contact> contacts = dbManager.getAllContact();
            for(Contact contact: contacts){
                if (contact.getUserDetail().getUserId().equals(userId)){
                    getView().onTaskSuccess(TASK_CONTACT, contact);
                    return;
                }
            }

            List<Block> blocks = dbManager.getBlockContacts();
            for(Block block: blocks){
                if (block.getUserDetail().getUserId().equals(userId)){
                    Contact contact = new Contact(block.getUserDetail());
                    contact.setBlockLevel(1);
                    getView().onTaskSuccess(TASK_CONTACT, contact);
                    return;
                }
            }

            User user = dbManager.searchUserByUserId(userId);
            if(user!=null){
                getView().onTaskSuccess(TASK_STRANGER, new Contact(user));
                return;
            }

            findUser(userId);
        }
    }

    private void findUser(String userId){
        if(userId==null || userId.equals("")){
            return;
        }

        Observable<HttpResult<List<User>>> observable = userApi.search(userId);
        Subscriber<HttpResult<List<User>>> subscriber = new Subscriber<HttpResult<List<User>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                if(getView()!=null) {
                    getView().onTaskError(TASK_STRANGER, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<User>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_STRANGER, null);
                    }
                }else {
                    if(result.getData().isEmpty()){
                        if(getView()!=null) {
                            getView().onTaskError(TASK_STRANGER, null);
                        }
                    }else {
                        User user = result.getData().get(0);
                        dbManager.insertUser(user);
                        if(getView()!=null) {
                            getView().onTaskSuccess(TASK_STRANGER, new Contact(user));
                        }
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
