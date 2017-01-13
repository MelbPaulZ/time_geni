package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.util.ContactFilterUtil;
import org.unimelb.itime.ui.presenter.contact.ContactHomePagePresenter;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;
import org.unimelb.itime.widget.SearchBar;
import org.unimelb.itime.widget.SideBarListView;

import java.util.Collections;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/13.
 */

public class ContactHomePageViewModel  extends BaseObservable {
    private ContactHomePagePresenter presenter;
    private boolean showTitileBack = false;
    private boolean showTitleRight = true;
    private ObservableList<ListItemViewModel> items = new ObservableArrayList<>();
    private ObservableList<ListItemViewModel> searchItems =  new ObservableArrayList<>();
    private ItemView view;
    private ItemView itemView;
    private String title = "Contacts";
    private List<ITimeUser> friendList;
    private boolean searching;
    private int requestCount=0;
    private SideBarListView sideBarListView;

    @Bindable
    public boolean getSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
        notifyPropertyChanged(BR.searching);
    }

    @Bindable
    public ObservableList getItems() {
        return items;
    }

    @Bindable
    public ObservableList getSearchItems() {
        return searchItems;
    }

    public void setFriendList(List<ITimeUser> list){
        friendList = list;
        updateListView(list);
    }

    @Bindable
    public ItemView getItemView(){
        if(itemView==null){
            itemView = ItemView.of(BR.viewModel, R.layout.listview_itime_friend_item);
        }
        return itemView;
    }

    public void removeContact(Contact contact){
        if(friendList==null){
            return;
        }
        for(ITimeUser user:friendList){
            if(user.getContact().getContactUid().equals(contact.getContactUid())){
                friendList.remove(user);
                updateListView(friendList);
                break;
            }
        }
    }

    public void updateContact(Contact contact){
        for(ITimeUser user: friendList){
            if(user.getContactUid().equals(contact.getContactUid())){
                friendList.remove(user);
                friendList.add(new ITimeUser(contact));
                break;
            }
        }
        //Collections.sort(friendList);
        updateListView(friendList);
    }

    public void addContact(Contact contact){
        if(friendList==null){
            return;
        }
        for(ITimeUser user:friendList){
            if(user.getContact().getContactUid().equals(contact.getContactUid())){
                friendList.remove(user);
                break;
            }
        }
        friendList.add(new ITimeUser(contact));
        updateListView(friendList);
    }

    private void updateListView(List<ITimeUser> list){
        generateListView(list);
        sideBarListView.updateList();
    }

    private void updateSearchListView(List<ITimeUser> list){
        generateSearchListView(list);
    }

    public void generateListView(List<ITimeUser> list){
        items.clear();
        for(int i=0;i<list.size();i++){
            ITimeUser user = list.get(i);
            ListItemViewModel item = new ListItemViewModel();
            item.setCheckable(false);
            item.setContact(user);
            item.setMatchColor(presenter.getMatchColor());
            item.setShowDetail(false);
            item.setShowFirstLetter(false);
            items.add(item);
        }
    }

    public void generateSearchListView(List<ITimeUser> list){
        searchItems.clear();
        for(int i=0;i<list.size();i++){
            ITimeUser user = list.get(i);
            ListItemViewModel item = new ListItemViewModel();
            item.setCheckable(false);
            item.setContact(user);
            item.setMatchColor(presenter.getMatchColor());
            item.setShowDetail(true);
            item.setShowFirstLetter(false);
            searchItems.add(item);
        }
    }

    public void setItems(ObservableList items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

    public ItemView getView() {
        return view;
    }

    public void setView(ItemView view) {
        this.view = view;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


    public ContactHomePagePresenter getPresenter() {
        return presenter;
    }

    public ContactHomePageViewModel(ContactHomePagePresenter presenter){
        this.presenter = presenter;
    }

    public void loadData(){
        presenter.getFriends(new FriendsCallBack());
    }

    public void initData(){
        presenter.getFriends(new FriendsCallBack());
    }

    public void initSideBarListView(SideBarListView sideBarListView){
        this.sideBarListView = sideBarListView;
        sideBarListView.setData(getItems(), getItemView());
        sideBarListView.setOnItemClickListener(getOnItemClickListener());
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItemViewModel viewModel = (ListItemViewModel) adapterView.getAdapter().getItem(i);
                Contact contact = viewModel.getContact().getContact();
                presenter.getView().goToProfileFragment(contact);
            }
        };
    }

    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToAddFriendsFragment();
            }
        };
    }

    public SearchBar.OnEditListener getOnEditListener(){
        return new SearchBar.OnEditListener() {
                @Override
                public void onEditing(View view, String text) {
                    if(text.equals("")){
                        setSearching(false);
                    }else{
                        setSearching(true);
                    }
                    updateSearchListView(filterData(text));
                }
            };
    }

    public void setPresenter(ContactHomePagePresenter presenter) {
        this.presenter = presenter;
    }

    public View.OnClickListener getNewFriendListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToNewFriendFragment();
            }
        };
    }

    @Bindable
    public int getRequestCount(){
        return requestCount;
    }

    public void setRequestCount(int count){
        requestCount = count;
        notifyPropertyChanged(BR.requestCount);
    }

    private List<ITimeUser> filterData(String text){
        return ContactFilterUtil.getInstance().filterUser(friendList, text);
    }

    public class FriendsCallBack{
        public void success(List<ITimeUser> list){
            setFriendList(list);
        }

        public void failed(){

        }
    }

    public class RequestCountListener{
        public void setCount(int count){
            setRequestCount(count);
        }
    }

}
