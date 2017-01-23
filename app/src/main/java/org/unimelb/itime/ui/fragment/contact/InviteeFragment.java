package org.unimelb.itime.ui.fragment.contact;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.BaseContact;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentInviteFridendsBinding;
import org.unimelb.itime.databinding.ListviewInviteeHeaderBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.event.EventEditFragment;
import org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.mvpview.contact.InviteFriendMvpView;
import org.unimelb.itime.ui.presenter.contact.InviteFriendPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.InviteFriendViewModel;
import org.unimelb.itime.widget.InviteeGroupView;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import java.util.List;

import static android.R.attr.fragment;

/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteeFragment extends BaseUiAuthFragment<InviteFriendMvpView, InviteFriendPresenter> implements InviteFriendMvpView {

    public final static String DATA_INVITEE = "invitee";
    public final static String DATA_EVENT = "event";
    public final static int RESULT_CANCEL = -1;

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
    private EventManager eventManager;
    private ToolbarViewModel toolbarViewModel;

    @Override
    public InviteFriendPresenter createPresenter() {
        return new InviteFriendPresenter(getActivity());
    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        eventManager = EventManager.getInstance(getContext());

        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.invitee));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
        binding.setToolbarVM(toolbarViewModel);

        viewModel = new InviteFriendViewModel(presenter);
        viewModel.setHeaderView((LinearLayout) headerBinding.getRoot());
        viewModel.setSideBarListView(binding.friendsListView);
        inviteeGroupView = headerBinding.inviteeGroupView;

        viewModel.setEvent(event);
        viewModel.loadData();

        binding.setViewModel(viewModel);
        headerBinding.setViewModel(viewModel);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite_fridends, container, false);
        headerBinding = DataBindingUtil.inflate(inflater, R.layout.listview_invitee_header, null, false);
        fm = getFragmentManager();
        return binding.getRoot();
    }


    public void toInviteMobileContactsPage() {

        if (mobileFragment == null) {
            mobileFragment = new InviteMobileContactsFragment();
        }
        getBaseActivity().openFragment(mobileFragment);
    }

    public void toInviteGmailContactsPage() {

        if (gmailFragment == null) {
            gmailFragment = new InviteOtherContactsFragment();
            gmailFragment.setSource(InviteOtherContactsFragment.GMAIL);
        }
        getBaseActivity().openFragment(gmailFragment);
    }

    public void toInviteFacebookContactsPage() {

        if (facebookFragment == null) {
            facebookFragment = new InviteOtherContactsFragment();
            facebookFragment.setSource(InviteOtherContactsFragment.FACEBOOK);
        }
        getBaseActivity().openFragment(facebookFragment);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    //Write your code here
    @Override
    public void onNext() {
        event.setInvitee((List<Invitee>) viewModel.getSelectedist());
        if(viewModel.getSelectedist().size()>0){
            // has invitee, as a group event, to select timeslot fragment
            EventTimeSlotViewFragment fragment = new EventTimeSlotViewFragment();
            fragment.setFragment_task(EventTimeSlotViewFragment.TASK_EDIT);
            fragment.setData(event, null);
            getBaseActivity().openFragment(fragment);
        }else{
            // solo event, jump to event edit page
            EventEditFragment fragment = (EventEditFragment) getTargetFragment();
            fragment.onActivityResult(getTargetRequestCode(), RESULT_CANCEL, null);
            getFragmentManager().popBackStack();
        }
    }

    public void onBack() {
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
        getFragmentManager().popBackStack();
    }

    public void toScanQRCodePage() {
        startActivityForResult(new Intent(getActivity(), CaptureActivityContact.class), 0);
    }

    @Override
    public void setDoneable(boolean bool) {
//        toolbarViewModel.setRightClickable(bool);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                viewModel.addInvitee(result);
            }
        }
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            case InviteFriendPresenter.TASK_SEARCH_CONTACT:
                if(data instanceof Contact){
                    viewModel.addInvitee(viewModel.contactToInvitee((Contact) data));
                }else if(data instanceof String){
                    viewModel.addInvitee(viewModel.unactivatedInvitee((String) data));
                }
                break;
            case InviteFriendPresenter.TASK_FRIEND_LIST:
                viewModel.setFriendList((List<BaseContact>) data);
                break;

        }
    }

    @Override
    public void onTaskError(int taskId,Object data) {
        switch (taskId){
            case InviteFriendPresenter.TASK_SEARCH_CONTACT:
                Toast.makeText(getContext(), getResources().getString(R.string.access_fail),Toast.LENGTH_SHORT).show();
                break;
            case InviteFriendPresenter.TASK_FRIEND_LIST:
                break;

        }
    }
}


