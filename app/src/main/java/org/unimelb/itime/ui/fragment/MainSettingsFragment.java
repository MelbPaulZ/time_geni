package org.unimelb.itime.ui.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.C;
import org.unimelb.itime.databinding.FragmentMainSettingsBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.activity.SplashActivity;
import org.unimelb.itime.ui.mvpview.MainSettingsMvpView;
import org.unimelb.itime.ui.presenter.MainSettingsPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.AuthUtil;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainSettingsFragment extends MvpFragment<MainSettingsMvpView, MainSettingsPresenter> implements MainSettingsMvpView{
    private MainSettingsViewModel settingVM;
    private FragmentMainSettingsBinding binding;
    private String TAG =  "MainSettingsFragment";

    public MainSettingsFragment() {
    }

    @Override
    public MainSettingsPresenter createPresenter() {
        return new MainSettingsPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingVM = new MainSettingsViewModel(getPresenter());
        binding.setVM(settingVM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_settings, container, false);
        return binding.getRoot();
    }

    @Override
    public void logOut() {
        stopRemoteService();
    }

    @Subscribe
    public void logout(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.LOGOUT){
            clearAccount();
            Intent i = new Intent(getContext(), SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            getActivity().finish();
        }
    }

    private void stopRemoteService(){
        Intent serviceI = new Intent(getContext(), RemoteService.class);
        getActivity().stopService(serviceI);
    }

    private void clearAccount(){
        AuthUtil.clearJwtToken(getContext());
        SharedPreferences sp = AppUtil.getTokenSaver(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, "");
        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, "");
        editor.apply();

        Log.i(TAG, "clearAccount: " + "clear DB manager");
        DBManager.getInstance(getContext()).deleteAllMessages();
        DBManager.getInstance(getContext()).clearDB();
        Log.i(TAG, "clearAccount: " + "clear Event manager");
        EventManager.getInstance(getContext()).clearManager();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
