package org.unimelb.itime.ui.fragment.contact;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.databinding.FragmentProfileBinding;
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
    private View mainView;
    private boolean showAdd = false;
    private boolean showSend = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
    private boolean showRightButton = true;
    private Contact user;
    private FriendRequest request;

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

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        presenter = createPresenter();
        viewModel = new ProfileFragmentViewModel(presenter);
        viewModel.setFriend(user);
        viewModel.setRequest(request);

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
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra(BaseActivity.TASK, BaseActivity.TASK_INVITE_OTHER_CREATE_EVENT);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        EventManager.getInstance(getContext()).initNewEvent(Calendar.getInstance());
        Event event = EventManager.getInstance(getContext()).getCurrentEvent();
        event.addInvitee(InviteeUtil.getInstance().contactToInvitee(contact, event));
        startActivityForResult(intent, EventUtil.ACTIVITY_CREATE_EVENT,bundleAnimation);
    }

    public void init(Contact contact){

    }

    public Context getContext(){
        return getActivity();
    }



}
