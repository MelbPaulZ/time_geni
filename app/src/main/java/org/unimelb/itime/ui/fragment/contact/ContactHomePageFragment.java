package org.unimelb.itime.ui.fragment.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ContactHomePageBinding;
import org.unimelb.itime.ui.activity.AddFriendActivityContact;
import org.unimelb.itime.ui.activity.FriendRequestActivityContact;
import org.unimelb.itime.ui.activity.ProfileActivityContact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.ContactHomePageMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactHomePagePresenter;
import org.unimelb.itime.ui.viewmodel.contact.ContactHomePageViewModel;

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
        viewModel.loadData();
    }

    public void onResume(){
        super.onResume();
        viewModel.loadData();
    }

    public void initMainView() {
        presenter = createPresenter();
        viewModel = new ContactHomePageViewModel(presenter);
        viewModel.setPresenter(presenter);
        binding.setMainViewModel(viewModel);
    }

    public void goToNewFriendFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FriendRequestActivityContact.class);
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
        intent.setClass(getActivity(), ProfileActivityContact.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProfileActivityContact.USER,user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public ContactHomePageBinding getBinding() {
        return binding;
    }

    public void goToAddFriendsFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AddFriendActivityContact.class);
        startActivity(intent);
    }

    @Override
    public ContactHomePagePresenter createPresenter() {
        return new ContactHomePagePresenter(getContext());
    }
}
