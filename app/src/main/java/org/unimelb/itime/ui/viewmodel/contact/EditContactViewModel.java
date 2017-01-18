package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.ui.presenter.contact.EditContactPresenter;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditContactViewModel extends BaseObservable{
    private EditContactPresenter presenter;
    private Contact contact;
    private String title;
    private String alias;

    public EditContactViewModel(EditContactPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        notifyPropertyChanged(BR.alias);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        setAlias(contact.getAliasName());
        notifyPropertyChanged(BR.contact);
    }

    public View.OnClickListener getCleanListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlias("");
            }
        };
    }

    public View.OnClickListener getBackOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter.getView()!=null){
                    presenter.getView().getActivity().onBackPressed();
                }
            }
        };
    }

}
