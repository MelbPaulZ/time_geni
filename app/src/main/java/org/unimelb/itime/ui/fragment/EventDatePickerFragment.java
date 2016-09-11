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

    public final static int TASK_SELECT_START_TIME = 1001;
    public final static int TASK_SELECT_END_TIME = 1002;
    public final static int TASK_SELECT_END_REPEAT_DATE = 1002;
    public final static int TASK_SELECT_ = 1002;

    private int taskId = 0;


//    public OnEventDatePickerListener onEventDatePickerListener;

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
                }else if (tag == getString(R.string.tag_create_event_before_sending)){
                    EventBus.getDefault().post(new MessageEventDate(tag, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                    gotoCreateEventBeforeSending();
                }

//                if(onEventDatePickerListener != null){
//                    onEventDatePickerListener.onComplete(taskId, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
//                }
//
//                // for cancel btn
//                if(onEventDatePickerListener != null){
//                    onEventDatePickerListener.onCancel(taskId);
//                }
            }
        });


    }

    public void show(){
//        getFragmentManager().beginTransaction().
    }

    public void hide(){

    }




    public void gotoTimePicker(String tag){
        ((EventCreateActivity)getActivity()).toTimePicker(this,tag);
    }

    public void gotoCreateNewEvent(){
        ((EventCreateActivity)getActivity()).toCreateEventNewFragment(this);
    }

    public void gotoCreateEventBeforeSending(){
        ((EventCreateActivity)getActivity()).toNewEventDetailBeforeSending(this);
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }


//    public interface OnEventDatePickerListener{
//        void onComplete(int taskId, int year, int month, int day);
//        void onCancel(int task);
//    }
//
//    public void setOnEventDatePickerListener(OnEventDatePickerListener onEventDatePickerListener){
//        this.onEventDatePickerListener = onEventDatePickerListener;
//    }
//
//    public void setTaskId(int taskId){
//        this.taskId = taskId;
//    }
//
//    public int getTaskId(){
//        return this.taskId;
//    }

}
