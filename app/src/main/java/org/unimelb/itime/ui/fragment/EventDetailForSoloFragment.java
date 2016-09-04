package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentSoloEventDetailBinding;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.EventDetailForSoloMvpView;
import org.unimelb.itime.ui.presenter.EventDetailForSoloPresenter;
import org.unimelb.itime.ui.viewmodel.EventSoloDetailViewModel;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventDetailForSoloFragment extends MvpFragment<EventDetailForSoloMvpView, EventDetailForSoloPresenter> {
    private FragmentSoloEventDetailBinding binding;
    private Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solo_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventSoloDetailViewModel eventSoloDetailViewModel = new EventSoloDetailViewModel(getPresenter(),this.event);
        binding.setSoloDetailVM(eventSoloDetailViewModel);
        initListener();
    }

    public void initListener(){
        Button cancelBtn = (Button) binding.getRoot().findViewById(R.id.event_solo_detail_back_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EventDetailActivity)getActivity()).gotoWeekViewCalendar();
            }
        });
    }

    @Override
    public EventDetailForSoloPresenter createPresenter() {
        return new EventDetailForSoloPresenter();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
