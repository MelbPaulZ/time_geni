package org.unimelb.itime.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.databinding.FragmentMainCalendarBinding;
import org.unimelb.itime.model.User;
import org.unimelb.itime.viewmodel.MainCalendarViewModel;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends BaseUiAuthFragment{

    private final static String TAG = "MainCalendarFragment";

    FragmentMainCalendarBinding binding;
    MainCalendarViewModel mainCalendarViewModel;

    public MainCalendarFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_calendar, container, false);
        mainCalendarViewModel = new MainCalendarViewModel();
        mainCalendarViewModel.setUser(new User("1", "yin", "123"));
        binding.setCalenarVM(mainCalendarViewModel);
        return binding.getRoot();
    }






}
