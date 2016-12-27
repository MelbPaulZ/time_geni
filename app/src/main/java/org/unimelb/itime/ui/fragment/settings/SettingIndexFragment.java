package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.base.C;
import org.unimelb.itime.databinding.FragmentSettingBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.AuthUtil;

import me.fesky.library.widget.ios.ActionSheetDialog;

import static org.unimelb.itime.R.id.main_fragment_container;
import static org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity.CHOOSE_FROM_LIBRARY;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingIndexFragment extends BaseUiFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>>
implements SettingCommonMvpView{

    private FragmentSettingBinding binding;
    private final String TAG = "SettingIndexFragment";

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainSettingsViewModel viewModel = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(viewModel);
        initSettingFragments();
    }


    private void initSettingFragments(){
        SettingMyProfileFragment myProfileFragment = new SettingMyProfileFragment();
        SettingMyProfileNameFragment myProfileNameFragment = new SettingMyProfileNameFragment();
        getFragmentManager().beginTransaction().add(R.id.main_fragment_container, myProfileFragment, myProfileFragment.getClassName()).hide(myProfileFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.main_fragment_container, myProfileNameFragment, myProfileNameFragment.getClassName()).hide(myProfileNameFragment).commit();
    }

    @Subscribe
    public void logout(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.LOGOUT){
            clearAccount();
            Intent i = new Intent(getContext(), LoginActivity.class);
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

    /**
     * popup dialog for logout
     */
    private void popupDialog(){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Log Out", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stopRemoteService();
                            }
                        });
                actionSheetDialog.show();
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


    @Override
    public void onViewChange(int task) {
        if (task == MainSettingsViewModel.TASK_LOGOUT){
            popupDialog();
        }else if (task == MainSettingsViewModel.TASK_VIEW_MY_PROFILE){
            SettingMyProfileFragment myProfileFragment = (SettingMyProfileFragment) getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName());
            openFragment(this, myProfileFragment);
        }
    }

}
