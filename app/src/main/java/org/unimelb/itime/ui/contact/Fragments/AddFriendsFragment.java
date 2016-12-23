package org.unimelb.itime.ui.contact.Fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.AddFriendFragmentBinding;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.MvpView.AddFriendsMvpView;
import org.unimelb.itime.ui.contact.Presenter.AddFriendsPresenter;
import org.unimelb.itime.ui.contact.QRCode.CaptureActivity;
import org.unimelb.itime.ui.contact.ViewModel.AddFriendsViewModel;

/**
 * Created by 37925 on 2016/12/10.
 */

public class AddFriendsFragment extends MvpFragment<AddFriendsMvpView, AddFriendsPresenter> implements AddFriendsMvpView{

    android.support.v4.app.FragmentManager fm;
    AddFriendFragmentBinding binding;
    AddFriendsViewModel mainViewModel;
    AddOtherContactsFragment gmailFragment;
    AddOtherContactsFragment facebookFragment;
    AddMobileContactsFragment mobileFragment;
    ProfileFragment profileFragment;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.add_friend_fragment, container, false);
        presenter = createPresenter();
        mainViewModel = new AddFriendsViewModel(presenter);
        binding.setMainViewModel(mainViewModel);
        fm = getFragmentManager();
        return binding.getRoot();
    }

    public void goToProfileFragment(ITimeUser user, String show){
        if(profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUser(user);
        if(show.equals("email")){
            profileFragment.setShowEmail(true);
            profileFragment.setShowPhone(false);
        }else{
            profileFragment.setShowPhone(true);
            profileFragment.setShowEmail(false);
        }
        profileFragment.setShowAdd(true);
        profileFragment.setShowSend(false);
        profileFragment.setShowRightButton(false);

        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    public void goToAddFacebookContacts(){
        if(facebookFragment==null){
            facebookFragment = new AddOtherContactsFragment();
            facebookFragment.setSource(AddOtherContactsFragment.FACEBOOK);
        }

        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, facebookFragment)
                .addToBackStack(null)
                .commit();
    }

    public void goToAddGmailContacts(){
        if(gmailFragment==null){
            gmailFragment = new AddOtherContactsFragment();
            gmailFragment.setSource(AddOtherContactsFragment.GMAIL);
        }

        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, gmailFragment)
                .addToBackStack(null)
                .commit();
    }

    public void goToScanQRCode(){
//        fm.beginTransaction()
//                .hide(this)
//                .replace(R.id.contentFrameLayout, new MyQRCodeFragment())
//                .addToBackStack(null)
//                .commit();
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
    }



    public void goToAddMobileContacts(){
        if(mobileFragment==null){
            mobileFragment = new AddMobileContactsFragment();
        }

        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, mobileFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public AddFriendsPresenter createPresenter() {
        return new AddFriendsPresenter(getContext());
    }
}
