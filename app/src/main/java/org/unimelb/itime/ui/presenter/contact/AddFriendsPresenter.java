package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.viewmodel.contact.AddFriendsViewModel;
import org.unimelb.itime.util.AppUtil;
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
        if(searchStr==null || searchStr.equals("")){
            return;
        }
        DBManager dbManager = DBManager.getInstance(context);
        searchStr = searchStr.trim();
        final List<Contact> contacts = dbManager.getAllContact();
        for(Contact contact:contacts){
            if(contact.getUserDetail().getPhone().equals(searchStr)
                    || contact.getUserDetail().getEmail().equals(searchStr)){
                callBack.gotoProfile(contact);
                AppUtil.hideProgressBar();
                return;
            }
        }

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
                Toast.makeText(context, context.getResources().getString(R.string.access_fail),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResult<List<User>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    callBack.gotoProfile(null);
                }else {
                    if(result.getData().isEmpty()){
                        callBack.gotoProfile(null);
                    }else {
                        User user = result.getData().get(0);
                        callBack.gotoProfile(new Contact(user));
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void goToProfile(Contact user){
        getView().goToProfileFragment(user);
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
