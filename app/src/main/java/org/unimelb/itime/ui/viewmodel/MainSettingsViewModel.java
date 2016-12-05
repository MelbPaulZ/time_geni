package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import org.unimelb.itime.ui.presenter.MainSettingsPresenter;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingsViewModel extends BaseObservable{
    private static final String TAG = "MainSettingsViewModel";
    MainSettingsPresenter presenter;

    public MainSettingsViewModel(MainSettingsPresenter presenter){
        this.presenter = presenter;

    }

    public View.OnClickListener onLogOutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  presenter.logOut();
            }
        };
    }

}
