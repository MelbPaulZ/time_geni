package org.unimelb.itime.ui.fragment.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingResetPassowrdBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingProfileResetPasswordFragment extends BaseUiAuthFragment<TaskBasedMvpView<User>, UserPresenter<TaskBasedMvpView<User>>> implements TaskBasedMvpView<User>, ItimeCommonMvpView {

    private FragmentSettingResetPassowrdBinding binding;
    private UserProfileViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private UserPresenter<TaskBasedMvpView<User>> presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_reset_passowrd, container, false);
        return binding.getRoot();
    }

    @Override
    public UserPresenter<TaskBasedMvpView<User>> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = getPresenter();

        contentViewModel = new UserProfileViewModel(presenter);
        User user = UserUtil.getInstance(getContext()).getUser();
        contentViewModel.setUser(user);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.setting_reset_password));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onBack() {
        getBaseActivity().backFragment(new SettingMyProfileFragment());
    }

    @Override
    public void onNext() {
        contentViewModel.onResetPSWDoneClick().onClick(null);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, User data) {
        switch (taskId){
            case UserPresenter.TASK_USER_UPDATE:{
                showUpdateSuccessMsg();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onTaskError(int taskId) {

        switch (taskId){
            case UserPresenter.TASK_USER_PSW_NOT_MATCH:{
                showNotMatchMsg();
                break;
            }
            default:
                break;
        }

    }

    private void showNotMatchMsg(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        // set title
        alertDialogBuilder.setTitle("Password NOT matched");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void showUpdateSuccessMsg(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            getContext());

        // set title
        alertDialogBuilder.setTitle("Success");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getBaseActivity().backFragment(new SettingMyProfileFragment());
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
