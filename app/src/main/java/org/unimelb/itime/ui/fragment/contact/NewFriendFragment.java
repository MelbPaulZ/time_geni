package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.databinding.FragmentNewFriendBinding;
import org.unimelb.itime.ui.mvpview.contact.NewFriendMvpView;
import org.unimelb.itime.ui.presenter.contact.NewFriendFragmentPresenter;
import org.unimelb.itime.ui.viewmodel.contact.NewFriendViewModel;

/**
 * Created by 37925 on 2016/12/9.
 */

public class NewFriendFragment extends MvpFragment<NewFriendMvpView, NewFriendFragmentPresenter> implements NewFriendMvpView {
    private FragmentNewFriendBinding binding;
    private android.support.v4.app.FragmentManager fm;
    private NewFriendViewModel viewModel;
    private AddFriendsFragment addFriendsFragment;
    private ProfileFragment profileFragment;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_new_friend, container, false);
//        initTitleBar();
//        initListView();
        presenter = createPresenter();
        viewModel = new NewFriendViewModel(presenter);
        binding.setViewModel(viewModel);
        fm = getFragmentManager();
        //initSearchBar();
        return binding.getRoot();
    }

    public void goToAddFriendsFragment() {
        if (addFriendsFragment == null) {
            addFriendsFragment = new AddFriendsFragment();
        }
        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, addFriendsFragment)
                .addToBackStack(null)
                .commit();
    }

    public void goToProfileFragment(Contact contact, FriendRequest request){
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUser(contact);
        profileFragment.setRequest(request);
        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public NewFriendFragmentPresenter createPresenter() {
        return new NewFriendFragmentPresenter(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadData();
    }
}
