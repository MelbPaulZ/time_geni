package org.unimelb.itime.ui.fragment.contact;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentProfileBinding;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.presenter.contact.ProfileFragmentPresenter;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragment extends BaseUiFragment<ProfileMvpView, ProfileFragmentPresenter> implements ProfileMvpView{
    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;
    private View mainView;
    private boolean showAdd = false;
    private boolean showSend = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
    private boolean showRightButton = true;
    private ITimeUser user;

    public View getContentView(){
        return getView();
    }

    @Override
    public ProfileFragmentPresenter createPresenter() {
        return new ProfileFragmentPresenter(getActivity());
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        presenter = createPresenter();
        viewModel = new ProfileFragmentViewModel(presenter);
        viewModel.setShowAdd(showAdd);
        viewModel.setShowSent(showSend);
        viewModel.setShowEmail(showEmail);
        viewModel.setShowPhone(showPhone);
        viewModel.setShowTitleRight(showRightButton);
        viewModel.setFriend(user);

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);
        binding.setViewModel(viewModel);
        mainView = binding.getRoot();
        return mainView;
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
    }

    public void setShowSend(boolean showSend) {
        this.showSend = showSend;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
    }

    public void setShowRightButton(boolean showRightButton) {
        this.showRightButton = showRightButton;
    }

    public void setUser(ITimeUser user) {
        this.user = user;
    }

    public void goToInviteFragment(ITimeUser user){

    }


    public Context getContext(){
        return getActivity();
    }



}
