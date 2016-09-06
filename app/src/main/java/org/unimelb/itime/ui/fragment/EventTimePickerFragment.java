package org.unimelb.itime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageEventTime;
import org.unimelb.itime.ui.activity.EventCreateActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Paul on 23/08/2016.
 */
//public class EventTimePickerFragment extends Fragment implements EventDatePickerFragment.EventDatePickerCommunicator{

public class EventTimePickerFragment extends Fragment implements FragmentTagListener {

    private View root;
    private Unbinder butterKnifeUnbinder;
    private String tag;

    @BindView(R.id.time_picker)TimePicker timePicker;
    @BindView(R.id.time_picker_button)Button timePickerBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_time_picker, container, false);
        butterKnifeUnbinder = ButterKnife.bind(this,root);
        return root;
    }

    @OnClick(R.id.time_picker_button)
    public void gotoCreateNewEventFragment(){
        if (tag == getString(R.string.tag_start_time) || tag == getString(R.string.tag_end_time))
            EventBus.getDefault().post(new MessageEventTime(tag,timePicker.getCurrentHour(),timePicker.getCurrentMinute()));
        ((EventCreateActivity)getActivity()).toCreateEventNewFragment(this);
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
