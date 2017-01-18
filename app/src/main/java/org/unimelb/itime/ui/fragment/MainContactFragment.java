package org.unimelb.itime.ui.fragment;

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
import org.unimelb.itime.databinding.ContactHomePageBinding;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.ui.activity.AddFriendActivity;
import org.unimelb.itime.ui.activity.FriendRequestActivity;
import org.unimelb.itime.ui.activity.ProfileActivity;
import org.unimelb.itime.ui.fragment.contact.AddFriendsFragment;
import org.unimelb.itime.ui.fragment.contact.BaseContactFragment;
import org.unimelb.itime.ui.fragment.contact.NewFriendFragment;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;
import org.unimelb.itime.ui.mvpview.contact.ContactHomePageMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactHomePagePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ContactHomePageViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;

import java.util.List;

/**
 * Created by 37925 on 2016/12/8.
 */

public class MainContactFragment extends BaseUiAuthFragment<ContactHomePageMvpView, ContactHomePagePresenter> implements ContactHomePageMvpView {
    private ContactHomePageBinding binding;
    private ContactHomePageViewModel viewModel;
    private ToolbarViewModel toolbarViewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.contact_home_page, container, false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new ContactHomePageViewModel(presenter);
        viewModel.initSideBarListView(binding.sortListView);
        binding.setMainViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitleStr(getString(R.string.contacts));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightDrawable(getResources().getDrawable(R.drawable.icon_plus_bold));
        binding.setToolbarVM(toolbarViewModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeContact(MessageRemoveContact msg){
        viewModel.removeContact(msg.contact);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addContact(MessageAddContact msg){
        viewModel.addContact(msg.contact);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRequestCount(MessageNewFriendRequest msg){
        viewModel.setRequestCount(msg.count);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void editContact(MessageEditContact msg){
        viewModel.loadData();
    }

    public void onStart(){
        super.onStart();
        viewModel.loadData();
    }

    public void onResume(){
        super.onResume();
    }



    public void goToNewFriendFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FriendRequestActivity.class);
        startActivity(intent);

//        Intent intent = new Intent();
//        intent.setClass(getActivity(), BlockContactsActivity.class);
//        startActivity(intent);

    }

    public void goToProfileFragment(Contact user) {
//        if (profileFragment == null) {
//            profileFragment = new ProfileFragment();
//        }
//        profileFragment.setUser(user);
//        profileFragment.setShowRightButton(true);
//        fm.beginTransaction()
//                .hide(this)
//                .add(R.id.contentFrameLayout, profileFragment)
//                .addToBackStack(null)
//                .commit();
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProfileActivity.USER,user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public ContactHomePageBinding getBinding() {
        return binding;
    }

    public void goToAddFriendsFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public ContactHomePagePresenter createPresenter() {
        return new ContactHomePagePresenter(getContext());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {
        goToAddFriendsFragment();
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<ITimeUser> data) {
        switch (taskId){
            case ContactHomePagePresenter.TASK_CONTACTS:
                viewModel.setFriendList(data);
        }
    }

    @Override
    public void onTaskError(int taskId) {

    }
}
