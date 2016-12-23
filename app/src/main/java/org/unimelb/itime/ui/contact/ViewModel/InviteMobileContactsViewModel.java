package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Model.ContactFilter;
import org.unimelb.itime.ui.contact.Presenter.InviteContactsPresenter;
import org.unimelb.itime.ui.contact.Widget.SearchBar;
import com.android.databinding.library.baseAdapters.BR;
import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class InviteMobileContactsViewModel extends BaseObservable {

    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private InviteContactsPresenter presenter;
    private String title = "Mobile Contacts";
    private ObservableList items;
    private List<BaseContact> contactList;
    private List<BaseContact> selectedList = new ArrayList<>();

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

    public InviteMobileContactsViewModel(InviteContactsPresenter presenter){
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

    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        return ItemView.of(BR.viewModel, R.layout.mobile_contacts_item);
    }

    private void updateListView(List<BaseContact> list){
        generateListView(list);
        presenter.getSideBarListView().updateList();
    }

    private void generateListView(List<BaseContact> list){
        items.clear();
        for(int i=0;i<list.size();i++){
            BaseContact contact = list.get(i);
            ListItemViewModel item = new ListItemViewModel();
            item.setContact(contact);
            item.setMatchColor(presenter.getMatchColor());
            if(selectedList.contains(contact)){
                item.setSelected(true);
            }else{
                item.setSelected(false);
            }
            items.add(item);
        }
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItemViewModel vm = (ListItemViewModel) adapterView.getAdapter().getItem(i);
                if(vm.getSelected()){
                    vm.setSelected(false);
                    selectedList.remove(vm.getContact());
                }else{
                    vm.setSelected(true);
                    selectedList.add(vm.getContact());
                }
            }
        };
    }

    public void setItems(ObservableList items) {
        this.items = items;
    }

}
