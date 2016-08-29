package org.unimelb.itime.ui.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventDetailBinding;
import org.unimelb.itime.ui.mvpview.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventDetailPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventDetailFragment extends MvpFragment<EventDetailMvpView, EventDetailPresenter> {
    private View root;
    private FragmentEventDetailBinding binding;
    private EventDetailViewModel eventDetailViewModel;
    private Event event;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        return binding.getRoot();
    }


    @Override
    public EventDetailPresenter createPresenter() {
        return new EventDetailPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDetailViewModel= new EventDetailViewModel(getPresenter());
        eventDetailViewModel.setEventDetailEvent(event);
        binding.setEventDetailVM(eventDetailViewModel);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
