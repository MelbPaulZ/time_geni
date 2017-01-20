package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.ContactCheckUtil;
import org.unimelb.itime.ui.presenter.contact.AddFriendsPresenter;
import org.unimelb.itime.widget.SearchBar;
import com.android.databinding.library.baseAdapters.BR;
/**
 * Created by 37925 on 2016/12/10.
 */

public class AddFriendsViewModel extends BaseObservable {
    private AddFriendsPresenter presenter;
    private boolean showSearch = false;
    private boolean showNotFound = false;
    private boolean showButtons = true;
    private boolean showAlert = false;
    private String searchText = "";
    private boolean showTitile = true;
    private boolean showCancel = false;
    private SpannableStringBuilder inviteText;

    @Bindable
    public boolean getShowAlert() {
        return showAlert;
    }


    public void setShowAlert(boolean showAlert) {
        this.showAlert = showAlert;
        notifyPropertyChanged(BR.showAlert);
    }

    @Bindable
    public boolean getIsUnimelb(){
        return ContactCheckUtil.getInsstance().isUnimelbEmail(getPureSearchText());
    }

    public AddFriendsViewModel(AddFriendsPresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public boolean getShowSearch() {
        return showSearch;
    }

    public void showSearch() {
        setShowSearch(true);
        setShowNotFound(false);
        setShowButtons(false);
        setShowAlert(false);
        setShowTitile(false);
        setShowCancel(true);
    }

    @Bindable
    public boolean getShowNotFound() {
        return showNotFound;
    }

    public void setShowNotFound(boolean bool){
        this.showNotFound = bool;
        notifyPropertyChanged(BR.showNotFound);
    }

    public void setShowSearch(boolean bool){
        this.showSearch = bool;
        notifyPropertyChanged(BR.showSearch);
    }

    public void setShowButtons(boolean bool){
        this.showButtons = bool;
        notifyPropertyChanged(BR.showButtons);
    }

    public void showNotFound() {
        setShowNotFound(true);
        setShowButtons(false);
        setShowSearch(false);
        setShowAlert(false);
        notifyPropertyChanged(BR.isUnimelb);
        String str = "Invite "+searchText+" to use iTime";

        SpannableStringBuilder text = new SpannableStringBuilder(str);
        int begin = str.indexOf(searchText);
        int end = begin+searchText.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC),
                begin,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setInviteText(text);
    }

    @Bindable
    public SpannableStringBuilder getInviteText(){
        return inviteText;
    }

    public void setInviteText(SpannableStringBuilder inviteText){
        this.inviteText = inviteText;
        notifyPropertyChanged(BR.inviteText);
    }

    @Bindable
    public boolean getShowButtons() {
        return showButtons;
    }

    public void showButtons() {
        setShowButtons(true);
        setShowNotFound(false);
        setShowSearch(false);
        setShowTitile(true);
        setShowCancel(false);
    }

    @Bindable
    public View.OnClickListener getGmailListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToGmail();
            }
        };
    }

    @Bindable
    public View.OnClickListener getQdListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToQRCode();
            }
        };
    }

    @Bindable
    public View.OnClickListener getFacebookListenser() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToFacebook();
            }
        };
    }

    @Bindable
    public View.OnClickListener getPhoneListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToMobile();
            }
        };
    }

    public View.OnClickListener getSearchButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = getPureSearchText();
                presenter.findFriend(searchStr,new SearchUserCallBack());
            }
        };
    }

    public String getPureSearchText(){
        return searchText;
    }

    @Bindable
    public String getSearchText() {
        return "Search: "+ searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        notifyPropertyChanged(BR.searchText);
    }

    public SearchBar.OnEditListener getOnEditListener(){
        return new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                if ("".equals(text)) {

                } else {
                    showSearch();
                }
                setSearchText(text);
            }
        };
    }

    public View.OnClickListener getCancelListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showButtons();
            }
        };
    }

    public void sendInvite(){

    }

    public class SearchUserCallBack{
        public void gotoProfile(Contact contact){
            if(contact==null){
                showNotFound();
            }else{
               presenter.goToProfile(contact);
            }
        }
    }

    @Bindable
    public boolean getShowTitle() {
        return showTitile;
    }

    public void setShowTitile(boolean showTitile) {
        this.showTitile = showTitile;
        notifyPropertyChanged(BR.showTitle);
    }

    @Bindable
    public boolean getShowCancel(){
        return showCancel;
    }

    public void setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
        notifyPropertyChanged(BR.showCancel);
    }
}
