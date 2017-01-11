package org.unimelb.itime.ui.fragment.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;

import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.SettingWrapper;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 1/1/17.
 */

public abstract class SettingBaseFragment< V extends SettingCommonMvpView, P extends SettingCommonPresenter<V>> extends BaseUiFragment<SettingWrapper, V, P> {

    private SettingWrapper setting;
    protected MainSettingsViewModel viewModel;
    protected ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

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
        toolbarViewModel = getToolBarViewModel();
        setLeftTitleStringToVM();
        setTitleStringToVM();
        setRightTitleStringToVM();
    }

    public SettingWrapper getSetting() {
        return setting;
    }

    @Override
    public void setData(SettingWrapper setting) {
        ((SettingBaseFragment)getTo()).setSetting(setting);
    }

    public void setSetting(SettingWrapper setting){
        this.setting = setting;
        viewModel.setSetting(setting);
    }

    public void reloadSetting(){
        setSetting(SettingManager.getInstance(getContext()).getSetting());
    }

    public abstract void setLeftTitleStringToVM();
    public abstract void setTitleStringToVM();
    public abstract void setRightTitleStringToVM();
    public abstract ToolbarViewModel<? extends ItimeCommonMvpView> getToolBarViewModel();
}
