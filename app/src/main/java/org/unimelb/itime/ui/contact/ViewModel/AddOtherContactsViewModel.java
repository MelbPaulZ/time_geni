package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Model.ContactFilter;
import org.unimelb.itime.ui.contact.Presenter.AddContactsPresenter;
import org.unimelb.itime.ui.contact.Widget.SearchBar;
import com.android.databinding.library.baseAdapters.BR;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddOtherContactsViewModel extends BaseObservable {
    public static final int GMAIL = 1;
    public static final int FACEBOOK = 2;

    private int source;
    private boolean showTitileBack = true;
    private boolean showTitleRight = false;
    private AddContactsPresenter presenter;
    private String title = "";
    private ObservableList items = new ObservableArrayList<>();
    private ItemView itemView = ItemView.of(BR.viewModel, R.layout.add_other_contact_item);
    private List<BaseContact> contactList;

    @Bindable
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
        switch (source){
            case GMAIL:
                setTitle("Google Contacts");
                contactList = presenter.getGoogleContacts();
                break;
            case FACEBOOK:
                setTitle("Facebook Contacts");
                contactList = presenter.getFacebookContacts();
                break;
        }
        updateListView(contactList);
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
    public boolean getShowTitileBack() {
        return showTitileBack;
    }

    public void setShowTitileBack(boolean showTitileBack) {
        this.showTitileBack = showTitileBack;
    }

    @Bindable
    public boolean getShowTitleRight() {
        return showTitleRight;
    }

    public void setShowTitleRight(boolean showTitleRight) {
        this.showTitleRight = showTitleRight;
    }

    public AddOtherContactsViewModel(AddContactsPresenter presenter){
        this.presenter = presenter;
    }

    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackPressed();
            }
        };
    }

    public SearchBar.OnEditListener getOnEditListener(){
        return new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                updateListView(filterData(text));
            }
        };
    }


    private List<BaseContact> filterData(String text){
        return ContactFilter.getInstance().filter(contactList, text);
    }

    private void updateListView(List<BaseContact> list){
        items.clear();
        for(BaseContact contact: list){
            AddOtherContactsItemViewModel item = new AddOtherContactsItemViewModel();
            item.setContact(contact);
            item.setMatchColor(presenter.getMatchColor());
            items.add(item);
        }
    }

    public ItemView getItemView() {
        return itemView;
    }

    public void refreshListView(){
        updateListView(contactList);
    }

    public ObservableList getItems() {
        return items;
    }
}
