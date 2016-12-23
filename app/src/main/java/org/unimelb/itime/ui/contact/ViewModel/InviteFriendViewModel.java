package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.databinding.InviteeFriendItemBinding;
import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Beans.ITimeContactInterface;
import org.unimelb.itime.ui.contact.Model.ContactFilter;
import org.unimelb.itime.ui.contact.Presenter.InviteFriendPresenter;
import org.unimelb.itime.ui.contact.ViewModelInterface.ContactItem;
import org.unimelb.itime.ui.contact.Widget.ContactListView;
import org.unimelb.itime.ui.contact.Widget.InviteeGroupView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 37925 on 2016/12/7.
 */

public class InviteFriendViewModel extends BaseObservable {
    private String title = "Invitee";
    private String countStr;
    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private boolean searching = false;
    private InviteeGroupView inviteeGroupView;
    private ListView sideBarListView;
    private ContactListView searchListView;
    private ObservableList iTimeFriendItems;
    private ObservableList<ListItemViewModel> searchItems;
    private List<BaseContact> friendList;
    private List<BaseContact> searchList;
    private InviteFriendPresenter presenter;
    private LinearLayout headerView;
    private boolean addButtonClickable = false;
    private String addButtonText;
    private InviteFriendAdapter adapter;
    private Map<String, Integer> positionMap;

    public LinearLayout getHeaderView() {
        return headerView;
    }

    public void setHeaderView(LinearLayout headerView) {
        this.headerView = headerView;
    }

    public InviteFriendViewModel(InviteFriendPresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public String getAddButtonText() {
        return addButtonText;
    }

    public void setAddButtonText(String addButtonText) {
        this.addButtonText = addButtonText;
        notifyPropertyChanged(BR.addButtonText);
    }

    @Bindable
    public boolean getAddButtonClickable() {
        return addButtonClickable;
    }

    public void setAddButtonClickable(boolean addButtonClickable) {
        this.addButtonClickable = addButtonClickable;
        notifyPropertyChanged(BR.addButtonClickable);
    }

    public ObservableList getITimeFriendItems() {
        if(iTimeFriendItems == null){
            iTimeFriendItems = new ObservableArrayList<>();
            friendList = presenter.getFriends();
            generateITimeListView(friendList);
            sort(iTimeFriendItems);
            updatePositionMap(iTimeFriendItems);
        }
        return iTimeFriendItems;
    }

    public ObservableList getSearchItems() {
        if(searchItems == null){
            searchItems = new ObservableArrayList<>();
            //searchList = presenter.getSearchList();
            //searchList = friendList;
            generateSearchListView(friendList);
        }
        return searchItems;
    }

    public void generateITimeListView(List<BaseContact> list){
        iTimeFriendItems.clear();
        for(int i=0;i<list.size();i++){
            BaseContact user = list.get(i);
            ListItemViewModel item = new ListItemViewModel();
            item.setCheckable(true);
            item.setContact(user);
            item.setMatchColor(presenter.getMatchColor());
            item.setOnClickListener(getFriendOnClickListener());
            iTimeFriendItems.add(item);
        }
    }


    public void generateSearchListView(List<BaseContact> list){
        searchItems.clear();
        for(int i=0;i<list.size();i++){
            BaseContact user = list.get(i);
            ListItemViewModel item = new ListItemViewModel();
            item.setCheckable(false);
            item.setContact(user);
            item.setMatchColor(presenter.getMatchColor());
            item.setOnClickListener(getSearchOnClickListener());
            searchItems.add(item);
        }
    }

    public void setInviteeGroupView(InviteeGroupView inviteeGroupView) {
        this.inviteeGroupView = inviteeGroupView;
        inviteeGroupView.setOnEditListener(getOnEditListener());
        inviteeGroupView.setOnItemClickListener(getOnInviteeGroupViewItemClickListener());
    }

    public ListView getSideBarListView() {
        return sideBarListView;
    }

    public void setSideBarListView(ListView sideBarListView) {
        this.sideBarListView = sideBarListView;
        //this.sideBarListView.setOnItemClickListener(getOnItemClickListener());
        adapter = new InviteFriendAdapter(presenter.getView().getActivity().getApplicationContext());
        adapter.setList(getITimeFriendItems());
        getSearchItems();
        this.sideBarListView.addHeaderView(headerView);
        this.sideBarListView.setAdapter(adapter);
    }

    public ListView getSearchListView() {
        return searchListView;
    }

    public void setSearchListView(ContactListView searchListView) {
        this.searchListView = searchListView;
    }

    @Bindable
    public boolean getSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
        notifyPropertyChanged(BR.searching);
    }

    public boolean getShowTitleRight() {
        return showTitleRight;
    }

    public boolean getShowTitleBack() {
        return showTitileBack;
    }

