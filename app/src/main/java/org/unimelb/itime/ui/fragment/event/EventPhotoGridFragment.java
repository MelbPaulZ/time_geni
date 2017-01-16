package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentPhotoGridviewBinding;
import org.unimelb.itime.ui.mvpview.EventDetailMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.List;

/**
 * Created by Paul on 1/1/17.
 */

public class EventPhotoGridFragment extends EventBaseFragment<EventDetailMvpView, EventCommonPresenter<EventDetailMvpView>>
implements EventDetailMvpView {

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
        viewModel.setEvent(event);
        binding.setVm(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public EventCommonPresenter<EventDetailMvpView> createPresenter() {
        return new EventCommonPresenter<>(getContext());
    }

    public void setEvent(Event event){
        this.event = event;
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
    public void reloadPage() {

    }

    @Override
    public void toResponse() {

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

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftTitleStr(getString(R.string.back));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.photo));
    }

    @Override
    public void setRightTitleStringToVM() {

    }

    @Override
    public ToolbarViewModel<? extends ItimeCommonMvpView> getToolbarViewModel() {
        return new ToolbarViewModel<>(this);
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

    }
}
