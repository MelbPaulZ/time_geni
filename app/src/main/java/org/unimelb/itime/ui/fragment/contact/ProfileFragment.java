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
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentProfileBinding;
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
    public static final int MODE_CONTACT = 1;
    public static final int MODE_REQUEST = 2;
    public static final int MODE_STRANGER = 3;

    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;
    private EditAliasFragment editAliasFragment;
    private String userId = "";
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private int startMode = 1;
    private Contact contact;
    private String requestId;

    public void setRequestId(String requestId){
        this.requestId = requestId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStartMode() {
        return startMode;
    }

    public void setStartMode(int startMode) {
        this.startMode = startMode;
    }

    public View getContentView(){
        return getView();
    }

    private void hideToolbarRight(){
        toolbarViewModel.setRightDrawable(null);
        toolbarViewModel.setRightClickable(false);
    }

    private void showToolbarRight(){
        toolbarViewModel.setRightDrawable(getResources().getDrawable(R.drawable.contact_more_bgwhite));
        toolbarViewModel.setRightClickable(true);
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter(getActivity());
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(startMode == MODE_REQUEST){
            presenter.getRequest(userId);
        }else{
            presenter.getContact(userId);
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new ProfileFragmentViewModel(presenter);
        binding.setViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.profile));
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);

        return binding.getRoot();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void editContact(MessageEditContact msg){
//        this.user = msg.contact;
//        if(viewModel != null){
//            viewModel.setContact(user);
//        }
//    }

    private void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact(){
        return contact;
    }

    @Override
    public void goToInviteFragment(){
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra("start_time", Calendar.getInstance().getTimeInMillis());
        intent.putExtra("contact", contact);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        getActivity().startActivityForResult(intent, CalendarBaseViewFragment.REQ_EVENT_CREATE,bundleAnimation);
    }

    @Override
    public void goToEditAlias() {
        if(editAliasFragment == null) {
            editAliasFragment = new EditAliasFragment();
        }
        editAliasFragment.setContact(contact);
        getBaseActivity().openFragment(editAliasFragment, null, true);
    }

    public Context getContext(){
        return getActivity();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
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
            case ProfilePresenter.TASK_CONTACT :
                contact = (Contact) data;
                viewModel.setContact(contact);
                viewModel.contactMode();
                showToolbarRight();
                break;
            case ProfilePresenter.TASK_STRANGER:
                contact = (Contact) data;
                viewModel.setContact(contact);
                viewModel.strangerMode();
                hideToolbarRight();
                break;
            case ProfilePresenter.TASK_REQUEST :
                contact = (Contact) data;
                viewModel.setContact(contact);
                viewModel.requestMode();
                viewModel.setRequestId(requestId);
                hideToolbarRight();
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
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
            case ProfilePresenter.TASK_STRANGER :
                Toast.makeText(getContext(), getString(R.string.access_fail), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
