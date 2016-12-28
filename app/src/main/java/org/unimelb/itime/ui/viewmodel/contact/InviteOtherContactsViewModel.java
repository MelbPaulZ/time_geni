package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.BaseContact;
import org.unimelb.itime.util.ContactFilterUtil;
import org.unimelb.itime.ui.presenter.contact.InviteContactsPresenter;
import org.unimelb.itime.widget.SearchBar;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/16.
 */

public class InviteOtherContactsViewModel extends BaseObservable {

    public static final int GMAIL = 1;
    public static final int FACEBOOK = 2;

    private int source;
    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private InviteContactsPresenter presenter;
    private String title = "";
    private ObservableList items = new ObservableArrayList<>();
    private ItemView itemView = ItemView.of(BR.viewModel, R.layout.listview_other_contact_item);
    private List<BaseContact> contactList;
    private List<BaseContact> selectedList = new ArrayList<>();

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


    public InviteOtherContactsViewModel(InviteContactsPresenter presenter) {
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
        return ContactFilterUtil.getInstance().filter(contactList, text);
    }

    private void updateListView(List<BaseContact> list){
        items.clear();
        for(BaseContact contact: list){
            ListItemViewModel item = new ListItemViewModel();
            item.setContact(contact);
            item.setMatchColor(presenter.getMatchColor());
            item.setCheckable(true);
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
