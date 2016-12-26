package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import org.unimelb.itime.ui.mvpview.SettingMyProfileMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingProfileViewModel extends BaseObservable {

    private SettingCommonPresenter presenter;
    private SettingMyProfileMvpView mvpView;

    public SettingProfileViewModel(SettingCommonPresenter presenter) {
        this.presenter = presenter;
        mvpView = (SettingMyProfileMvpView) presenter.getView();

    }

    public View.OnClickListener onChangeAvatar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    mvpView.changeAdatar();
                }
            }
        };
    }
}
