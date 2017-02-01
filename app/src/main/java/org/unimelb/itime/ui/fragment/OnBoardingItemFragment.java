package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentMainContactsBinding;
import org.unimelb.itime.databinding.ViewpagerOnboardingBinding;
import org.unimelb.itime.ui.viewmodel.OnBoardingItemViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ContactHomePageViewModel;

/**
 * Created by Qiushuo Huang on 2017/1/30.
 */

public class OnBoardingItemFragment extends Fragment{
    private ViewpagerOnboardingBinding binding;
    private OnBoardingItemViewModel viewModel;
    private int img;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.viewpager_onboarding, container, false);
        viewModel = new OnBoardingItemViewModel();
        viewModel.setGraphyImg(img);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    public void onResume(){
        super.onResume();
    }

    public void setImg(int img){
        this.img = img;
    }
}
