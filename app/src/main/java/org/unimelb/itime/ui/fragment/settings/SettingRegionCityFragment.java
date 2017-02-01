package org.unimelb.itime.ui.fragment.settings;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentRegionCityBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.SettingRegionViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.UserUtil;

import static android.R.attr.fragment;

/**
 * Created by Paul on 30/1/17.
 */

public class SettingRegionCityFragment extends BaseUiAuthFragment<SettingRegionMvpView, UserPresenter<SettingRegionMvpView>>
    implements SettingRegionMvpView{
    private FragmentRegionCityBinding binding;
    private long locationId;
    private SettingRegionViewModel contentViewModel;
    @Override
    public UserPresenter<SettingRegionMvpView> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_region_city, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ToolbarViewModel toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.region));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
        toolbarViewModel.setRightClickable(true);
        binding.setToolbarVM(toolbarViewModel);

        contentViewModel= new SettingRegionViewModel(getPresenter(), locationId);
        binding.setContentVM(contentViewModel);
        initListViewHeader();
    }

    private void initListViewHeader(){
        ListView cityList = (ListView) getActivity().findViewById(R.id.city_list);
        View header = View.inflate(getContext(), R.layout.listview_city_header, null);
        cityList.addHeaderView(header);
    }



    public void setLocationId(long locationId){
        this.locationId = locationId;
    }


    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        presenter.updateProfile(contentViewModel.getUser());

    }

    @Override
    public void toSelectCity(long locationId) {

    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    /**
     * this is the right way to use backFragment.. and clear popupBackStack
     * @param taskId
     * @param data
     */
    @Override
    public void onTaskSuccess(int taskId, User data) {
        hideProgressDialog();
        SettingMyProfileFragment fragment = (SettingMyProfileFragment) getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName());
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getBaseActivity().backFragment(fragment);
        fragment.onActivityResult(SettingMyProfileFragment.REQ_REGION, Activity.RESULT_OK, null);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }
}
