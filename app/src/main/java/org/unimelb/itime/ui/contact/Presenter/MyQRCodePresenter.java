package org.unimelb.itime.ui.contact.Presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.contact.MvpView.MyQRCodeMvpView;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodePresenter extends MvpBasePresenter<MyQRCodeMvpView> {
    public void onBackPress() {
        getView().getActivity().onBackPressed();
    }
}
