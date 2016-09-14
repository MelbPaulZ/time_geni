package org.unimelb.itime.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.ListviewTimeslotPickBinding;
import org.unimelb.itime.ui.viewmodel.EventDetailForHostViewModel;

import java.util.List;

/**
 * Created by Paul on 12/09/2016.
 */
public class EventTimeSlotAdapter extends ArrayAdapter<TimeSlot> {

    private ListviewTimeslotPickBinding binding;
    private LayoutInflater inflater;
    private Event adapterEvent;
    private EventDetailForHostViewModel viewModel;

    public EventTimeSlotAdapter(Context context, int resource, List<TimeSlot> objects, EventDetailForHostViewModel viewmodel) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.viewModel = viewmodel;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null || binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.listview_timeslot_pick, parent, false);
        }
        binding.setViewmodel(viewModel);
        binding.setPosition(position);
        return binding.getRoot();
    }

    public Event getAdapterEvent() {
        return adapterEvent;
    }

    public void setAdapterEvent(Event adapterEvent) {
        this.adapterEvent = adapterEvent;
    }


}
