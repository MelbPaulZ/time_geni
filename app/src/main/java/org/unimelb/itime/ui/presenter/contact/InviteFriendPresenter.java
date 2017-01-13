package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.bean.BaseContact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.viewmodel.contact.AddFriendsViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.ContactCheckUtil;
import org.unimelb.itime.ui.mvpview.contact.InviteFriendMvpView;
import org.unimelb.itime.ui.viewmodel.contact.InviteFriendViewModel;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.rulefactory.InviteeUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by 37925 on 2016/12/16.
 */

public class InviteFriendPresenter extends MvpBasePresenter<InviteFriendMvpView> {

    private static final String TAG = "Invitee Presenter";
    private Context context;
    private ContactApi contactApi;
    private UserApi userApi;

    public InviteFriendPresenter (Context context){
        this.context = context;
        contactApi = HttpUtil.createService(context,ContactApi.class);
        userApi = HttpUtil.createService(context, UserApi.class);
    }

    public Context getContext(){
        return context;
    }


    public void getFriends(final InviteFriendViewModel.FriendCallBack callBack){
        DBManager dbManager = DBManager.getInstance(context);
        List<BaseContact> list = generateITimeUserList(dbManager.getAllContact());
        callBack.success(list);
        getFriendsFromServer(callBack);
    }

    public void getFriendsFromServer(final InviteFriendViewModel.FriendCallBack callBack){
        final DBManager dbManager = DBManager.getInstance(context);
        Observable<HttpResult<List<Contact>>> observable = contactApi.list();
        Observable<List<Contact>> dbObservable = observable.map(new Func1<HttpResult<List<Contact>>, List<Contact>>() {
            @Override
            public List<Contact> call(HttpResult<List<Contact>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    return null;
                }else {
                    for(Contact contact:result.getData()) {
                        dbManager.insertContact(contact);
                    }
                    return DBManager.getInstance(context).getAllContact();
                }
            }
        });

        Subscriber<List<Contact>> subscriber = new Subscriber<List<Contact>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                e.printStackTrace();
                callBack.failed();
            }

            @Override
            public void onNext(List<Contact> list) {
                if(list == null){
                    callBack.failed();
                }else {
                    callBack.success(generateITimeUserList(list));
                }
            }
        };
        HttpUtil.subscribe(dbObservable, subscriber);
    }

//    public void getFriendsFromServer(final InviteFriendViewModel.FriendCallBack callBack){
//        final DBManager dbManager = DBManager.getInstance(context);
//        Observable<HttpResult<List<Contact>>> observable = contactApi.list();
//
//        Subscriber<HttpResult<List<Contact>>> subscriber = new Subscriber<HttpResult<List<Contact>>>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG, "onCompleted: ");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "onError: " + e.getMessage());
//                e.printStackTrace();
//                callBack.failed();
//            }
//
//            @Override
//            public void onNext(HttpResult<List<Contact>> result) {
//                Log.d(TAG, "onNext: " + result.getInfo());
//                if (result.getStatus()!=1){
//                    callBack.failed();
//                }else {
//                    for(Contact contact:result.getData()) {
//                        dbManager.insertUser(contact.getUser());
//                        dbManager.insertContact(contact);
//                    }
//                    List<Contact> list = DBManager.getInstance(context).getAllContact();
//                    callBack.success(generateITimeUserList(list));
//                }
//            }
//        };
//        HttpUtil.subscribe(observable, subscriber);
//    }

    private List<BaseContact> generateITimeUserList(List<Contact> list){
        List<BaseContact> result = new ArrayList<>();
        if(list!=null) {
            for (Contact contact : list) {
                if (contact.getBlockLevel() == 0) {
                    ITimeUser user = new ITimeUser(contact);
                    result.add(user);
                }
            }
        }
        return result;
    }

//    public List<BaseContact> getSearchList(){
//        return dao.getFriends();
//    }

    public void searchContact(String input, InviteFriendViewModel.SearchContactCallback callback){
        AppUtil.showProgressBar(context, context.getString(R.string.Searching), context.getString(R.string.please_wait));
        DBManager dbManager = DBManager.getInstance(context);
        List<Contact> contacts = dbManager.getAllContact();
        for(Contact contact:contacts){
            if(contact.getUserDetail().getPhone().equals(input)
                    || contact.getUserDetail().getEmail().equals(input)){
                callback.success(contact);
                AppUtil.hideProgressBar();
                return;
            }
        }

        findFriend(input, callback);
    }

    public void findFriend(final String searchStr, final InviteFriendViewModel.SearchContactCallback callback){
        AppUtil.showProgressBar(context, context.getString(R.string.Searching), context.getString(R.string.please_wait));
        Observable<HttpResult<List<User>>> observable = userApi.search(searchStr);
        Subscriber<HttpResult<List<User>>> subscriber = new Subscriber<HttpResult<List<User>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
                AppUtil.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                AppUtil.hideProgressBar();
                callback.failed();
            }

            @Override
            public void onNext(HttpResult<List<User>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {
                    if(result.getData().isEmpty()){
                        callback.success(searchStr);
                    }else {
                        User user = result.getData().get(0);
                        callback.success(new Contact(user));
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public int getMatchColor() {
        return context.getResources().getColor(R.color.matchColor);
    }

    public boolean isEmail(String str){
        return ContactCheckUtil.getInsstance().isEmail(str);
    }

    public boolean isPhone(String str){
        return ContactCheckUtil.getInsstance().isPhone(str);
    }

    public void onBackPress() {
        getView().onBackClicked();
    }

    public boolean isUniMelbEmail(String str) {
        return ContactCheckUtil.getInsstance().isUnimelbEmail(str);
    }

    public void onDoneClicked(){
        if(getView() != null){
            getView().onDoneClicked();
        }

    }
}
