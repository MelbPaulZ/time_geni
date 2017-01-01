package org.unimelb.itime.ui.fragment.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentAddFriendBinding;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.presenter.contact.AddFriendsPresenter;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;
import org.unimelb.itime.ui.viewmodel.contact.AddFriendsViewModel;

/**
 * Created by 37925 on 2016/12/10.
 */

public class AddFriendsFragment extends BaseUiFragment<AddFriendsMvpView, AddFriendsPresenter> implements AddFriendsMvpView{

    android.support.v4.app.FragmentManager fm;
    FragmentAddFriendBinding binding;
    AddFriendsViewModel mainViewModel;
    AddOtherContactsFragment gmailFragment;
    AddOtherContactsFragment facebookFragment;
    AddMobileContactsFragment mobileFragment;
    ProfileFragment profileFragment;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_friend, container, false);
        presenter = createPresenter();
        mainViewModel = new AddFriendsViewModel(presenter);
        binding.setMainViewModel(mainViewModel);
        fm = getFragmentManager();
        return binding.getRoot();
    }

    public void goToProfileFragment(Contact user){
        if(profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUser(user);

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
        startActivityForResult(new Intent(getActivity(), CaptureActivityContact.class), 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                presenter.findFriend(result, mainViewModel.new SearchUserCallBack());
            }
        }
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
