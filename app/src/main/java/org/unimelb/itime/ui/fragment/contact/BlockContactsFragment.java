package org.unimelb.itime.ui.fragment.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.ContactHomePageBinding;
import org.unimelb.itime.databinding.FragmentBlocklistBinding;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.messageevent.contact.MessageBlockContact;
import org.unimelb.itime.messageevent.contact.MessageUnblockContact;
import org.unimelb.itime.ui.activity.AddFriendActivityContact;
import org.unimelb.itime.ui.activity.FriendRequestActivityContact;
import org.unimelb.itime.ui.activity.ProfileActivityContact;
import org.unimelb.itime.ui.mvpview.contact.BlockContactsMvpView;
import org.unimelb.itime.ui.mvpview.contact.ContactHomePageMvpView;
import org.unimelb.itime.ui.presenter.contact.BlockContactsPresenter;
import org.unimelb.itime.ui.presenter.contact.ContactHomePagePresenter;
import org.unimelb.itime.ui.viewmodel.contact.BlockContactsViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ContactHomePageViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class BlockContactsFragment extends BaseContactFragment<BlockContactsMvpView, BlockContactsPresenter> implements BlockContactsMvpView {
    private FragmentBlocklistBinding binding;
    private android.support.v4.app.FragmentManager fm;
    private ProfileFragment profileFragment;
    private BlockContactsViewModel viewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_blocklist, container, false);
        initMainView();
        fm = getFragmentManager();
        viewModel.initSideBarListView(binding.sortListView);

        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeBlock(MessageUnblockContact msg){
        viewModel.removeContact(msg.contact);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addBlock(MessageBlockContact msg){
        viewModel.addContact(msg.contact);
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
    }

    public void initMainView() {
        presenter = createPresenter();
        viewModel = new BlockContactsViewModel(presenter);
        binding.setMainViewModel(viewModel);
    }



    public void goToNewFriendFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FriendRequestActivityContact.class);
        startActivity(intent);
    }

    public void goToProfileFragment(Contact user) {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUser(user);
        fm.beginTransaction()
                .hide(this)
                .add(R.id.contentFrameLayout, profileFragment)
                .addToBackStack(null)
                .commit();
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), ProfileActivityContact.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ProfileActivityContact.USER,user);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    public void goToAddFriendsFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AddFriendActivityContact.class);
        startActivity(intent);
    }

    @Override
    public BlockContactsPresenter createPresenter() {
        return new BlockContactsPresenter(getContext());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
