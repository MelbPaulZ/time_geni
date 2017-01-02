package org.unimelb.itime.ui.fragment.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

/**
 * Created by Paul on 1/1/17.
 */

public abstract class SettingBaseFragment< V extends SettingCommonMvpView, P extends SettingCommonPresenter<V>> extends BaseUiFragment<Setting, V, P>  {

    private Setting setting;
    protected MainSettingsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetting();
    }

    private void initSetting(){
        if (setting == null){
            setting = SettingManager.getInstance(getContext()).copySetting();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new MainSettingsViewModel(getPresenter());
        viewModel.setSetting(setting);
    }

    public Setting getSetting() {
        return setting;
    }

//    @Override
//    public void onEnter() {
//        super.onEnter();
//        viewModel.setSetting(setting);
//    }

    @Override
    public void setData(Setting setting) {
        ((SettingBaseFragment)getTo()).setSetting(setting);
    }

    public void setSetting(Setting setting){
        this.setting = setting;
        viewModel.setSetting(setting);
    }
}
