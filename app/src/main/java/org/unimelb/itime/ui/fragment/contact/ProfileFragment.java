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
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentProfileBinding;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.presenter.contact.ProfileFragmentPresenter;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.util.rulefactory.InviteeUtil;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragment extends BaseContactFragment<ProfileMvpView, ProfileFragmentPresenter> implements ProfileMvpView{
    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;
    private View mainView;
    private boolean showAdd = false;
    private boolean showSend = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
    private boolean showRightButton = true;
    private Contact user;

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
        viewModel.setFriend(user);

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);
        binding.setViewModel(viewModel);
        mainView = binding.getRoot();
        return mainView;
    }

    private void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
    }

    private void setShowSend(boolean showSend) {
        this.showSend = showSend;
    }

    private void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    private void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
    }

    private void setShowRightButton(boolean showRightButton) {
        this.showRightButton = showRightButton;
    }

    public void setUser(Contact user) {
        this.user = user;
        if(viewModel != null){
            viewModel.setFriend(user);
        }
    }

    public void goToInviteFragment(Contact contact){
        
    }

    public void init(Contact contact){

    }

    public Context getContext(){
        return getActivity();
    }



}
