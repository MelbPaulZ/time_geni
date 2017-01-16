package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.UserApi;
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
    private UserApi userApi;

    public BlockContactsPresenter(Context context){
        this.context = context;
        this.userApi = HttpUtil.createService(context, UserApi.class);
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

    private List<ITimeUser> generateITimeUserList(List<Block> list){
        List<ITimeUser> result = new ArrayList<>();
        for(Block block: list){
            Contact contact = new Contact(block.getUserDetail());
            contact.setBlockLevel(block.getBlockLevel());
            contact.setRelationship(1);
            ITimeUser user = new ITimeUser(contact);
            result.add(user);
        }
        return result;
    }

    private void getBlockListFromServer(final BlockContactsViewModel.ContactsCallBack callBack){
        final DBManager dbManager = DBManager.getInstance(context);
        Observable<HttpResult<List<Block>>> observable = userApi.listBlock();
        Observable<List<Block>> dbObservable = observable.map(new Func1<HttpResult<List<Block>>, List<Block>>() {
            @Override
            public List<Block> call(HttpResult<List<Block>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    return null;
                }else {
                    for(Block block:result.getData()) {
                        dbManager.insertBlock(block);
                    }
                    return DBManager.getInstance(context).getBlockContacts();
                }
            }
        });

        Subscriber<List<Block>> subscriber = new Subscriber<List<Block>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                callBack.failed();
            }

            @Override
            public void onNext(List<Block> list) {
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
