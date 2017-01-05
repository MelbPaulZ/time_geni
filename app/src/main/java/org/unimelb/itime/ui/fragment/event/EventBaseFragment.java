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
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

import static com.wx.wheelview.widget.WheelView.Skin.Common;

/**
 * provide some common methods and initialise parameters
 */
public abstract class EventBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends BaseUiFragment<Event, V, P> implements EventCommonMvpView{

    @Override
    public void setData(Event event) {

    }


    @Override
    public void onStart() {
        super.onStart();
        setLeftTitleStringToVM();
        setTitleStringToVM();
        setRightTitleStringToVM();
    }

    public abstract void setLeftTitleStringToVM();
    public abstract void setTitleStringToVM();
    public abstract void setRightTitleStringToVM();
}
