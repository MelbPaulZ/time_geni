package org.unimelb.itime.ui.fragment.contact;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.BaseUiFragment;
import java.lang.Object;

/**
 * Created by Paul on 1/1/17.
 */

public abstract class BaseContactFragment<V extends MvpView, P extends MvpPresenter<V>> extends BaseUiFragment<Object, V, P> {
    @Override
    public void setData(Object o) {

    }
}
