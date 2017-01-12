package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingGenderBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel.GenderWrapper;
import org.unimelb.itime.util.UserUtil;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingProfileGenderFragment  extends BaseUiAuthFragment<TaskBasedMvpView<User>, UserPresenter<TaskBasedMvpView<User>>> implements TaskBasedMvpView<User>, ItimeCommonMvpView {

    private FragmentSettingGenderBinding binding;

    private UserProfileViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private UserPresenter<TaskBasedMvpView<User>> presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_gender, container, false);
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

        contentViewModel.setGenderWrapperList(prepareData());
        contentViewModel.setGenderItemView(ItemView.of(BR.wrapper, R.layout.listview_setting_gender));

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.setting_my_profile));
        toolbarViewModel.setTitleStr(getString(R.string.setting_gender));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private ObservableList<GenderWrapper> prepareData(){
        ObservableList<GenderWrapper> list = new ObservableArrayList<>();
        list.add(new GenderWrapper("Male", false));
        list.add(new GenderWrapper("Female", false));
        list.add(new GenderWrapper("Undefind", false));
        return list;
    }


    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, User data) {

    }

    @Override
    public void onTaskError(int taskId) {

    }
}
