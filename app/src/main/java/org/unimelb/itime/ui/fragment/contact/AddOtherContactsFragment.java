package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentAddOtherContactsBinding;
import org.unimelb.itime.ui.mvpview.contact.AddContactsMvpView;
import org.unimelb.itime.ui.presenter.contact.AddContactsPresenter;
import org.unimelb.itime.ui.viewmodel.contact.AddOtherContactsViewModel;
import org.unimelb.itime.widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddOtherContactsFragment extends BaseUiFragment<AddContactsMvpView, AddContactsPresenter> implements AddContactsMvpView {
    public static final int GMAIL = 1;
    public static final int FACEBOOK = 2;

    private FragmentAddOtherContactsBinding binding;
    private AddOtherContactsViewModel viewModel;

    public AddOtherContactsFragment(){
        presenter = createPresenter();
        viewModel = new AddOtherContactsViewModel(presenter);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_other_contacts, container, false);
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
