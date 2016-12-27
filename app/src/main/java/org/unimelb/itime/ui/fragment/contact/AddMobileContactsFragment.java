package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentAddMobileContactsBinding;
import org.unimelb.itime.ui.mvpview.contact.AddContactsMvpView;
import org.unimelb.itime.ui.presenter.contact.AddContactsPresenter;
import org.unimelb.itime.ui.viewmodel.contact.AddMobileContactsViewModel;
import org.unimelb.itime.widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddMobileContactsFragment extends MvpFragment<AddContactsMvpView, AddContactsPresenter> implements AddContactsMvpView {

    private FragmentAddMobileContactsBinding binding;
    private AddMobileContactsViewModel viewModel;

    public AddMobileContactsFragment(){
        presenter = createPresenter();
        viewModel = new AddMobileContactsViewModel(presenter);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_mobile_contacts, container, false);
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
