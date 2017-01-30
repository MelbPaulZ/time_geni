package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingRegionBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.UserMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.SettingRegionViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 30/1/17.
 */

public class SettingRegionFragment extends BaseUiAuthFragment<SettingRegionMvpView, UserPresenter<SettingRegionMvpView>>
    implements SettingRegionMvpView{

    private FragmentSettingRegionBinding binding;
    private View headerView;
    @Override
    public UserPresenter<SettingRegionMvpView> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_region, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ToolbarViewModel toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.region));
        binding.setToolbarVM(toolbarViewModel);

        SettingRegionViewModel regionViewModel = new SettingRegionViewModel(getPresenter(), -1); // -1 refers to init countries
        binding.setContentVM(regionViewModel);
        initHeaderView();

    }

    private void initHeaderView(){
        ListView regionList = (ListView) getActivity().findViewById(R.id.region_list);
        headerView = View.inflate(getContext(), R.layout.listview_region_header, null);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "current location", Toast.LENGTH_SHORT).show();
            }
        });
        regionList.addHeaderView(headerView);
    }


    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void toSelectCity(long locationId) {
        SettingRegionCityFragment fragment = new SettingRegionCityFragment();
        fragment.setTargetFragment(getTargetFragment(), SettingMyProfileFragment.REQ_REGION);
        fragment.setLocationId(locationId);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, User data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
