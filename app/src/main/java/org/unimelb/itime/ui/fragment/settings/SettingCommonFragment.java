package org.unimelb.itime.ui.fragment.settings;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingCommonFragment extends MvpFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>> implements SettingCommonMvpView{



    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }
}
