package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateBeforeSendingBinding;
import org.unimelb.itime.messageevent.MessageEventEvent;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingFragment extends MvpFragment<EventCreateDetailBeforeSendingMvpView, EventCreateDetailBeforeSendingPresenter> {
    private FragmentEventCreateBeforeSendingBinding binding;
    private EventCreateDetailBeforeSendingViewModel eventCreateDetailBeforeSendingViewModel;
    private Event event;
    private EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_before_sending, container, false);
        eventCreateDetailBeforeSendingFragment = this;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        this.event = (Event) bundle.getSerializable(getString(R.string.new_event));
        eventCreateDetailBeforeSendingViewModel = new EventCreateDetailBeforeSendingViewModel(getPresenter(),this.event);
        binding.setNewEventDetailVM(eventCreateDetailBeforeSendingViewModel);
        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        initListeners();
    }

    public void initListeners(){
        Button sendBtn = (Button) binding.getRoot().findViewById(R.id.event_detail_before_sending_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EventCreateActivity)getActivity()).sendEvent(event);
            }
        });


        Button cancelBtn = (Button) binding.getRoot().findViewById(R.id.event_detail_before_sending_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EventCreateActivity)getActivity()).goBackToTimeSlot(eventCreateDetailBeforeSendingFragment);
            }
        });
    }

    @Override
    public EventCreateDetailBeforeSendingPresenter createPresenter() {
        return new EventCreateDetailBeforeSendingPresenter(getContext());
    }


    public void setEvent(Event event) {
        this.event = event;
    }

}
