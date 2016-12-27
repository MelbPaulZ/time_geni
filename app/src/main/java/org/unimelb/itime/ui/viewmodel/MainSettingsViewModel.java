package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.MainSettingsPresenter;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.util.UserUtil;

import static java.security.AccessController.getContext;
import static org.unimelb.itime.vendor.contact.widgets.SideBar.b;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingsViewModel extends BaseObservable{
    private static final String TAG = "MainSettingsViewModel";
    private SettingCommonPresenter presenter;
    private SettingCommonMvpView mvpView;

    public final static int TASK_LOGOUT = 1;
    public final static int TASK_VIEW_AVATAR = 2;
    public final static int TASK_VIEW_MY_PROFILE = 3;
    public final static int TASK_TO_SETTING = 4;
    public final static int TASK_TO_MY_PROFILE_NAME = 5;


    public MainSettingsViewModel(SettingCommonPresenter presenter){
        this.presenter = presenter;
        this.mvpView = (SettingCommonMvpView) presenter.getView();

    }

    private Context getContext(){
        return presenter.getContext();
    }

    public String getUsername(){
            return UserUtil.getInstance(getContext()).getUser().getEmail();
    }


    public View.OnClickListener onViewChange(final int task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (task){
                    case TASK_LOGOUT:
                        v.setClickable(false);
                        Toast.makeText(presenter.getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                        mvpView.onViewChange(task);
                        break;
                    case TASK_VIEW_MY_PROFILE:
                        mvpView.onViewChange(task);
                        break;
                    case TASK_TO_SETTING:
                        mvpView.onViewChange(task);
                        break;
                    case TASK_TO_MY_PROFILE_NAME:
                        mvpView.onViewChange(task);
                        break;
                    case TASK_VIEW_AVATAR:
                        mvpView.onViewChange(task);
                        break;
                }
            }
        };
    }


}
