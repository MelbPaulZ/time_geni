package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.databinding.FragmentNewFriendBinding;
import org.unimelb.itime.ui.mvpview.contact.NewFriendMvpView;
import org.unimelb.itime.ui.presenter.contact.NewFriendFragmentPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.NewFriendViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;

import java.util.List;

/**
 * Created by 37925 on 2016/12/9.
 */

public class NewFriendFragment extends BaseUiAuthFragment<NewFriendMvpView, NewFriendFragmentPresenter> implements NewFriendMvpView {
    private FragmentNewFriendBinding binding;
    private android.support.v4.app.FragmentManager fm;
    private NewFriendViewModel viewModel;
    private AddFriendsFragment addFriendsFragment;
    private ProfileFragment profileFragment;
    private ToolbarViewModel toolbarViewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_new_friend, container, false);
//        initTitleBar();
//        initListView();
        //initSearchBar();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new NewFriendViewModel(presenter);
        binding.setViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.new_friends));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightDrawable(getResources().getDrawable(R.drawable.icon_plus_bold));
        binding.setToolbarVM(toolbarViewModel);
    }

    public void goToAddFriendsFragment() {
        if (addFriendsFragment == null) {
            addFriendsFragment = new AddFriendsFragment();
        }
        getBaseActivity().openFragment(addFriendsFragment, null, true);
//        fm.beginTransaction()
//                .hide(this)
//                .replace(R.id.contentFrameLayout, addFriendsFragment)
//                .addToBackStack(null)
//                .commit();
    }

    public void goToProfileFragment(Contact contact, FriendRequest request){
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUser(contact);
        profileFragment.setRequest(request);
        getBaseActivity().openFragment(profileFragment, null, true);
//        fm.beginTransaction()
//                .hide(this)
//                .replace(R.id.contentFrameLayout, profileFragment)
//                .addToBackStack(null)
//                .commit();
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

    @Override
    public void onBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        goToAddFriendsFragment();
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<RequestFriend> data) {
        viewModel.setRequestList(data);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
    }
}
