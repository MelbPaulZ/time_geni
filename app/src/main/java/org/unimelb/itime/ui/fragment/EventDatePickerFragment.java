package org.unimelb.itime.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.ui.activity.EventCreateActivity;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventDatePickerFragment extends Fragment {
   private View root;

//    private EventDatePickerCommunicator communicator;


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
                EventBus.getDefault().post(new MessageEventDate(datePicker.getYear(), datePicker.getMonth(),datePicker.getDayOfMonth()));
                gotoTimePicker();
            }
        });
    }

    public void gotoTimePicker(){
        ((EventCreateActivity)getActivity()).toTimePicker(this);
    }



    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //    public interface EventDatePickerCommunicator{
//        void changeDate(int year, int month, int day);
//    }

}
