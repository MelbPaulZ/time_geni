package org.unimelb.itime.ui.contact.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.InviteFridendsFragmentBinding;
import org.unimelb.itime.databinding.InviteeHeaderBinding;
import org.unimelb.itime.ui.contact.MvpView.InviteFriendMvpView;
import org.unimelb.itime.ui.contact.Presenter.InviteFriendPresenter;
import org.unimelb.itime.ui.contact.ViewModel.InviteFriendViewModel;

/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteFriendsFragment  extends MvpFragment<InviteFriendMvpView, InviteFriendPresenter> implements InviteFriendMvpView {
    private InviteFridendsFragmentBinding binding;
    private InviteeHeaderBinding headerBinding;
    private FragmentManager fm;
    private InviteMobileContactsFragment mobileFragment;
    private InviteOtherContactsFragment gmailFragment;
    private InviteOtherContactsFragment facebookFragment;
    private InviteFriendViewModel viewModel;
    private boolean isInit = true;

    @Override
    public InviteFriendPresenter createPresenter() {
        return new InviteFriendPresenter();
    }

    public void onStart(){
        super.onStart();
        viewModel.setHeaderView((LinearLayout) headerBinding.getRoot());
        viewModel.setSideBarListView(binding.friendsListView);
        viewModel.setInviteeGroupView(headerBinding.inviteeGroupView);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.invite_fridends_fragment, container, false);
        headerBinding = DataBindingUtil.inflate(inflater,
                R.layout.invitee_header, null, false);
        presenter = createPresenter();
        viewModel = new InviteFriendViewModel(presenter);
        binding.setViewModel(viewModel);
        headerBinding.setViewModel(viewModel);
        fm = getFragmentManager();
        return binding.getRoot();
    }

    public void gotoInviteMobileContacts(){

        if(mobileFragment==null){
            mobileFragment = new InviteMobileContactsFragment();
        }
        fm.beginTransaction()
                .hide(this)
                .add(R.id.contentFrameLayout, mobileFragment)
                .addToBackStack(null)
                .commit();
    }

    public void gotoInviteGmailContacts(){

        if(gmailFragment==null){
            gmailFragment = new InviteOtherContactsFragment();
            gmailFragment.setSource(InviteOtherContactsFragment.GMAIL);
        }
        fm.beginTransaction()
                .hide(this)
                .add(R.id.contentFrameLayout, gmailFragment)
                .addToBackStack(null)
                .commit();
    }

    public void gotoInviteFacebookContacts(){

        if(facebookFragment==null){
            facebookFragment = new InviteOtherContactsFragment();
            facebookFragment.setSource(InviteOtherContactsFragment.FACEBOOK);
        }
        fm.beginTransaction()
                .hide(this)
                .add(R.id.contentFrameLayout, facebookFragment)
                .addToBackStack(null)
                .commit();
    }

}
