package org.unimelb.itime.ui.fragment.settings;


import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;

/**
 * Created by Paul on 1/1/17.
 */

public abstract class SettingBaseFragment< V extends SettingCommonMvpView, P extends SettingCommonPresenter<V>> extends BaseUiFragment<Setting, V, P>  {

    private Setting setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Setting getSetting() {
        return setting;
    }

    @Override
    public void setData(Setting setting) {
        this.setting = setting;
    }
}
