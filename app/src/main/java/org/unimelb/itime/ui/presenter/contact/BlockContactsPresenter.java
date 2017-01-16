package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.contact.BlockContactsMvpView;
import org.unimelb.itime.ui.mvpview.contact.ContactHomePageMvpView;
import org.unimelb.itime.ui.viewmodel.contact.BlockContactsViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ContactHomePageViewModel;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class BlockContactsPresenter extends MvpBasePresenter<BlockContactsMvpView> {
    private static final String TAG = "BlockContacts";
    private Context context;
    private ContactApi contactApi;

    public BlockContactsPresenter(Context context){
        this.context = context;
        this.contactApi = HttpUtil.createService(context, ContactApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void getBlockList(BlockContactsViewModel.ContactsCallBack callBack){
        DBManager dbManager = DBManager.getInstance(context);
        List<ITimeUser> list = generateITimeUserList(dbManager.getBlockContacts());
        callBack.success(list);
        getBlockListFromServer(callBack);
    }

    private List<ITimeUser> generateITimeUserList(List<Contact> list){
        List<ITimeUser> result = new ArrayList<>();
        for(Contact contact: list){
            ITimeUser user = new ITimeUser(contact);
            result.add(user);
        }
        return result;
    }

    private void getBlockListFromServer(final BlockContactsViewModel.ContactsCallBack callBack){
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
                    return DBManager.getInstance(context).getBlockContacts();
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

    public int getMatchColor() {
        return context.getResources().getColor(R.color.matchColor);
    }
}
