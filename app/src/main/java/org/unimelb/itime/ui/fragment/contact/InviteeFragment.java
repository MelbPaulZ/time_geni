package org.unimelb.itime.ui.fragment.contact;

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
import org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.mvpview.contact.InviteFriendMvpView;
import org.unimelb.itime.ui.presenter.contact.InviteFriendPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.InviteFriendViewModel;
import org.unimelb.itime.widget.InviteeGroupView;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import java.util.List;

/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteeFragment extends BaseUiAuthFragment<InviteFriendMvpView, InviteFriendPresenter> implements InviteFriendMvpView {

    public final static String DATA_INVITEE = "invitee";
    public final static String DATA_EVENT = "event";

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

        viewModel = new InviteFriendViewModel(presenter);
        viewModel.setHeaderView((LinearLayout) headerBinding.getRoot());
        viewModel.setSideBarListView(binding.friendsListView);
        inviteeGroupView = headerBinding.inviteeGroupView;

        viewModel.setEvent(event);
        viewModel.loadData();

        binding.setViewModel(viewModel);
        headerBinding.setViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.invitee));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
        binding.setToolbarVM(toolbarViewModel);

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
        EventTimeSlotViewFragment fragment = new EventTimeSlotViewFragment();
        fragment.setFragment_task(EventTimeSlotViewFragment.TASK_EDIT);
        event.setInvitee((List<Invitee>) viewModel.getSelectedist());
        fragment.setData(event, null);
//        fragment.setData(eventManager.copyCurrentEvent(event));
        getBaseActivity().openFragment(fragment);

//        event.setInvitee((List<Invitee>) viewModel.getSelectedist());
//        if(getFrom() instanceof EventCreateNewFragment){
//            if (event.getInvitee().size()>=1) {
//                // pick at least one invitee
//                EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
//                eventTimeSlotViewFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventTimeSlotViewFragment);
//            }else{
//                EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
//                beforeSendingFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, beforeSendingFragment);
//            }
//        }else

//        if (getFrom() instanceof EventTimeSlotViewFragment) {
//            if (event.getInvitee().size() >= 1) {
//                EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFrom();
//                eventTimeSlotViewFragment.setData(eventManager.copyCurrentEvent(event));
//                eventTimeSlotViewFragment.resetCalendar(eventManager.copyCurrentEvent(event));
//                openFragment(this, (EventTimeSlotViewFragment) getFrom());
//            } else {
//                EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
//                beforeSendingFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, beforeSendingFragment);
//            }
//        } else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment) {
//            if (event.getInvitee().size() >= 1) {
//                EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
//                eventTimeSlotViewFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventTimeSlotViewFragment);
//            } else {
//                EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
//                beforeSendingFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, beforeSendingFragment);
//            }
//        }else if (getFrom() instanceof EventEditFragment){
//            if (event.getInvitee().size()>=1) {
//                EventDetailTimeSlotFragment eventDetailTimeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
//                eventDetailTimeSlotFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventDetailTimeSlotFragment);
//            }else{
//                EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
//                eventEditFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventEditFragment);
//            }
//        } else if (getFrom() instanceof EventDetailTimeSlotFragment) {
//            if (event.getInvitee().size() > 1) {
//                EventDetailTimeSlotFragment eventDetailTimeSlotFragment = (EventDetailTimeSlotFragment) getFrom();
//                eventDetailTimeSlotFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventDetailTimeSlotFragment);
//            } else {
//                EventEditFragment eventEditFragment = (EventEditFragment)getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
//                eventEditFragment.setData(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventEditFragment);
//            }
//        }
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


