package org.unimelb.itime.ui.contact.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.InviteMobileContactsFragmentBinding;
import org.unimelb.itime.ui.contact.MvpView.InviteContactsMvpView;
import org.unimelb.itime.ui.contact.Presenter.InviteContactsPresenter;
import org.unimelb.itime.ui.contact.ViewModel.InviteMobileContactsViewModel;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class InviteMobileContactsFragment extends MvpFragment<InviteContactsMvpView, InviteContactsPresenter> implements InviteContactsMvpView {

    private InviteMobileContactsFragmentBinding binding;
    private InviteMobileContactsViewModel viewModel;

    public InviteMobileContactsFragment(){
        presenter = createPresenter();
        viewModel = new InviteMobileContactsViewModel(presenter);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.invite_mobile_contacts_fragment, container, false);
        binding.setMainViewModel(viewModel);
        return binding.getRoot();
    }

    public void onStart(){
        super.onStart();
        SideBarListView listView = binding.sortListView;
        listView.setData(viewModel.getItems(), viewModel.getItemView());
        listView.setOnItemClickListener(viewModel.getOnItemClickListener());
    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.refreshListView();
    }

    @Override
    public InviteContactsPresenter createPresenter() {
        return new InviteContactsPresenter();
    }

    @Override
    public SideBarListView getSideBarListView() {
        return binding.sortListView;
    }
}
