package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentInviteFridendsBinding;
import org.unimelb.itime.databinding.ListviewInviteeHeaderBinding;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.ui.mvpview.contact.InviteFriendMvpView;
import org.unimelb.itime.ui.presenter.contact.InviteFriendPresenter;
import org.unimelb.itime.ui.viewmodel.contact.InviteFriendViewModel;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;
import org.unimelb.itime.widget.InviteeGroupView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteFriendsFragment  extends BaseUiFragment<InviteFriendMvpView, InviteFriendPresenter> implements InviteFriendMvpView {
    private FragmentInviteFridendsBinding binding;
    private ListviewInviteeHeaderBinding headerBinding;
    private FragmentManager fm;
    private InviteMobileContactsFragment mobileFragment;
    private InviteOtherContactsFragment gmailFragment;
    private InviteOtherContactsFragment facebookFragment;
    private InviteFriendViewModel viewModel;
    private boolean isInit = true;
    private Event event;
    private InviteeGroupView inviteeGroupView;

    @Override
    public InviteFriendPresenter createPresenter() {
        return new InviteFriendPresenter(getActivity());
    }

    @Override
    public void onStart(){
        super.onStart();
        viewModel.setHeaderView((LinearLayout) headerBinding.getRoot());
        viewModel.setSideBarListView(binding.friendsListView);
        inviteeGroupView = headerBinding.inviteeGroupView;
        viewModel.setEvent(event);
        viewModel.loadData();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
//        presenter = createPresenter();
        viewModel = new InviteFriendViewModel(presenter);
        binding.setViewModel(viewModel);
        headerBinding.setViewModel(viewModel);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_invite_fridends, container, false);
        headerBinding = DataBindingUtil.inflate(inflater,
                R.layout.listview_invitee_header, null, false);
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

    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setEvent(Event event){
        this.event = event;
        if(viewModel!=null){
            viewModel.setEvent(event);
        }
    }

    //Write your code here
    @Override
    public void onDoneClicked(){
        System.out.println(viewModel.getInviteeList().size());
    }
}
