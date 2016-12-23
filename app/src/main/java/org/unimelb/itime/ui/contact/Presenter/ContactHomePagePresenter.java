package org.unimelb.itime.ui.contact.Presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Dao.InviteeDao;
import org.unimelb.itime.ui.contact.MvpView.ContactHomePageMvpView;
import org.unimelb.itime.ui.contact.ViewModel.ContactHomePageViewModel;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 37925 on 2016/12/13.
 */

public class ContactHomePagePresenter extends MvpBasePresenter<ContactHomePageMvpView>{
    private static final String TAG = "ContactPresenter";
    private Context context;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;
    private ContactHomePageViewModel.FriendListLisener friendListLisener;
    private ContactHomePageViewModel.RequestCountListener requestCountListener;

    public ContactHomePagePresenter(Context context) {
        this.context = context;
        contactApi =  HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context,FriendRequestApi.class);
    }

    public ContactHomePageViewModel.FriendListLisener getFriendListLisener() {
        return friendListLisener;
    }

    public void setFriendListLisener(ContactHomePageViewModel.FriendListLisener friendListLisener) {
        this.friendListLisener = friendListLisener;
    }

    public void setRequestCountListener(ContactHomePageViewModel.RequestCountListener listener){
        requestCountListener = listener;
    }

    public void goToProfileFragment(ITimeUser user){
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

    public void getRequestCount(){
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
                    if(friendListLisener!=null){
                        int count = 0;
                        for(FriendRequest request:result.getData()){
                            if(!request.isRead()){
                                count++;
                            }
                        }
                        requestCountListener.setCount(count);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void getFriends(){
//        List<ITimeUser> list = new InviteeDao().getITimeFriends();
        Observable<HttpResult<List<Contact>>> observable = contactApi.list();
        Subscriber<HttpResult<List<Contact>>> subscriber = new Subscriber<HttpResult<List<Contact>>>() {
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
            public void onNext(HttpResult<List<Contact>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {
                    if(friendListLisener!=null){
                        friendListLisener.setList(generateITimeUserList(result.getData()));
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
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
        return getView().getActivity().getResources().getColor(R.color.matchColor);
    }

    public SideBarListView getSideBarListView(){
        return getView().getBinding().sortListView;
    }
}
