package org.unimelb.itime.ui.contact.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.InviteOtherContactsFragmentBinding;
import org.unimelb.itime.ui.contact.MvpView.InviteContactsMvpView;
import org.unimelb.itime.ui.contact.Presenter.InviteContactsPresenter;
import org.unimelb.itime.ui.contact.ViewModel.InviteOtherContactsViewModel;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class InviteOtherContactsFragment extends MvpFragment<InviteContactsMvpView, InviteContactsPresenter> implements InviteContactsMvpView {
    public static final int GMAIL = 1;
    public static final int FACEBOOK = 2;

    private InviteOtherContactsFragmentBinding binding;
    private InviteOtherContactsViewModel viewModel;

    public InviteOtherContactsFragment(){
        presenter = createPresenter();
        viewModel = new InviteOtherContactsViewModel(presenter);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.invite_other_contacts_fragment, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart(){
        super.onStart();
        binding.listView.setOnItemClickListener(viewModel.getOnItemClickListener());
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
    public InviteContactsPresenter createPresenter() {
        return new InviteContactsPresenter();
    }

    @Override
    public SideBarListView getSideBarListView() {
        return null;
    }
}
