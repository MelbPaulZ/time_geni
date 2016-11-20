package org.unimelb.itime.ui.fragment.eventcreate;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.TimeslotCreateConfirmBinding;
import org.unimelb.itime.ui.mvpview.TimeslotCreateMvpView;
import org.unimelb.itime.ui.presenter.TimeslotCreatePresenter;
import org.unimelb.itime.ui.viewmodel.TimeslotCreateViewModel;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;

/**
 * Created by Paul on 18/11/16.
 */
public class EventTimeSlotCreateFragment extends BaseUiFragment<TimeslotCreateMvpView, TimeslotCreatePresenter>
        implements TimeslotCreateMvpView {
    private TimeslotCreateViewModel viewModel;
    private TimeSlotView timeSlotView;
    private TimeslotCreateConfirmBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.timeslot_create_confirm, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new TimeslotCreateViewModel(getPresenter());
        binding.setVm(viewModel);
    }

    public void setTimeSlotView(TimeSlotView timeSlotView){
        this.timeSlotView = timeSlotView;
        viewModel.setNewTimeSlotView(timeSlotView);
    }


    @Override
    public TimeslotCreatePresenter createPresenter() {
        return new TimeslotCreatePresenter(getContext());
    }

    @Override
    public void onClickCancel() {
        switchFragment(this, (EventTimeSlotViewFragment)getFrom());
    }

    @Override
    public void onClickDone() {
        switchFragment(this, (EventTimeSlotViewFragment)getFrom());
        // // TODO: 20/11/16 use event bus instead of calling another fragment method
        ((EventTimeSlotViewFragment)getFrom()).createTimeSlot(this.timeSlotView);
    }


}
