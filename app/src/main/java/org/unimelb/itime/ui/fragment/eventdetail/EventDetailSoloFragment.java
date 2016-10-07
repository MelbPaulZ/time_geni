package org.unimelb.itime.ui.fragment.eventdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentSoloEventDetailBinding;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventDetailSoloMvpView;
import org.unimelb.itime.ui.presenter.EventDetailSoloPresenter;
import org.unimelb.itime.ui.viewmodel.EventSoloDetailViewModel;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventDetailSoloFragment extends BaseUiFragment<EventDetailSoloMvpView, EventDetailSoloPresenter> implements EventDetailSoloMvpView {
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
        if (event == null) {
            event = EventManager.getInstance().copyCurrentEvent(EventManager.getInstance().getCurrentEvent());
        }
        eventSoloDetailViewModel = new EventSoloDetailViewModel(getPresenter());
        eventSoloDetailViewModel.setSoloEvent(event);
        binding.setSoloDetailVM(eventSoloDetailViewModel);
    }


    @Override
    public EventDetailSoloPresenter createPresenter() {
        return new EventDetailSoloPresenter(getContext());
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
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void toEditEvent() {
        EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
        eventEditFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, eventEditFragment);
    }
}
