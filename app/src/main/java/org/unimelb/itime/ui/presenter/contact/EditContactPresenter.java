package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.contact.EditContactMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditContactPresenter extends MvpBasePresenter<EditContactMvpView> {
    private static final String TAG = "EditContact";
    private Context context;
    private ContactApi contactApi;

    public EditContactPresenter(Context context) {
        this.context = context;
        contactApi = HttpUtil.createService(context,ContactApi.class);
    }

    public void editAlias(Contact contact){
        if(getView()!=null){
            EventBus.getDefault().post(new MessageEditContact(contact));
            updateAlias(contact);
            getView().getActivity().onBackPressed();
        }
    }

    private void updateAlias(final Contact contact){
        Observable<HttpResult<Void>> observable = contactApi.updateAlias(contact.getContactUid(), contact.getAliasName());
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                Toast.makeText(context,"Update failed, please check your network", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpResult<Void> result) {
                Log.d(TAG, "onNext: " + result.getInfo());

                if (result.getStatus()!=1){

                }else {
                    DBManager.getInstance(context).updateContact(contact);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
