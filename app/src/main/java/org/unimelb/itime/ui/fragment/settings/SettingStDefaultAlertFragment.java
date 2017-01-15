package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.databinding.FragmentSettingDefaultAlertBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.ui.viewmodel.SettingViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Arrays;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingStDefaultAlertFragment extends BaseUiAuthFragment<TaskBasedMvpView<Setting>, SettingPresenter<TaskBasedMvpView<Setting>>> implements TaskBasedMvpView, ItimeCommonMvpView {

    private FragmentSettingDefaultAlertBinding binding;
    private SettingViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private SettingPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_default_alert, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentViewModel = new SettingViewModel(getPresenter());
        contentViewModel.setAlertItemView(ItemView.of(BR.alertWrapper,R.layout.listview_setting_alert));
        contentViewModel.setSetting(UserUtil.getInstance(getContext()).getSetting());

        int[] alertKeys = {-1,0,5,15,30,60,120,2*24*60,7*24*60};
        contentViewModel.setAlertSet(alertWrapperMaker(alertKeys));

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.setting_default_alert));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter<>(getContext());
    }

    @Override
    public ToolbarViewModel getToolbarViewModel() {
        return toolbarViewModel;
    }

    @Override
    public void onBack() {
        getBaseActivity().backFragment(new SettingCalendarPreferenceFragment());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId) {
        Toast.makeText(getContext(),"Internet Error",Toast.LENGTH_LONG).show();
    }

    private ArrayList<SettingViewModel.AlertWrapper> alertWrapperMaker(int[] keys){
        ArrayList<SettingViewModel.AlertWrapper> wrappers = new ArrayList<>();
        for (int key:keys
             ) {
            wrappers.add(new SettingViewModel.AlertWrapper(false, AppUtil.getDefaultAlertStr(key), key));
        }

        return wrappers;
    }
}
