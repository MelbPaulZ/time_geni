package org.unimelb.itime.ui.fragment.settings;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.viewmodel.CommonViewModel;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingNotificationFragment extends BaseUiFragment<SettingCommonMvpView, CommonPresenter<SettingCommonMvpView>> {


    @Override
    public CommonPresenter<SettingCommonMvpView> createPresenter() {
        return new CommonPresenter<>(getContext());
    }
}
