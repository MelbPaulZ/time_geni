package org.unimelb.itime.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * use for user authorization,
 * except for LoginFragment, all others should extend this class
 */
public abstract class BaseUiAuthFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {


    protected BaseActivity baseActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baseActivity = (BaseActivity)getActivity();
    }

    public BaseActivity getBaseActivity(){
        return baseActivity;
    }

}
