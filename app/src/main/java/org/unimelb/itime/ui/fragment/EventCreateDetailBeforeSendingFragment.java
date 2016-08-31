package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateBeforeSendingBinding;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingFragment extends MvpFragment<EventCreateDetailBeforeSendingMvpView, EventCreateDetailBeforeSendingPresenter> {
    private FragmentEventCreateBeforeSendingBinding binding;
    private Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_before_sending, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventCreateDetailBeforeSendingViewModel eventCreateDetailBeforeSendingViewModel = new EventCreateDetailBeforeSendingViewModel(getPresenter());
        eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(this.event);
        binding.setNewEventDetailVM(eventCreateDetailBeforeSendingViewModel);
        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }

    @Override
    public EventCreateDetailBeforeSendingPresenter createPresenter() {
        return new EventCreateDetailBeforeSendingPresenter(getContext());
    }


    public void setEvent(Event event) {
        this.event = event;
    }
}