    public void gotoInviteFacebookContacts() {
        presenter.getView().gotoInviteFacebookContacts();
    }

    public void gotoInviteGmailContacts() {
        presenter.getView().gotoInviteGmailContacts();
    }

    public void gotoInviteMobileContacts() {
        presenter.getView().gotoInviteMobileContacts();
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public String getCountStr() {
        return countStr;
    }

    public void setCountStr(int count) {
        this.countStr = count + " people accepted";
        notifyPropertyChanged(BR.countStr);
    }

    public InviteeGroupView.OnEditListener getOnEditListener(){
        return new InviteeGroupView.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                onSearch(text);
            }
        };
    }

    public void onSearch(String str){
        List<BaseContact> filterList = filterSearchList(str);
        if("".equals(str)){
            setSearching(false);
            adapter.setList(iTimeFriendItems);
        }else{
            setSearching(true);
            generateSearchListView(filterList);
            adapter.setList(searchItems);
            checkAddButtonValid(str);
        }
    }

    private List<BaseContact> filterSearchList(String text){
        //return ContactFilter.getInstance().filter(searchList, text);
        return ContactFilter.getInstance().filter(friendList, text);
    }

    public View.OnClickListener getFriendOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteeFriendItemBinding binding = (InviteeFriendItemBinding) view.getTag();
                ListItemViewModel viewModel = (ListItemViewModel) binding.getViewModel();
                BaseContact user = (BaseContact) viewModel.getContact();
                if (viewModel.getSelected()) {
                    viewModel.setSelected(false);
                    inviteeGroupView.deleteInvitee(user);
                    setCountStr(inviteeGroupView.countInvitee());
                } else {
                    viewModel.setSelected(true);
                    inviteeGroupView.addAvatarInvitee(user);
                    setCountStr(inviteeGroupView.countInvitee());
                }
                inviteeGroupView.clearInput();
            }
        };
    }

    public View.OnClickListener getSearchOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteeFriendItemBinding binding = (InviteeFriendItemBinding) view.getTag();
                ListItemViewModel viewModel = (ListItemViewModel) binding.getViewModel();
                BaseContact user = (BaseContact) viewModel.getContact();
                for(Object vm:iTimeFriendItems) {
                    ListItemViewModel item = (ListItemViewModel) vm;
                    if(item.getContact()==user){
                        if (item.getSelected()) {
                            item.setSelected(false);
                            inviteeGroupView.deleteInvitee(user);
                            setCountStr(inviteeGroupView.countInvitee());
                        } else {
                            item.setSelected(true);
                            inviteeGroupView.addAvatarInvitee(user);
                            setCountStr(inviteeGroupView.countInvitee());
                        }
                        break;
                    }
                }
                inviteeGroupView.clearInput();
            }
        };
    }

    public InviteeGroupView.OnItemClickListener getOnInviteeGroupViewItemClickListener(){
        return new InviteeGroupView.OnItemClickListener() {
            @Override
            public void onClick(View view, ITimeContactInterface invitee) {
                for(int i=0;i<iTimeFriendItems.size();i++){
                    ListItemViewModel vm = (ListItemViewModel) iTimeFriendItems.get(i);
                    ITimeContactInterface in = vm.getContact();
                    if(in == invitee){
                        vm.setSelected(false);
                        break;
                    }
                }
                setCountStr(inviteeGroupView.countInvitee());
            }
        };
    }

    private void checkAddButtonValid(String str){
        setAddButtonClickable(presenter.isEmail(str)||presenter.isPhone(str));
        setAddButtonText(str);
    }

    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackPress();
            }
        };
    }

    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //presenter.onBackPress();
            }
        };
    }

    public View.OnClickListener getAddButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ITimeContactInterface contact = new BaseContact();
                contact.setContactId(addButtonText);
                if (presenter.isEmail(addButtonText)){
                    inviteeGroupView.addEmailInvitee(contact);
                }else{
                    inviteeGroupView.addPhoneInvitee(contact);
                }
                inviteeGroupView.clearInput();
            }
        };
    }

    private void updatePositionMap(ObservableList<ContactItem> list){
        if(positionMap==null){
            positionMap = new HashMap<>();
        }
        positionMap.clear();
        for(int i=0;i<list.size();i++){
            ContactItem item = list.get(i);
            String letter =item.getContact().getSortLetters();
            if(positionMap.containsKey(letter)){
                item.setShowFirstLetter(false);
                continue;
            }else{
                positionMap.put(letter, i);
                item.setShowFirstLetter(true);
            }
        }
    }

    private void sort(ObservableList<ContactItem> list){
        if(list!=null) {
            Collections.sort(list, new Comparator<ContactItem>() {
                @Override
                public int compare(ContactItem t1, ContactItem t2) {
                    return t1.getContact().getPinyin().compareTo(t2.getContact().getPinyin());
                }
            });
        }
    }
}
