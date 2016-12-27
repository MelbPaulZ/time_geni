package org.unimelb.itime.ui.fragment.settings;

import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;

/**
 * Created by Paul on 27/12/2016.
 */

/**/
public class SettingImportCalendarUnimelbFragment extends BaseUiFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>> {

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }
}
