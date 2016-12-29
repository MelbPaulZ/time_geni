package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.ListviewInviteeFriendItemBinding;
import org.unimelb.itime.bean.BaseContact;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.ContactFilterUtil;
import org.unimelb.itime.ui.presenter.contact.InviteFriendPresenter;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;
import org.unimelb.itime.widget.ContactListView;
import org.unimelb.itime.widget.InviteeGroupView;

import java.util.ArrayList;
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
    private ListView sideBarListView;
    private ContactListView searchListView;
    private ObservableList iTimeFriendItems = new ObservableArrayList<>();
    private ObservableList<ListItemViewModel> searchItems;
    private List<ITimeInviteeInterface> inviteeList;
    private List<BaseContact> friendList = new ArrayList<>();
    private List<BaseContact> searchList;
    private InviteFriendPresenter presenter;
    private LinearLayout headerView;
    private boolean validInput = false;
    private String addButtonText;
    private InviteFriendAdapter adapter;
    private Map<String, Integer> positionMap;
    private boolean showAlertMsg = false;
    private Event event;

    public LinearLayout getHeaderView() {
        return headerView;
    }

    public void setHeaderView(LinearLayout headerView) {
        this.headerView = headerView;
    }

    public InviteFriendViewModel(InviteFriendPresenter presenter){
        this.presenter = presenter;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        List<ITimeInviteeInterface> inviteeList = Collections.synchronizedList(new ArrayList<ITimeInviteeInterface>());
        for(Invitee invitee: event.getInvitee()){
            inviteeList.add(invitee);
        }
        setInviteeList(inviteeList);
    }

    @Bindable
    public boolean getShowAlertMsg() {
        return showAlertMsg;
    }

    public void setShowAlertMsg(boolean showAlertMsg) {
        this.showAlertMsg = showAlertMsg;
        notifyPropertyChanged(BR.showAlertMsg);
    }


    public void removeContact(Contact contact){
        if(friendList==null){
            return;
        }
        for(BaseContact user:friendList){
            if(user.getContact().getContactUid().equals(contact.getContactUid())){
                friendList.remove(user);
                generateITimeListView(friendList);
                break;
            }
        }
    }

    public void addContact(Contact contact){
        if(friendList==null){
            return;
        }
        for(BaseContact user:friendList){
            if(user.getContact().getContactUid().equals(contact.getContactUid())){
                friendList.remove(user);
                break;
            }
        }
        friendList.add(new ITimeUser(contact));
        generateITimeListView(friendList);
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
    public boolean getValidInput() {
        return validInput;
    }

    public void setValidInput(boolean validInput) {
        this.validInput = validInput;
        notifyPropertyChanged(BR.validInput);
    }

    public void loadData(){
        presenter.getFriends(new FriendCallBack());
    }

    public ObservableList getITimeFriendItems() {
        return iTimeFriendItems;
    }

    public void setFriendList(List<BaseContact> list){
        friendList = list;
        generateITimeListView(friendList);
        sort(iTimeFriendItems);
        updatePositionMap(iTimeFriendItems);
        adapter.notifyDataSetChanged();
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

//    public void setInviteeGroupView(InviteeGroupView inviteeGroupView) {
//        this.inviteeGroupView = inviteeGroupView;
////        inviteeGroupView.setOnEditListener(getOnEditListener());
////        inviteeGroupView.setOnInviteeClickListener(getOnInviteeGroupViewItemClickListener());
//    }

    public ListView getSideBarListView() {
        return sideBarListView;
    }

    public void setSideBarListView(ListView sideBarListView) {
        this.sideBarListView = sideBarListView;
        //this.sideBarListView.setOnInviteeClickListener(getOnItemClickListener());
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
        setShowAlertMsg(false);
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
        //return ContactFilterUtil.getInstance().filter(searchList, text);
        return ContactFilterUtil.getInstance().filter(friendList, text);
    }

    public View.OnClickListener getFriendOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListviewInviteeFriendItemBinding binding = (ListviewInviteeFriendItemBinding) view.getTag();
                ListItemViewModel viewModel = (ListItemViewModel) binding.getViewModel();
                BaseContact user = (BaseContact) viewModel.getContact();
                if (viewModel.getSelected()) {
                    viewModel.setSelected(false);
                    deleteInvitee(user.getContact().getContactUid());

                } else {
                    viewModel.setSelected(true);
                    addInvitee(contactToInvitee(user.getContact(), event));
                }
            }
        };
    }

    public View.OnClickListener getSearchOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListviewInviteeFriendItemBinding binding = (ListviewInviteeFriendItemBinding) view.getTag();
                ListItemViewModel viewModel = (ListItemViewModel) binding.getViewModel();
                BaseContact user = (BaseContact) viewModel.getContact();
                for(Object vm:iTimeFriendItems) {
                    ListItemViewModel item = (ListItemViewModel) vm;
                    if(item.getContact()==user){
                        if (item.getSelected()) {
                            item.setSelected(false);
                            deleteInvitee(user.getContact().getContactUid());
                        } else {
                            item.setSelected(true);
                            addInvitee(contactToInvitee(user.getContact(), event));
                        }
                        break;
                    }
                }
            }
        };
    }

    public InviteeGroupView.OnItemClickListener getOnInviteeGroupViewItemClickListener(){
        return new InviteeGroupView.OnItemClickListener() {
            @Override
            public void onClick(View view, ITimeInviteeInterface invitee) {
                for(int i=0;i<iTimeFriendItems.size();i++){
                    ListItemViewModel vm = (ListItemViewModel) iTimeFriendItems.get(i);
                    String uid = vm.getContact().getContact().getContactUid();
                    if(uid.equals(invitee.getUserUid())){
                        vm.setSelected(false);
                        break;
                    }
                }
                deleteInvitee(invitee.getUserUid());
            }
        };
    }

    private void checkAddButtonValid(String str){
        setValidInput(presenter.isUniMelbEmail(str));
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
                presenter.onDoneClicked();
            }
        };
    }

    public View.OnClickListener getAddButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getValidInput()) {
                    addInvitee(unactivatedInvitee(addButtonText, event));
                } else {
                    setShowAlertMsg(true);
                }
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
                    return t1.getContact().compareTo(t2.getContact());
                }
            });
        }
    }

    public class FriendCallBack{
        public void success(List<BaseContact> list){
            setFriendList(list);
        }

        public void failed(){
            //Toast.makeText(presenter.getView().getActivity(), )
        }
    }

    private Invitee contactToInvitee(Contact contact, Event event) {
        Invitee invitee = new Invitee();
        invitee.setEventUid(event.getEventUid());
        // need to check if the contact in the invitee list
        invitee.setInviteeUid(getInviteeUid(contact,event));
        invitee.setUserUid(contact.getContactUid());
        invitee.setUserId(contact.getAliasName());
        invitee.setStatus("needsAction");
        invitee.setAliasPhoto(contact.getPhoto());
        invitee.setAliasName(contact.getName());
        invitee.setUserStatus(contact.getStatus());
        return invitee;
    }

    // str is email or phone
    private Invitee unactivatedInvitee(String str, Event event) {
        Invitee invitee = new Invitee();
        invitee.setUserStatus(Invitee.USER_STATUS_UNACTIVATED);
        invitee.setUserId(str);

        //please replace these two with right value
        invitee.setAliasName(str);
        invitee.setUserUid(str);

        return  invitee;
    }

    /** if the contact is already in the event's invitees, then no need of setting up a new InviteeUid,
     * otherwise use its previous inviteeUid
     * */
    private String getInviteeUid(Contact contact,Event event){
        for (Invitee invitee : event.getInvitee()){
            if(invitee.getUserUid().equals(contact.getContactUid())){
                return invitee.getInviteeUid();
            }
        }
        return AppUtil.generateUuid();
    }

    @Bindable
    public List<ITimeInviteeInterface> getInviteeList() {
        return inviteeList;
    }

    public void setInviteeList(List<ITimeInviteeInterface> inviteeList) {
        this.inviteeList = inviteeList;
        notifyPropertyChanged(BR.inviteeList);
    }

    public void addInvitee(ITimeInviteeInterface invitee){
        for(ITimeInviteeInterface i:inviteeList){
            if (i.getUserUid().equals(invitee.getUserUid())){
                notifyPropertyChanged(BR.inviteeList);
                return;
            }
        }
        inviteeList.add(invitee);
        setCountStr(inviteeList.size());
        notifyPropertyChanged(BR.inviteeList);
    }

    public void deleteInvitee(String uid){
        for(ITimeInviteeInterface i:inviteeList){
            if (i.getUserUid().equals(uid)){
                inviteeList.remove(i);
                break;
            }
        }
        setCountStr(inviteeList.size());
        notifyPropertyChanged(BR.inviteeList);
    }
}
