package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.aigestudio.wheelpicker.WheelPicker;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.TimeslotCreateConfirmBinding;
import org.unimelb.itime.databinding.TimeslotCreatePickerBinding;
import org.unimelb.itime.ui.mvpview.TimeslotCreateMvpView;
import org.unimelb.itime.ui.presenter.TimeslotCommonPresenter;
import org.unimelb.itime.ui.viewmodel.TimeslotCreateViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 18/11/16.
 * This fragment is used when create a new timeSlot, then this fragment will jump up.
 */
public class EventTimeSlotCreateFragment extends EventBaseFragment<TimeslotCreateMvpView, TimeslotCommonPresenter<TimeslotCreateMvpView>>
        implements TimeslotCreateMvpView {
    private TimeslotCreateViewModel viewModel;
    private TimeSlotView timeSlotView;
    private TimeslotCreateConfirmBinding binding;
    private TimeslotCreatePickerBinding pickerBinding;
    private WheelPicker yearPicker, monthPicker, dayPicker, hourPicker, minutePicker;
    private int changeTimeType;
    private PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.timeslot_create_confirm, container, false);
        pickerBinding = DataBindingUtil.inflate(inflater, R.layout.timeslot_create_picker , container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new TimeslotCreateViewModel(getPresenter());
        binding.setVm(viewModel);
        pickerBinding.setVm(viewModel);
    }

    public void setTimeSlotView(TimeSlotView timeSlotView){
        this.timeSlotView = timeSlotView;
        viewModel.setNewTimeSlotView(timeSlotView);
    }


    @Override
    public TimeslotCommonPresenter<TimeslotCreateMvpView> createPresenter() {
        return new TimeslotCommonPresenter<>(getContext());
    }

    @Override
    public void onClickCancel() {
        closeFragment(this, (EventTimeSlotViewFragment)getFrom());
    }

    @Override
    public void onClickDone() {
        openFragment(this, (EventTimeSlotViewFragment)getFrom());
        // // TODO: 20/11/16 use event bus instead of calling another fragment method
        ((EventTimeSlotViewFragment)getFrom()).createTimeSlot(this.timeSlotView);
    }

    private void setupWheels(WheelPicker wheelPicker, List data){
        wheelPicker.setData(data);
        wheelPicker.setAtmospheric(true);
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(Color.LTGRAY);
        wheelPicker.setSelectedItemTextColor(Color.parseColor("#000000"));
        wheelPicker.setItemTextSize(45);
        wheelPicker.setIndicatorSize(5);
        wheelPicker.setVisibleItemCount(5);
    }


    @Override
    public void onChooseTime(int type, long time) {
        changeTimeType = type;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        yearPicker = (WheelPicker) pickerBinding.getRoot().findViewById(R.id.year_wheel_picker);
        setupWheels(yearPicker, EventUtil.getYears());
        yearPicker.setSelectedItemPosition(EventUtil.getYears().indexOf(year+""));
        monthPicker = (WheelPicker) pickerBinding.getRoot().findViewById(R.id.month_wheel_picker);
        setupWheels(monthPicker, EventUtil.getMonths());
        monthPicker.setSelectedItemPosition(EventUtil.getMonths().indexOf(month+""));
        monthPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                dayPicker.setData(EventUtil.getDays(position));
            }
        });
        dayPicker = (WheelPicker) pickerBinding.getRoot().findViewById(R.id.day_wheel_picker);
        setupWheels(dayPicker, EventUtil.getDays(calendar.get(Calendar.MONTH)));
        dayPicker.setSelectedItemPosition(EventUtil.getDays(calendar.get(Calendar.MONTH)).indexOf(day+""));
        hourPicker = (WheelPicker) pickerBinding.getRoot().findViewById(R.id.hour_wheel_picker);
        setupWheels(hourPicker, EventUtil.getHours());
        hourPicker.setSelectedItemPosition(EventUtil.getHours().indexOf(hour+""));
        minutePicker = (WheelPicker) pickerBinding.getRoot().findViewById(R.id.minute_wheel_picker);
        setupWheels(minutePicker, EventUtil.getMinutes());
        minutePicker.setSelectedItemPosition(EventUtil.getMinutes().indexOf(minute+""));

        popupWindow = new PopupWindow(pickerBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, 500);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationPopup);
        popupWindow.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClickPickerDone(){
        int year = yearPicker.getCurrentItemPosition() + 2010;
        int month = monthPicker.getCurrentItemPosition(); //
        int day = dayPicker.getCurrentItemPosition()+1;
        int hour = hourPicker.getCurrentItemPosition();
        int minute = minutePicker.getCurrentItemPosition()*15; // 0,15,30,45
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,hour,minute,0);
        if (changeTimeType == viewModel.STARTTIME){
            viewModel.setStartTime(calendar.getTimeInMillis());
        }
        popupWindow.dismiss();
    }


    @Override
    public void onRecommend(List<Timeslot> timeslotList) {

    }

    @Override
    public void setLeftTitleStringToVM() {
        viewModel.setLeftTitleStr(getString(R.string.cancel));
    }

    @Override
    public void setTitleStringToVM() {
        viewModel.setTitleStr(getString(R.string.new_timeslot));
    }

    @Override
    public void setRightTitleStringToVM() {
        viewModel.setRightTitleStr(getString(R.string.done));
    }

    @Override
    public void onTaskStart(int task) {

    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {

    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {

    }
}
