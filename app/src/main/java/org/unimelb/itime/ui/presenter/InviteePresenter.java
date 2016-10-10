package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.util.HttpUtil;

/**
 * Created by Paul on 10/10/16.
 */
public class InviteePresenter extends MvpBasePresenter<MvpView> {
    private Context context;
    public InviteePresenter(Context context){
        this.context = context;
    }

}
