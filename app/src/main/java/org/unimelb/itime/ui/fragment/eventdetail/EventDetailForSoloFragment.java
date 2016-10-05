package org.unimelb.itime.ui.fragment.eventdetail;

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
public class EventDetailForSoloFragment extends MvpFragment<EventDetailForSoloMvpView, EventDetailForSoloPresenter> implements EventDetailForSoloMvpView {
    private FragmentSoloEventDetailBinding binding;
    private Event event;
    private EventSoloDetailViewModel eventSoloDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solo_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventSoloDetailViewModel = new EventSoloDetailViewModel(getPresenter(),this.event);
        binding.setSoloDetailVM(eventSoloDetailViewModel);
    }


    @Override
    public EventDetailForSoloPresenter createPresenter() {
        return new EventDetailForSoloPresenter(getContext());
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        if(eventSoloDetailViewModel!=null){
            eventSoloDetailViewModel.setSoloEvent(event);
        }

    }

    @Override
    public void toWeekView() {
        ((EventDetailActivity)getActivity()).gotoWeekViewCalendar();
    }

    @Override
    public void toEditEvent() {
        ((EventDetailActivity)getActivity()).toEditEvent(this, event);
    }
}
