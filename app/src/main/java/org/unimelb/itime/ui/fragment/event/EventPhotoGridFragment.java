package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentPhotoGridviewBinding;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;

import java.util.List;

/**
 * Created by Paul on 1/1/17.
 */

public class EventPhotoGridFragment extends EventBaseFragment<EventDetailGroupMvpView, EventCommonPresenter<EventDetailGroupMvpView>>
implements EventDetailGroupMvpView{

    private FragmentPhotoGridviewBinding binding;
    private Event event;
    private EventDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_gridview, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventDetailViewModel(getPresenter());
        binding.setVm(viewModel);
    }

    @Override
    public EventCommonPresenter<EventDetailGroupMvpView> createPresenter() {
        return new EventCommonPresenter<>(getContext());
    }

    public void setEvent(Event event){
        this.event = event;
        if (viewModel!=null) {
            viewModel.setEvDtlHostEvent(event);
        }

    }

    @Override
    public void toEditEvent() {

    }

    @Override
    public void viewInCalendar() {

    }

    @Override
    public void viewInviteeResponse(Timeslot timeSlot) {

    }

    @Override
    public void gotoGridView() {

    }

    @Override
    public void onClickPhotoGridBack() {
        EventDetailFragment eventDetailFragment = (EventDetailFragment) getFragmentManager().findFragmentByTag(EventDetailFragment.class.getSimpleName());
        closeFragment(this, eventDetailFragment);
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
