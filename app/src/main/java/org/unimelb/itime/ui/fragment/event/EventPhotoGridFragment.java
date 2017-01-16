package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentPhotoGridviewBinding;
import org.unimelb.itime.ui.mvpview.EventDetailMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;
import org.unimelb.itime.ui.viewmodel.EventGridPhotoViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.util.List;

/**
 * Created by Paul on 1/1/17.
 */

public class EventPhotoGridFragment extends BaseUiAuthFragment implements ItimeCommonMvpView{

    private FragmentPhotoGridviewBinding binding;
    private Event event;
    private ToolbarViewModel toolbarViewModel;
    private EventGridPhotoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_gridview, container, false);
        return binding.getRoot();
    }

    @Override
    public MvpBasePresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventGridPhotoViewModel();
        initToolbar();
        binding.setVm(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initToolbar() {
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.back));
        toolbarViewModel.setTitleStr(getString(R.string.photo));
    }


    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

    }
}
