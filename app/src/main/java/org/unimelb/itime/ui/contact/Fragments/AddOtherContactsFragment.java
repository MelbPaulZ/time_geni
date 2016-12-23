package org.unimelb.itime.ui.contact.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.AddOtherContactsFragmentBinding;
import org.unimelb.itime.ui.contact.MvpView.AddContactsMvpView;
import org.unimelb.itime.ui.contact.Presenter.AddContactsPresenter;
import org.unimelb.itime.ui.contact.ViewModel.AddOtherContactsViewModel;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddOtherContactsFragment extends MvpFragment<AddContactsMvpView, AddContactsPresenter> implements AddContactsMvpView {
    public static final int GMAIL = 1;
    public static final int FACEBOOK = 2;

    private AddOtherContactsFragmentBinding binding;
    private AddOtherContactsViewModel viewModel;

    public AddOtherContactsFragment(){
        presenter = createPresenter();
        viewModel = new AddOtherContactsViewModel(presenter);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.add_other_contacts_fragment, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.refreshListView();
    }

    public void setSource(int source){
        viewModel.setSource(source);
    }

    @Override
    public AddContactsPresenter createPresenter() {
        return new AddContactsPresenter();
    }

    @Override
    public SideBarListView getSideBarListView() {
        return null;
    }
}
