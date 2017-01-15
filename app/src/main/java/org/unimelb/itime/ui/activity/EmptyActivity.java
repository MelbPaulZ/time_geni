package org.unimelb.itime.ui.activity;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.BaseActivity;

/**
 * Created by Paul on 13/1/17.
 */

public class EmptyActivity extends BaseActivity<MvpView, MvpBasePresenter<MvpView>> implements MvpView{
    @NonNull
    @Override
    public MvpBasePresenter<MvpView> createPresenter() {
        return new MvpBasePresenter<>();
    }
}
