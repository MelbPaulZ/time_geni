package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.unimelb.itime.ui.mvpview.MainSettingsMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.MainSettingsPresenter;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.util.UserUtil;

import static java.security.AccessController.getContext;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingsViewModel extends BaseObservable{
    private static final String TAG = "MainSettingsViewModel";
    private SettingCommonPresenter presenter;
    private MainSettingsMvpView mvpView;


    public MainSettingsViewModel(SettingCommonPresenter presenter){
        this.presenter = presenter;
        this.mvpView = (MainSettingsMvpView) presenter.getView();

    }

    private Context getContext(){
        return presenter.getContext();
    }

    public String getUsername(){
            return UserUtil.getInstance(getContext()).getUser().getEmail();
    }

    public View.OnClickListener onLogOutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.setClickable(false);
                Toast.makeText(presenter.getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                mvpView.logout();
            }
        };
    }


}
