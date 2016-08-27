package org.unimelb.itime.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
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

public class EventTimePickerFragment extends Fragment {

    private View root;
    private Unbinder butterKnifeUnbinder;
//    private EventTimePickerCommunicator timePickerCommunicator;

    private int receivedYear;
    private int receivedMonth;
    private int receivedDay;

    @BindView(R.id.time_picker)TimePicker timePicker;
    @BindView(R.id.time_picker_button)Button timePickerBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_time_picker, container, false);
        butterKnifeUnbinder = ButterKnife.bind(this,root);
        return root;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }

    @OnClick(R.id.time_picker_button)
    public void gotoCreateNewEventFragment(){
        EventBus.getDefault().post(new MessageEventTime(timePicker.getCurrentHour(),timePicker.getCurrentMinute()));
        ((EventCreateActivity)getActivity()).toCreateEventNewFragment(this);
    }



    //    @OnClick(R.id.time_picker_button)
//    public void gotoCreateNewEventFragment(){
//        timePickerCommunicator.changeDateAndTime(receivedYear,receivedMonth,receivedDay, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
//        ((EventCreateActivity)getActivity()).toCreateEventNewFragment(this);
//    }
//
//    @Override
//    public void changeDate(int year, int month, int day) {
//        receivedYear = year;
//        receivedMonth = month;
//        receivedDay = day;
//    }
//
//    public interface EventTimePickerCommunicator{
//        void changeDateAndTime(int year, int month, int day, int hour, int minute);
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try{
//            timePickerCommunicator = (EventTimePickerCommunicator)activity;
//        }catch (ClassCastException e){
//            throw new ClassCastException(activity.toString()+" must implement communicator interface");
//        }
//    }
}
