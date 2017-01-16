package org.unimelb.itime.ui.fragment.contact;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.databinding.FragmentProfileBinding;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.ui.fragment.calendars.CalendarBaseViewFragment;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.presenter.contact.ProfileFragmentPresenter;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.InviteeUtil;

import java.util.Calendar;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragment extends BaseContactFragment<ProfileMvpView, ProfileFragmentPresenter> implements ProfileMvpView{
    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;
    private EditAliasFragment editAliasFragment;
    private View mainView;
    private Contact user;
    private FriendRequest request;
    private FragmentManager fm;

    public View getContentView(){
        return getView();
    }

    public void setRequest(FriendRequest request){
        this.request = request;
        if(viewModel!=null){
            viewModel.setRequest(request);
        }
    }

    @Override
    public ProfileFragmentPresenter createPresenter() {
        return new ProfileFragmentPresenter(getActivity());
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        presenter = createPresenter();
        viewModel = new ProfileFragmentViewModel(presenter);
        viewModel.setFriend(user);
        viewModel.setRequest(request);
        fm = getFragmentManager();
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);
        binding.setViewModel(viewModel);
        mainView = binding.getRoot();
        return mainView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void editContact(MessageEditContact msg){
        this.user = msg.contact;
        if(viewModel != null){
            viewModel.setFriend(user);
        }
    }

    public void setUser(Contact user) {
        this.user = user;
        if(viewModel != null){
            viewModel.setFriend(user);
        }
    }

    public void goToInviteFragment(Contact contact){
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra("start_time", Calendar.getInstance().getTimeInMillis());
        intent.putExtra("contact", contact);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        getActivity().startActivityForResult(intent, CalendarBaseViewFragment.REQ_EVENT_CREATE,bundleAnimation);
    }

    @Override
    public void goToEditAlias(Contact contact) {
        if(editAliasFragment == null) {
            editAliasFragment = new EditAliasFragment();
        }
        editAliasFragment.setContact(user);
        fm.beginTransaction()
                .hide(this)
                .replace(R.id.contentFrameLayout, editAliasFragment)
                .addToBackStack(null)
                .commit();
    }

    public Context getContext(){
        return getActivity();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
