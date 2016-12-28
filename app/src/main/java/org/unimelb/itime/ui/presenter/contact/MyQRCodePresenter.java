package org.unimelb.itime.ui.presenter.contact;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.contact.MyQRCodeMvpView;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodePresenter extends MvpBasePresenter<MyQRCodeMvpView> {
    public void onBackPress() {
        getView().getActivity().onBackPressed();
    }

    public void saveQRCode(){
        getView().saveQRCode();
    }
}
