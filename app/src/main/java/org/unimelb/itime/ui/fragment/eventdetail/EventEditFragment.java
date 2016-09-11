package org.unimelb.itime.ui.fragment.eventdetail;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditFragment extends MvpFragment<EventEditMvpView, EventEditPresenter> implements EventEditMvpView, FragmentTagListener {

    private FragmentEventEditDetailBinding binding;
    private EventEditViewModel eventEditViewModel;
    private Event event;
    private String tag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public EventEditPresenter createPresenter() {
        return new EventEditPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventEditViewModel = new EventEditViewModel(getPresenter());
        eventEditViewModel.setEventEditViewEvent(event);
        binding.setEventEditVM(eventEditViewModel);
        tag = getString(R.string.tag_edit_event);

    }

    public void setEvent(Event event){
        this.event = event;
        if (eventEditViewModel!=null){
            eventEditViewModel.setEventEditViewEvent(event);
        }
    }

    @Override
    public void toHostEventDetail(Event event) {
        ((EventDetailActivity)getActivity()).toEventDetail(event);
    }

    @Override
    public void changeLocation() {
        ((EventDetailActivity)getActivity()).toLocationPicker(tag,this);
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Subscribe
    public void getLocation(MessageLocation messageLocation){
        if (messageLocation.tag.equals(tag)){
            event.setLocation(messageLocation.locationString);
            eventEditViewModel.setEventEditViewEvent(event);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
