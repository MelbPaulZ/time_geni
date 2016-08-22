package org.unimelb.itime.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentMainCalendarBinding;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends MvpFragment<MainCalendarMvpView, MainCalendarPresenter> implements MainCalendarMvpView{

    private final static String TAG = "MainCalendarFragment";

    FragmentMainCalendarBinding binding;
    MainCalendarViewModel mainCalendarViewModel;

    // put dayview and weekview in this page, set vi
    public MainCalendarFragment() {

    }

    @Override
    public MainCalendarPresenter createPresenter() {
        return new MainCalendarPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_calendar, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainCalendarViewModel = new MainCalendarViewModel(getPresenter());
        mainCalendarViewModel.setUser(new User());
        binding.setCalenarVM(mainCalendarViewModel);
    }
}
