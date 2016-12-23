package org.unimelb.itime.ui.contact.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.AddMobileContactsFragmentBinding;
import org.unimelb.itime.ui.contact.MvpView.AddContactsMvpView;
import org.unimelb.itime.ui.contact.Presenter.AddContactsPresenter;
import org.unimelb.itime.ui.contact.ViewModel.AddMobileContactsViewModel;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddMobileContactsFragment extends MvpFragment<AddContactsMvpView, AddContactsPresenter> implements AddContactsMvpView {

    private AddMobileContactsFragmentBinding binding;
    private AddMobileContactsViewModel viewModel;

    public AddMobileContactsFragment(){
        presenter = createPresenter();
        viewModel = new AddMobileContactsViewModel(presenter);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.add_mobile_contacts_fragment, container, false);
        binding.setMainViewModel(viewModel);
        return binding.getRoot();
    }

    public void onStart(){
        super.onStart();
        viewModel.initSideBarListView();
    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.refreshListView();
    }

    @Override
    public AddContactsPresenter createPresenter() {
        return new AddContactsPresenter();
    }

    @Override
    public SideBarListView getSideBarListView() {
        return binding.sortListView;
    }
}
