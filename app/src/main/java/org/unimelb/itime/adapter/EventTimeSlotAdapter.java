package org.unimelb.itime.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.ListviewTimeslotPickBinding;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailHostFragment;
import org.unimelb.itime.ui.viewmodel.ListPickTimeSlotViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.List;

/**
 * Created by Paul on 12/09/2016.
 */
public class EventTimeSlotAdapter extends ArrayAdapter<TimeSlot> {

    private ListviewTimeslotPickBinding binding;
    private LayoutInflater inflater;
    private Event adapterEvent;

    public EventTimeSlotAdapter(Context context, int resource, List<TimeSlot> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = DataBindingUtil.inflate(inflater, R.layout.listview_timeslot_pick, parent, false);
        ListPickTimeSlotViewModel viewModel = new ListPickTimeSlotViewModel(getContext());
        viewModel.setListPickTSEvent(adapterEvent);
        viewModel.setPosition(position); // tell view modol the position of timeslot
        binding.setListTimeslotVM(viewModel);

        return binding.getRoot();
    }

    public Event getAdapterEvent() {
        return adapterEvent;
    }

    public void setAdapterEvent(Event adapterEvent) {
        this.adapterEvent = adapterEvent;
    }


}
