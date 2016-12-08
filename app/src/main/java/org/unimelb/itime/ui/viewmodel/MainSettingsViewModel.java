package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.unimelb.itime.ui.presenter.MainSettingsPresenter;
import org.unimelb.itime.util.UserUtil;

import static java.security.AccessController.getContext;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingsViewModel extends BaseObservable{
    private static final String TAG = "MainSettingsViewModel";
    MainSettingsPresenter presenter;

    public MainSettingsViewModel(MainSettingsPresenter presenter){
        this.presenter = presenter;

    }

    public String getUsername(){
        return UserUtil.getInstance().getUser().getEmail();
    }

    public View.OnClickListener onLogOutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                Toast.makeText(presenter.getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                presenter.logOut();
            }
        };
    }

}
