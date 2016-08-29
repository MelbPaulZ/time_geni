package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventDetailForInviteeBinding;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.EventDetailForInviteeMvpView;
import org.unimelb.itime.ui.presenter.EventDetailForInviteePresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewForInviteeViewModel;

import java.util.zip.Inflater;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventDetailForInviteeFragment extends MvpFragment<EventDetailForInviteeMvpView, EventDetailForInviteePresenter> implements EventDetailForInviteeMvpView {
    private View root;
    private FragmentEventDetailForInviteeBinding binding;
    private EventDetailViewForInviteeViewModel eventDetailViewForInviteeViewModel;
    private Event event;
    private LayoutInflater inflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_for_invitee, container, false);
        this.inflater = inflater;
        return binding.getRoot();
    }




    @Override
    public EventDetailForInviteePresenter createPresenter() {
        return new EventDetailForInviteePresenter(getContext(),inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDetailViewForInviteeViewModel = new EventDetailViewForInviteeViewModel(getPresenter());
        eventDetailViewForInviteeViewModel.setEventDetailEvent(event);
        binding.setEventDetailForInviteeVM(eventDetailViewForInviteeViewModel);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @Override
    public void gotoWeekViewCalendar() {
        ((EventDetailActivity)getActivity()).gotoWeekViewCalendar();
    }

    @Override
    public void confirmAndGotoWeekViewCalendar(Event event,boolean[] suggestTimeSlotConfirmArray) {
        ((EventDetailActivity)getActivity()).confirmAndGotoWeekViewCalendar(event,suggestTimeSlotConfirmArray);
    }

}
