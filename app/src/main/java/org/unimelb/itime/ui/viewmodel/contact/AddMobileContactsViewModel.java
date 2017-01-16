package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.BaseContact;
import org.unimelb.itime.util.ContactFilterUtil;
import org.unimelb.itime.ui.presenter.contact.AddContactsPresenter;
import org.unimelb.itime.widget.SearchBar;
import org.unimelb.itime.widget.SideBarListView;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddMobileContactsViewModel extends BaseObservable {

    private boolean showTitileBack = true;
    private boolean showTitleRight = false;
    private AddContactsPresenter presenter;
    private String title = "Mobile Contacts";
    private ObservableList items;
    private List<BaseContact> contactList;

    @Bindable
    public String getTitle() {
        return title;
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

    public AddMobileContactsViewModel(AddContactsPresenter presenter){
        this.presenter = presenter;
    }

    public void initSideBarListView(){
        SideBarListView sideBarListView = presenter.getSideBarListView();
        sideBarListView.setData(getItems(), getItemView());
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
        return ContactFilterUtil.getInstance().filter(contactList, text);
    }

    public void refreshListView(){
        getItems();
        updateListView(contactList);
    }


    public ObservableList getItems() {
        if(items == null){
            items = new ObservableArrayList<>();
            contactList = presenter.getMobileContacts();
            generateListView(contactList);
        }
        return items;
    }

    public ItemView getItemView(){
        return ItemView.of(BR.viewModel, R.layout.listview_add_mobile_contact_item);
    }

    private void updateListView(List<BaseContact> list){
        generateListView(list);
        presenter.getSideBarListView().updateList();
    }

    private void generateListView(List<BaseContact> list){
        items.clear();
        for(int i=0;i<list.size();i++){
            BaseContact user = list.get(i);
            AddOtherContactsItemViewModel item = new AddOtherContactsItemViewModel();
            item.setContact(user);
            item.setMatchColor(presenter.getMatchColor());
            items.add(item);
        }
    }

    public void setItems(ObservableList items) {
        this.items = items;
    }

}
