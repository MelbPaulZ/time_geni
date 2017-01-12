package org.unimelb.itime.ui.fragment.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentInviteFridendsBinding;
import org.unimelb.itime.databinding.ListviewInviteeHeaderBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.mvpview.contact.InviteFriendMvpView;
import org.unimelb.itime.ui.presenter.contact.InviteFriendPresenter;
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA_EVENT, event);

        EventTimeSlotViewFragment fragment = new EventTimeSlotViewFragment();
        event.setInvitee((List<Invitee>) viewModel.getSelectedist());
        fragment.setEvent(eventManager.copyCurrentEvent(event));
        getBaseActivity().openFragment(fragment, bundle);

//        event.setInvitee((List<Invitee>) viewModel.getSelectedist());
//        if(getFrom() instanceof EventCreateNewFragment){
//            if (event.getInvitee().size()>=1) {
//                // pick at least one invitee
//                EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
//                eventTimeSlotViewFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventTimeSlotViewFragment);
//            }else{
//                EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
//                beforeSendingFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, beforeSendingFragment);
//            }
//        }else

//        if (getFrom() instanceof EventTimeSlotViewFragment) {
//            if (event.getInvitee().size() >= 1) {
//                EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFrom();
//                eventTimeSlotViewFragment.setEvent(eventManager.copyCurrentEvent(event));
//                eventTimeSlotViewFragment.resetCalendar(eventManager.copyCurrentEvent(event));
//                openFragment(this, (EventTimeSlotViewFragment) getFrom());
//            } else {
//                EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
//                beforeSendingFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, beforeSendingFragment);
//            }
//        } else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment) {
//            if (event.getInvitee().size() >= 1) {
//                EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
//                eventTimeSlotViewFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventTimeSlotViewFragment);
//            } else {
//                EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
//                beforeSendingFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, beforeSendingFragment);
//            }
//        }else if (getFrom() instanceof EventEditFragment){
//            if (event.getInvitee().size()>=1) {
//                EventDetailTimeSlotFragment eventDetailTimeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
//                eventDetailTimeSlotFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventDetailTimeSlotFragment);
//            }else{
//                EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
//                eventEditFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventEditFragment);
//            }
//        } else if (getFrom() instanceof EventDetailTimeSlotFragment) {
//            if (event.getInvitee().size() > 1) {
//                EventDetailTimeSlotFragment eventDetailTimeSlotFragment = (EventDetailTimeSlotFragment) getFrom();
//                eventDetailTimeSlotFragment.setEvent(eventManager.copyCurrentEvent(event));
//                openFragment(this, eventDetailTimeSlotFragment);
//            } else {
//                EventEditFragment eventEditFragment = (EventEditFragment)getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
//                eventEditFragment.setEvent(eventManager.copyCurrentEvent(event));
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
}


