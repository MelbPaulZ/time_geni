package org.unimelb.itime.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.ui.presenter.MainContactPresenter;

/**
 * required signIn, need to extend BaseUiAuthFragment
 */
public class MainContactsFragment<V,P> extends BaseUiFragment {


    public MainContactsFragment() {
    }

    @Override
    public MvpPresenter createPresenter() {
        return new MainContactPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_contacts, container, false);
    }

    @Override
    public void setData(Object o) {

    }
}
