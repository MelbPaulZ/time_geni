package org.unimelb.itime.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.activity.SettingActivity;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.MainSettingMvpView;
import org.unimelb.itime.ui.viewmodel.MainSettingViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import me.fesky.library.widget.ios.ActionSheetDialog;

/**
 * Created by Paul on 25/12/2016.
 */

public class MainSettingFragment extends BaseUiFragment<Object,MainSettingMvpView,MvpBasePresenter<MainSettingMvpView>> implements MainSettingMvpView {
    private FragmentSettingBinding binding;
    private MainSettingViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Override
    public MvpBasePresenter createPresenter() {
        return new MvpBasePresenter<>();
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
        contentViewModel = new MainSettingViewModel(getPresenter());
        User user = UserUtil.getInstance(getContext()).getUser();
        contentViewModel.setUser(user);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitleStr(getString(R.string.setting));
        toolbarViewModel.setLeftClickable(false);
        toolbarViewModel.setRightClickable(false);

        binding.setToolbarVM(toolbarViewModel);
        binding.setContentVM(contentViewModel);
    }

    @Override
    public void toProfilePage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_MY_PROFILE);
        startActivity(intent);
    }

    @Override
    public void toQRcodePage() {
        Intent intent = new Intent(getActivity(), CaptureActivityContact.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_QR_CODE);
        startActivity(intent);
    }

    @Override
    public void toBlockedUserPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_BLOCK_USER);
        startActivity(intent);
    }

    @Override
    public void toNotificationPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_NOTICIFATION);
        startActivity(intent);
    }

    @Override
    public void toCalendarPreferencePage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_CALENDAR_PREFERENCE);
        startActivity(intent);
    }

    @Override
    public void toHelpFdPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_HELP_AND_FEEDBACK);
        startActivity(intent);
    }

    @Override
    public void toAboutPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        intent.putExtra(SettingActivity.TASK, SettingActivity.TASK_TO_ABOUT);
        startActivity(intent);
    }

    @Override
    public void onLogOut() {
        popupDialog();
    }

    @Subscribe
    public void logOut(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.LOGOUT){
            UserUtil.getInstance(getContext()).clearAccount();
            Intent i = new Intent(getContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            getActivity().finish();
        }
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void popupDialog(){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Log Out", null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                AppUtil.stopRemoteService(getContext());
                            }
                        });
        actionSheetDialog.show();
    }

    @Override
    public void setData(Object o) {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }
}
