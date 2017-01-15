package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.EventResponse;
import org.unimelb.itime.databinding.FragmentEventResponseBinding;
import org.unimelb.itime.ui.mvpview.EventResponseMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventResponsePresenter;
import org.unimelb.itime.ui.viewmodel.EventResponseViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.List;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventResponseFragment extends BaseUiAuthFragment<EventResponseMvpView, EventResponsePresenter> implements EventResponseMvpView{
    private FragmentEventResponseBinding binding;
    private Event event;

    private EventResponseViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_response, container, false);
        return binding.getRoot();
    }

    @Override
    public EventResponsePresenter createPresenter() {
        return new EventResponsePresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentViewModel = new EventResponseViewModel(getPresenter());
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitleStr(getString(R.string.event_title_response));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    public void setData(Event event){
        this.event = event;
    }


    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<EventResponse> data) {

    }

    @Override
    public void onTaskError(int taskId) {

    }
}
