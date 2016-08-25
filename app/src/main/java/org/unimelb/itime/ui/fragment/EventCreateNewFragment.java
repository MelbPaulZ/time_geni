package org.unimelb.itime.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.activity.EventCreateActivity;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventCreateNewFragment extends Fragment implements EventTimePickerFragment.EventTimePickerCommunicator{
    private View root;
    private Unbinder butterKnifeUnbinder;

    private String attendeeInfo = "None";
    private ArrayList<String> attendeeNameArrayList;
    private String eventTitleString;
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMin;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMin;
    private String selectStartDay;
    private String selectStartTime;
    private String selectEndDay;
    private String selectEndTime;
    private final int STARTEVENTTIME = 0;
    private final int ENDEVENTTIME = 1;
    private String calendarType;
    private String repeat;
    private String eventNoteString;
    private String eventURLString;

    private boolean isChangingStartTime = true;
    private boolean isEndTimeChanged = false;

    // next do bundles

    @BindView(R.id.my_toolbar)Toolbar eventToolBar;
    @BindView(R.id.create_event_cancel_button)Button eventCancelBtn;
    @BindView(R.id.create_event_send_button)Button eventSendBtn;
    @BindView(R.id.create_event_title_edittext)EditText eventTitleEditText;
    @BindView(R.id.create_event_location_tv)TextView eventLocationTV;
    @BindView(R.id.create_event_attendee_tv)TextView eventAttendeeTV;
    @BindView(R.id.create_event_repeat_tv)TextView eventRepeatTV;
    @BindView(R.id.create_event_start_time) TextView eventStartTimeTV;
    @BindView(R.id.create_event_end_time)TextView eventEndTimeTV;
    @BindView(R.id.create_event_url_tv)EditText eventUrlEditText;
    @BindView(R.id.create_event_note_edittext) EditText eventNoteEditText;
    @BindView(R.id.create_event_calendar_type_tv)TextView eventCalendarTypeTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_create,container,false);
        butterKnifeUnbinder = ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //set bar
        ((EventCreateActivity)getActivity()).setSupportActionBar(eventToolBar);
        ((EventCreateActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        initListener();

        getBundle();

        // read bundles
        showData();
    }




    @OnClick({R.id.create_event_start_time})
    public void gotoTimePicker(){
        isChangingStartTime = true;
        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        ((EventCreateActivity)getActivity()).toDatePicker(this);
    }

    @OnClick(R.id.create_event_end_time)
    public void gotoTimePicker2(){
        isChangingStartTime = false;
        ((EventCreateActivity)getActivity()).toDatePicker(this);
    }

    @OnClick(R.id.create_event_calendar_type_tv)
    public void chooseCalendarType(){
        final CharSequence types[] = new CharSequence[] {"Work", "Private", "Group", "Public"};

        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
        builder.setTitle("Choose a calendar type");
        builder.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                calendarType = (String) types[i];
                showData();
            }
        });
        builder.show();
    }

    @OnClick(R.id.create_event_repeat_tv)
    public void chooseRepeat(){
        final CharSequence repeats[] = new CharSequence[]{"None","Daily","Weekly","Monthly"};
        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
        builder.setTitle("Choose a repeat type");
        builder.setItems(repeats, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                repeat = (String) repeats[i];
                showData();
            }
        });
        builder.show();
    }


    @OnClick(R.id.create_event_url_tv)
    public void gotoUrl(){
        Uri uri;
        if (eventUrlEditText.getText().toString().startsWith("http")){
            uri = Uri.parse(eventURLString);
        }else {
            uri = Uri.parse("http://"+eventURLString);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnifeUnbinder.unbind();
    }


    public void initListener(){
        // init title change listener
        eventTitleEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    eventTitleString = eventTitleEditText.getText().toString();
                }
            }
        });

        eventNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    eventNoteString = eventNoteEditText.getText().toString();
                }
            }
        });

        eventUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0)
                    eventURLString = eventUrlEditText.getText().toString();
            }
        });

