package org.unimelb.itime.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.activity.EventCreateActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventDatePickerFragment extends Fragment {
    private View root;
    private Unbinder butterKnifeUnbinder;
    private EventDatePickerCommunicator communicator;
    @BindView(R.id.date_picker)DatePicker datePicker;
    @BindView(R.id.date_picker_button)Button datePickerBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_date_picker, container, false);
        butterKnifeUnbinder = ButterKnife.bind(this,root);
        return root;
    }

    @OnClick(R.id.date_picker_button)
    public void gotoTimePicker(){
        communicator.changeDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        ((EventCreateActivity)getActivity()).toTimePicker(this);
    }

    public interface EventDatePickerCommunicator{
        void changeDate(int year, int month,int day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            communicator = (EventDatePickerCommunicator) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement communicator interface");
        }
    }
}
