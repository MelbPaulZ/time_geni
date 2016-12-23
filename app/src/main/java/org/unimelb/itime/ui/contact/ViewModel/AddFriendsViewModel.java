package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;

import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Presenter.AddFriendsPresenter;
import org.unimelb.itime.ui.contact.Widget.SearchBar;
import com.android.databinding.library.baseAdapters.BR;
/**
 * Created by 37925 on 2016/12/10.
 */

public class AddFriendsViewModel extends BaseObservable {
    private AddFriendsPresenter presenter;
    private boolean showSearch = false;
    private boolean showNotFound = false;
    private boolean showButtons = true;
    private boolean showTitileBack = true;
    private boolean showTitleRight = false;
    private String searchText;
    private SpannableStringBuilder inviteText;

    private String title = "Add Friends";

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

    public AddFriendsViewModel(AddFriendsPresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public boolean getShowSearch() {
        return showSearch;
    }

    public void showSearch() {
        this.showSearch = true;
        this.showNotFound = false;
        this.showButtons = false;

        notifyPropertyChanged(BR.showSearch);
        notifyPropertyChanged(BR.showNotFound);
        notifyPropertyChanged(BR.showButtons);
    }

    @Bindable
    public boolean getShowNotFound() {
        return showNotFound;
    }

    public void showNotFound() {
        this.showNotFound = true;
        this.showButtons = false;
        this.showSearch = false;
        String str = "Invite "+searchText+" to use iTime";
        inviteText = new SpannableStringBuilder(str);
        int begin = str.indexOf(searchText);
        int end = begin+searchText.length();
        inviteText.setSpan(new StyleSpan(Typeface.ITALIC),
                begin,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        notifyPropertyChanged(BR.showSearch);
        notifyPropertyChanged(BR.showNotFound);
        notifyPropertyChanged(BR.showButtons);
        notifyPropertyChanged(BR.inviteText);
    }

    @Bindable
    public SpannableStringBuilder getInviteText(){
        return inviteText;
    }

    @Bindable
    public boolean getShowButtons() {
        return showButtons;
    }

    public void showButtons() {
        this.showButtons = true;
        this.showNotFound = false;
        this.showSearch = false;
        notifyPropertyChanged(BR.showSearch);
        notifyPropertyChanged(BR.showNotFound);
        notifyPropertyChanged(BR.showButtons);
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

    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackPress();
            }
        };
    }

    public SearchBar.OnEditListener getOnEditListener(){
        return new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                if ("".equals(text)) {
                    showButtons();
                } else {
                    showSearch();
                }
                setSearchText(text);
            }
        };
    }

    public class SearchUserCallBack{
        public void gotoProfile(ITimeUser u){
            if(u==null){
                showNotFound();
            }else{
                if("".equals(u.getEmail())){
                    presenter.goToProfile(u,"phone");
                }else{
                    presenter.goToProfile(u,"email");
                }
            }
        }
    }

}
