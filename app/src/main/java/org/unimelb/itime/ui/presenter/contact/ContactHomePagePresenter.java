package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;
import org.unimelb.itime.widget.SideBarListView;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by 37925 on 2016/12/13.
 */

public class ContactHomePagePresenter extends MvpBasePresenter<MainContactsMvpView>{
    private static final String TAG = "ContactPresenter";
    public static final int TASK_CONTACTS = 0;
    private Context context;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;

    public ContactHomePagePresenter(Context context) {
        this.context = context;
        contactApi =  HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context,FriendRequestApi.class);
    }

    public void goToProfileFragment(Contact user){
        if(getView()!=null)
            getView().goToProfileFragment(user);
    }

    public void goToNewFriendFragment(){
        if(getView()!=null)
            getView().goToNewFriendFragment();
    }

    public void goToAddFriendsFragment(){
        if(getView()!=null)
            getView().goToAddFriendsFragment();
    }

    public void getFriends(){
        DBManager dbManager = DBManager.getInstance(context);
        List<ITimeUser> list = generateITimeUserList(dbManager.getAllContact());
        if(getView()!=null){
            getView().onTaskSuccess(TASK_CONTACTS, list);
        }
        getFriendsFromServer();
    }

    public void getFriendsFromServer(){
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
                if(getView()!=null){
                    getView().onTaskError(TASK_CONTACTS, null);
                }
            }

            @Override
            public void onNext(List<Contact> list) {
                if(list == null){
                    if(getView()!=null){
                        getView().onTaskError(TASK_CONTACTS, null);
                    }
                }else {
                    if(getView()!=null){
                        getView().onTaskSuccess(TASK_CONTACTS, generateITimeUserList(list));
                    }
                }
            }
        };
        HttpUtil.subscribe(dbObservable, subscriber);
    }

    private List<ITimeUser> generateITimeUserList(List<Contact> list){
        List<ITimeUser> result = new ArrayList<>();
        for(Contact contact: list){
                ITimeUser user = new ITimeUser(contact);
                result.add(user);
        }
        return result;
    }

    public int getMatchColor(){
        return context.getResources().getColor(R.color.matchColor);
    }

    public SideBarListView getSideBarListView(){
        return getView().getBinding().sortListView;
    }
}
