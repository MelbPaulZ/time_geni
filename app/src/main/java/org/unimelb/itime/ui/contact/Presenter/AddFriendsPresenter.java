package org.unimelb.itime.ui.contact.Presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Dao.InviteeDao;
import org.unimelb.itime.ui.contact.MvpView.AddFriendsMvpView;
import org.unimelb.itime.ui.contact.ViewModel.AddFriendsViewModel;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 37925 on 2016/12/13.
 */

public class AddFriendsPresenter extends MvpBasePresenter<AddFriendsMvpView> {

    private static final String TAG = "AddFriend";
    private Context context;
    private UserApi userApi;
    public AddFriendsPresenter(Context context){
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
    }
    public void findFriend(String searchStr, final AddFriendsViewModel.SearchUserCallBack callBack){
        Observable<HttpResult<List<User>>> observable = userApi.search(searchStr);
        Subscriber<HttpResult<List<User>>> subscriber = new Subscriber<HttpResult<List<User>>>() {
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
            public void onNext(HttpResult<List<User>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());

                if (result.getStatus()!=1){

                }else {
                    if(result.getData().isEmpty()){
                        callBack.gotoProfile(null);
                    }else {
                        ITimeUser itimeUser = new ITimeUser(result.getData().get(0));
                        Log.d(TAG, "onNext: " + result.getData().get(0).getUserId());
                        callBack.gotoProfile(itimeUser);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void goToProfile(ITimeUser user, String show){
        getView().goToProfileFragment(user, show);
    }

    public void goToQRCode(){
        getView().goToScanQRCode();
    }

    public void goToGmail(){
        getView().goToAddGmailContacts();
    }

    public void goToFacebook(){
        getView().goToAddFacebookContacts();
    }

    public void goToMobile(){
        getView().goToAddMobileContacts();
    }

    public void onBackPress(){
        getView().getActivity().onBackPressed();
    }
}
