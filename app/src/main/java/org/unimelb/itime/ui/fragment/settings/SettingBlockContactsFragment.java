package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.databinding.FragmentBlocklistBinding;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;
import org.unimelb.itime.ui.mvpview.contact.BlockContactsMvpView;
import org.unimelb.itime.ui.presenter.contact.BlockContactsPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.BlockContactsViewModel;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class SettingBlockContactsFragment extends BaseUiAuthFragment<BlockContactsMvpView, BlockContactsPresenter> implements BlockContactsMvpView {
    private FragmentBlocklistBinding binding;
    private android.support.v4.app.FragmentManager fm;
    private ProfileFragment profileFragment;
    private BlockContactsViewModel viewModel;
    private ToolbarViewModel toolbarViewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_blocklist, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new BlockContactsViewModel(presenter);
        viewModel.initSideBarListView(binding.sortListView);
        binding.setMainViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitleStr(getString(R.string.block_contacts));
        toolbarViewModel.setLeftDrawable(getResources().getDrawable(R.drawable.ic_back_arrow));
        binding.setToolbarVM(toolbarViewModel);
    }

    public void onStart(){
        super.onStart();
        viewModel.loadData();
    }

    public void onResume(){
        super.onResume();
    }

    public void goToProfileFragment(Contact user) {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUser(user);
        getBaseActivity().openFragment(profileFragment, null, true);
//        fm.beginTransaction()
//                .hide(this)
//                .add(R.id.contentFrameLayout, profileFragment)
//                .addToBackStack(null)
//                .commit();
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), ProfileActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ProfileActivity.USER,user);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    @Override
    public BlockContactsPresenter createPresenter() {
        return new BlockContactsPresenter(getContext());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<ITimeUser> data) {
        viewModel.setFriendList(data);
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