//        eventUrlEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                eventTitleEditText.setFocusable(false);
//                Uri uri;
//                if (eventUrlEditText.getText().toString().startsWith("http")){
//                    uri = Uri.parse(eventURLString);
//                }else {
//                    uri = Uri.parse("http://"+eventURLString);
//                }
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });




    }


    public void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            if(bundle.containsKey(getString(R.string.event_attendee))) {
                attendeeNameArrayList = bundle.getStringArrayList(getString(R.string.event_attendee));
                if (attendeeNameArrayList != null) {
                    if (attendeeNameArrayList.size() > 1) {
                        attendeeInfo = attendeeNameArrayList.get(0) + " and " + String.valueOf(attendeeNameArrayList.size() - 1) + " more";
                    } else {
                        attendeeInfo = attendeeNameArrayList.get(0);
                    }
                }
            }

            if (bundle.containsKey(getString(R.string.event_title))){
                eventTitleString = bundle.getString(getString(R.string.event_title));
            }

            if (bundle.containsKey(getString(R.string.event_start_day))){
                startDay = bundle.getInt(getString(R.string.event_start_day));
                startMonth = bundle.getInt(getString(R.string.event_start_month));
                startYear = bundle.getInt(getString(R.string.event_start_year));
                selectStartDay = String.format("%d.%d.%d",startYear,startMonth,startDay);
            }

            if(bundle.containsKey(getString(R.string.event_start_hour))){ // contains hour must contains minute
                startHour = bundle.getInt(getString(R.string.event_start_hour));
                startMin = bundle.getInt(getString(R.string.event_start_minute));
                selectStartTime = String.format("%d:%d", startHour, startMin);
            }

            if (bundle.containsKey(getString(R.string.event_end_day))){
                endDay = bundle.getInt(getString(R.string.event_end_day));
                endMonth = bundle.getInt(getString(R.string.event_end_month));
                endYear = bundle.getInt(getString(R.string.event_end_year));
                selectEndDay = String.format("%d.%d.%d ",endYear,endMonth,endDay);
            }

            if (bundle.containsKey(getString(R.string.event_end_hour))){
                endHour = bundle.getInt(getString(R.string.event_end_hour));
                endMin = bundle.getInt(getString(R.string.event_end_minute));
                selectEndTime = String.format("%d:%d",endHour,endMin);
            }
        }
    }

    public void showData(){
        if (eventTitleString!=null)
            eventTitleEditText.setText(eventTitleString);
        eventStartTimeTV.setText(getSelectDayTimeString(startYear,startMonth,startDay,startHour,startMin));
        eventEndTimeTV.setText(getSelectDayTimeString(endYear,endMonth,endDay,endHour,endMin));
        if (calendarType!=null)
            eventCalendarTypeTV.setText(calendarType);
        if (repeat!=null)
            eventRepeatTV.setText(repeat);
        if (eventURLString!=null)
            eventUrlEditText.setText(eventURLString);
        if (eventNoteString!=null)
            eventNoteEditText.setText(eventNoteString);
        eventAttendeeTV.setText(attendeeInfo);
    }

    private String getSelectDayTimeString(int year, int month, int day, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,hour,minute);
        String eventDayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String eventDayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String eventMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String eventHour = String.valueOf(hour);
        String eventMinute = String.valueOf(minute);
        String amOrPm = hour>=12? "PM":"AM";

        return eventDayOfWeek + " " + eventDayOfMonth + "/" +
                eventMonth + " " + eventHour + ":" + eventMinute + " " + amOrPm;
    }

    private String getDayOfWeek(int dayOfWeek){
        switch (dayOfWeek){
            case 1:
                return "SUN";
            case 2:
                return "MON";
            case 3:
                return "TUE";
            case 4:
                return "WED";
            case 5:
                return "FRI";
            case 6:
                return "SAT";
            case 7:
                return "SUN";
        }
        return "error get day of week";
    }

    @Override
    public void changeDateAndTime(int year, int month, int day, int hour, int minute) {
        if (isChangingStartTime){
            startYear = year;
            startMonth = month;
            startDay = day;
            startHour = hour;
            startMin = minute;
            if (!isEndTimeChanged){
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour+1,minute);
                endYear = calendar.get(Calendar.YEAR);
                endMonth = calendar.get(Calendar.MONTH);
                endDay = calendar.get(Calendar.DAY_OF_MONTH);
                endHour = calendar.get(Calendar.HOUR_OF_DAY);
                endMin = calendar.get(Calendar.MINUTE);
            }
        }else{
            endYear = year;
            endMonth = month;
            endDay = day;
            endHour = hour;
            endMin = minute;
            isEndTimeChanged = true;
        }
        showData();
    }
}
