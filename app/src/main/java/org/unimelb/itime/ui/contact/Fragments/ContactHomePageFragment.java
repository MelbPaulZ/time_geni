package org.unimelb.itime.ui.contact.Fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ContactHomePageBinding;
import org.unimelb.itime.ui.activity.AddFriendActivity;
import org.unimelb.itime.ui.activity.FriendRequestActivity;
import org.unimelb.itime.ui.activity.ProfileActivity;
import org.unimelb.itime.ui.contact.Activities.MyQRCodeActivity;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.MvpView.ContactHomePageMvpView;
import org.unimelb.itime.ui.contact.Presenter.ContactHomePagePresenter;
import org.unimelb.itime.ui.contact.ViewModel.ContactHomePageViewModel;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;

/**
 * Created by 37925 on 2016/12/8.
 */

public class ContactHomePageFragment extends MvpFragment<ContactHomePageMvpView, ContactHomePagePresenter> implements ContactHomePageMvpView {
    private ContactHomePageBinding binding;
    private android.support.v4.app.FragmentManager fm;
    private NewFriendFragment newFriendFragment;
    private AddFriendsFragment addFriendsFragment;
    private ProfileFragment profileFragment;
    private ContactHomePageViewModel viewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.contact_home_page, container, false);
        initMainView();
        fm = getFragmentManager();
        viewModel.initSideBarListView(binding.sortListView);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onStart(){
        super.onStart();
    }

    public void initMainView() {
        presenter = createPresenter();
        viewModel = new ContactHomePageViewModel(presenter);
        viewModel.setPresenter(presenter);
        binding.setMainViewModel(viewModel);
    }

    public void goToNewFriendFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FriendRequestActivity.class);
        startActivity(intent);
    }

    public void goToProfileFragment(ITimeUser user) {
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
}
