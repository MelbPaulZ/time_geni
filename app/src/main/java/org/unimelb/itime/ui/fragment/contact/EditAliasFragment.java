package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentEditAliasBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.contact.EditContactMvpView;
import org.unimelb.itime.ui.presenter.contact.EditContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.EditContactViewModel;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditAliasFragment extends BaseUiAuthFragment<EditContactMvpView, EditContactPresenter> implements EditContactMvpView {

    private EditContactViewModel viewModel;
    private FragmentEditAliasBinding binding;
    private Contact contact;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new EditContactViewModel(presenter);
        if(contact!=null) {
            viewModel.setContact(contact);
        }
        binding.setViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.edit_alias));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
        binding.setToolbarVM(toolbarViewModel);
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

    @Override
    public void onBack() {
        this.getActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        if(viewModel.getAlias().length()>20){
                if(presenter.getView()!=null)
                    presenter.getView().showAlert();
        }else {
            if (viewModel.getAlias().equals("")) {
                contact.setAliasName(contact.getUserDetail().getPersonalAlias());
            } else {
                contact.setAliasName(viewModel.getAlias());
            }
            presenter.editAlias(contact);
        }
    }

    public void showAlert(){
        showDialog(getString(R.string.edit_alias_alert), getString(R.string.edit_alias_alert_msg));
    }
}
