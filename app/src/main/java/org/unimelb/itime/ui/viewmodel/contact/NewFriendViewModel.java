package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.util.ContactFilterUtil;
import org.unimelb.itime.ui.presenter.contact.NewFriendFragmentPresenter;
import org.unimelb.itime.widget.SearchBar;

import java.util.ArrayList;
import java.util.List;
import com.android.databinding.library.baseAdapters.BR;
import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/14.
 */

public class NewFriendViewModel extends BaseObservable {
    private NewFriendFragmentPresenter presenter;
    private ObservableList<RequestFriendItemViewModel> items = new ObservableArrayList<>();
    private List<RequestFriend> requestList = new ArrayList<RequestFriend>();
    private ItemView itemView = ItemView.of(BR.viewModel, R.layout.listview_friend_request_item);
    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private String title = "New Friends";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getShowTitileBack() {
        return showTitileBack;
    }

    public void setShowTitileBack(boolean showTitileBack) {
        this.showTitileBack = showTitileBack;
    }

    public boolean getShowTitleRight() {
        return showTitleRight;
    }

    public void setShowTitleRight(boolean showTitleRight) {
        this.showTitleRight = showTitleRight;
    }

    public ItemView getItemView() {
        return itemView;
    }

    public ObservableList<RequestFriendItemViewModel> getItems() {
        return items;
    }

    public NewFriendViewModel(NewFriendFragmentPresenter presenter){
        this.presenter = presenter;
    }

    public void loadData(){
        presenter.getRequestFriendList(new RequestListCallBack());
    }

    public void setRequestList(List<RequestFriend> list){
        requestList = list;
        updateListView(requestList);
    }

    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            presenter.getView().goToAddFriendsFragment();
            }
        };
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

    private List<RequestFriend> filterData(String text){
        return ContactFilterUtil.getInstance().filterRequest(requestList, text);
    }

    private void updateListView(List<RequestFriend> list){
        items.clear();
        for(RequestFriend request: list){
            RequestFriendItemViewModel item = new RequestFriendItemViewModel(presenter);
            item.setRequestFriend(request);
            items.add(item);
        }
    }

    public class RequestListCallBack{
        public void success(List<RequestFriend> list){
            System.out.println(list.size());
            setRequestList(list);
        }

        public void fail(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
