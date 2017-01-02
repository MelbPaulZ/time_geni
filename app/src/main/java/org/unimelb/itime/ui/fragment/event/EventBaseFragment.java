package org.unimelb.itime.ui.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.contact.BaseContactFragment;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

/**
 * provide some common methods and initialise parameters
 */
public abstract class EventBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends BaseUiFragment<Event, V, P> {

    @Override
    public void setData(Event event) {

    }
}
