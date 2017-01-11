package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentEditAliasBinding;
import org.unimelb.itime.ui.mvpview.contact.EditContactMvpView;
import org.unimelb.itime.ui.presenter.contact.EditContactPresenter;
import org.unimelb.itime.ui.viewmodel.contact.EditContactViewModel;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditAliasFragment extends MvpFragment<EditContactMvpView, EditContactPresenter> implements EditContactMvpView {

    private EditContactViewModel viewModel;
    private FragmentEditAliasBinding binding;
    private Contact contact;

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new EditContactViewModel(presenter);
        if(contact!=null) {
            viewModel.setContact(contact);
        }
        binding.setViewModel(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_alias, container, false);
        return binding.getRoot();
    }

    @Override
    public EditContactPresenter createPresenter() {
        return new EditContactPresenter(getActivity());
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        if(viewModel!=null){
            viewModel.setContact(contact);
        }
    }
}
