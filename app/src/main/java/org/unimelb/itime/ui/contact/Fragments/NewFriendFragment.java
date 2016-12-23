package org.unimelb.itime.ui.contact.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.NewFriendFragmentBinding;
import org.unimelb.itime.ui.contact.MvpView.NewFriendMvpView;
import org.unimelb.itime.ui.contact.Presenter.NewFriendFragmentPresenter;
import org.unimelb.itime.ui.contact.ViewModel.NewFriendViewModel;

/**
 * Created by 37925 on 2016/12/9.
 */

public class NewFriendFragment extends MvpFragment<NewFriendMvpView, NewFriendFragmentPresenter> implements NewFriendMvpView {
    private NewFriendFragmentBinding binding;
    private android.support.v4.app.FragmentManager fm;
    private NewFriendViewModel viewModel;
    private AddFriendsFragment addFriendsFragment;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.new_friend_fragment, container, false);
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

    @Override
    public NewFriendFragmentPresenter createPresenter() {
        return new NewFriendFragmentPresenter(getContext());
    }
}
