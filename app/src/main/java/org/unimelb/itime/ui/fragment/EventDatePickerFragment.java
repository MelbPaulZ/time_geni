package org.unimelb.itime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventDatePickerFragment extends Fragment implements FragmentTagListener{
    private View root;
    private String tag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_date_picker, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        root.findViewById(R.id.date_picker_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = (DatePicker) root.findViewById(R.id.date_picker);
                if (tag == getString(R.string.tag_start_time) || tag== getString(R.string.tag_end_time)){
                    EventBus.getDefault().post(new MessageEventDate(tag, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                    gotoTimePicker(tag);
                }else if (tag == getString(R.string.tag_end_repeat)){
                    EventBus.getDefault().post(new MessageEventDate(tag, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                    gotoCreateNewEvent();
                }
            }
        });
    }



    public void gotoTimePicker(String tag){
        ((EventCreateActivity)getActivity()).toTimePicker(this,tag);
    }

    public void gotoCreateNewEvent(){
        ((EventCreateActivity)getActivity()).toCreateEventNewFragment(this);
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
