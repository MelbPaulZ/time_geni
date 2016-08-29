package org.unimelb.itime.ui.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentEventReceiveDetailBinding;
import org.unimelb.itime.ui.mvpview.EventReceiveDetailMvpView;
import org.unimelb.itime.ui.presenter.EventReceiveDetailPresenter;
import org.unimelb.itime.ui.viewmodel.EventReceiveDetailViewModel;

/**
 * Created by Paul on 29/08/2016.
 */
public class EventReceiveDetailFragment extends MvpFragment<EventReceiveDetailMvpView,EventReceiveDetailPresenter> {

    private FragmentEventReceiveDetailBinding binding;
    @Override
    public EventReceiveDetailPresenter createPresenter() {
        return new EventReceiveDetailPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_receive_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventReceiveDetailViewModel eventReceiveDetailViewModel = new EventReceiveDetailViewModel();
        binding.setEventReceiveDetailMV(eventReceiveDetailViewModel);
    }
}
