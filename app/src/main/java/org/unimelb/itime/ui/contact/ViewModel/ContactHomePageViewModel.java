package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Model.ContactFilter;
import org.unimelb.itime.ui.contact.Presenter.ContactHomePagePresenter;
import org.unimelb.itime.ui.contact.Widget.SearchBar;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/13.
 */

public class ContactHomePageViewModel  extends BaseObservable {
    private ContactHomePagePresenter presenter;
    private boolean showTitileBack = false;
    private boolean showTitleRight = true;
    private ObservableList<ListItemViewModel> items;
    private ItemView view;
    private String title = "Contacts";
    private List<ITimeUser> friendList;
    private int requestCount=0;

    public ObservableList getItems() {
        if(items == null){
            items = new ObservableArrayList<>();
        }
        return items;
    }

    public void setFriendList(List<ITimeUser> list){
        friendList = list;
        updateListView(list);
    }

    public ItemView getItemView(){
        return ItemView.of(BR.viewModel, R.layout.itime_friend_item);
    }

    private void updateListView(List<ITimeUser> list){
        generateListView(list);
        presenter.getSideBarListView().updateList();
    }

    public void generateListView(List<ITimeUser> list){
        items.clear();
        for(int i=0;i<list.size();i++){
            ITimeUser user = list.get(i);
            ListItemViewModel item = new ListItemViewModel();
            item.setCheckable(false);
            item.setContact(user);
            item.setMatchColor(presenter.getMatchColor());
            items.add(item);
        }
    }

    public void setItems(ObservableList items) {
        this.items = items;
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
        presenter.setFriendListLisener(new FriendListLisener());
        presenter.getFriends();
        presenter.setRequestCountListener(new RequestCountListener());
        presenter.getRequestCount();
    }

    public void initData(){
        presenter.setFriendListLisener(new FriendListLisener());
        presenter.getFriends();
        presenter.setRequestCountListener(new RequestCountListener());
        presenter.getRequestCount();
    }

    public void initSideBarListView(SideBarListView sideBarListView){
        sideBarListView.setData(getItems(), getItemView());
        sideBarListView.setOnItemClickListener(getOnItemClickListener());
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItemViewModel viewModel = (ListItemViewModel) adapterView.getAdapter().getItem(i);
                ITimeUser user = (ITimeUser) viewModel.getContact();
                presenter.getView().goToProfileFragment(user);
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
                    updateListView(filterData(text));
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
        return ContactFilter.getInstance().filterUser(friendList, text);
    }

    public class FriendListLisener{
        public void setList(List<ITimeUser> list){
            setFriendList(list);
        }
    }

    public class RequestCountListener{
        public void setCount(int count){
            setRequestCount(count);
        }
    }
}
