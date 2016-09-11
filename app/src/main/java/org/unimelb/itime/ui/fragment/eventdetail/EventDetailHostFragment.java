package org.unimelb.itime.ui.fragment.eventdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventDetailForHostBinding;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.EventDetailForHostMvpView;
import org.unimelb.itime.ui.presenter.EventDetailForHostPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailForHostViewModel;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailHostFragment extends MvpFragment<EventDetailForHostMvpView, EventDetailForHostPresenter> implements EventDetailForHostMvpView{
    private FragmentEventDetailForHostBinding binding;
    private EventDetailForHostViewModel eventDetailForHostViewModel;
    private Event event;
    private LayoutInflater inflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_for_host, container, false);
        this.inflater = inflater;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDetailForHostViewModel = new EventDetailForHostViewModel(getPresenter());
        eventDetailForHostViewModel.setEvDtlHostEvent(event);
        binding.setHostDetailVM(eventDetailForHostViewModel);
    }

    public void setEvent(Event event){
        this.event = event;
        if (eventDetailForHostViewModel!=null) {
            eventDetailForHostViewModel.setEvDtlHostEvent(event);
        }
        
    }

    @Override
    public EventDetailForHostPresenter createPresenter() {
        return new EventDetailForHostPresenter(getContext(),inflater);
    }


    @Override
    public void toWeekView() {
        ((EventDetailActivity)getActivity()).gotoWeekViewCalendar();
    }

    @Override
    public void toAttendeeView(long time) {
        ((EventDetailActivity)getActivity()).toAttendeeView(time);
    }

    @Override
    public void toEditEvent(Event event) {
        ((EventDetailActivity)getActivity()).toEditEvent(event);
    }

    @Override
    public void viewInCalendar(String tag) {
        ((EventDetailActivity)getActivity()).toTimeSlotView(tag,event);
    }

    @Override
    public void confirmAndGotoWeekViewCalendar(Event event) {
        ((EventDetailActivity)getActivity()).confirmAndGotoWeekViewCalendar(event);
    }


}
