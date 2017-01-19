package org.unimelb.itime.ui.fragment.contact;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.databinding.FragmentProfileBinding;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.ui.fragment.calendars.CalendarBaseViewFragment;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.presenter.contact.ProfilePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.ui.activity.EventCreateActivity;

import java.util.Calendar;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragment extends BaseUiAuthFragment<ProfileMvpView, ProfilePresenter> implements ProfileMvpView{
    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;
    private EditAliasFragment editAliasFragment;
    private Contact user;
    private FriendRequest request;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    public View getContentView(){
        return getView();
    }

    public void setRequest(FriendRequest request){
        this.request = request;
        if(viewModel!=null){
            viewModel.setRequest(request);
        }
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter(getActivity());
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new ProfileFragmentViewModel(presenter);
        viewModel.setFriend(user);
        viewModel.setRequest(request);
        binding.setViewModel(viewModel);


        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.profile));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightDrawable(getResources().getDrawable(R.drawable.contact_more_bgwhite));
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);

        return binding.getRoot();
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
        getBaseActivity().openFragment(editAliasFragment, null, true);
//        fm.beginTransaction()
//                .hide(this)
//                .replace(R.id.contentFrameLayout, editAliasFragment)
//                .addToBackStack(null)
//                .commit();
    }

    public Context getContext(){
        return getActivity();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        viewModel.onRightClicked();
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            case ProfilePresenter.TASK_ACCEPT :
                viewModel.setShowAccept(false);
                viewModel.setShowAccepted(true);
                break;
            case ProfilePresenter.TASK_ADD :
                viewModel.setShowAdd(false);
                viewModel.setShowSent(true);
                break;
            case ProfilePresenter.TASK_BLOCK :
                viewModel.blockSuccess();
                break;
            case ProfilePresenter.TASK_UNBLOCK :
                viewModel.unblockSuccess();
                break;
            case ProfilePresenter.TASK_DELETE :
                Toast.makeText(getContext(), getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                onBack();
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        switch (taskId){
            case ProfilePresenter.TASK_ACCEPT :
                Toast.makeText(getContext(), getString(R.string.accept_fail), Toast.LENGTH_SHORT).show();
                break;
            case ProfilePresenter.TASK_ADD :
                Toast.makeText(getContext(), getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                break;
            case ProfilePresenter.TASK_BLOCK :
                Toast.makeText(getContext(), getString(R.string.block_fail), Toast.LENGTH_SHORT).show();
                break;
            case ProfilePresenter.TASK_UNBLOCK :
                Toast.makeText(getContext(), getString(R.string.unblock_fail), Toast.LENGTH_SHORT).show();
                break;
            case ProfilePresenter.TASK_DELETE :
                Toast.makeText(getContext(), getString(R.string.delete_fail), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
