package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.ui.presenter.contact.NewFriendFragmentPresenter;
import com.android.databinding.library.baseAdapters.BR;


/**
 * Created by 37925 on 2016/12/9.
 */

public class RequestFriendItemViewModel extends BaseObservable {

    private RequestFriend requestFriend;
    private NewFriendFragmentPresenter presenter;
    private View.OnClickListener onClickListener;

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.gotoProfile(requestFriend);
            }
        };
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyPropertyChanged(BR.onClickListener);
    }

    @Bindable
    public String getDisplayStatus() {
        return requestFriend.getDisplayStatus();
    }

    public void setDisplayStatus(String displayStatus) {
        requestFriend.setDisplayStatus(displayStatus);
        notifyPropertyChanged(BR.displayStatus);
    }

    public RequestFriendItemViewModel(NewFriendFragmentPresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public RequestFriend getRequestFriend() {
        return requestFriend;
    }

    public void setRequestFriend(RequestFriend requestFriend) {
        this.requestFriend = requestFriend;
        notifyPropertyChanged(BR.requestFriend);
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.contactId);
        notifyPropertyChanged(BR.displayStatus);
    }

    @Bindable
    public SpannableString getName(){
        return changeMatchColor(requestFriend.getName(), requestFriend.getMatchStr());
    }

    @Bindable
    public SpannableString getContactId(){
        return changeMatchColor(requestFriend.getContactId(), requestFriend.getMatchStr());
    }

    public SpannableString changeMatchColor(String str, String matchStr){
        SpannableString span = new SpannableString(str);
        if(matchStr.equals("")){
            return span;
        }
        int begin = str.toLowerCase().indexOf(matchStr.toLowerCase());
        int end = begin+matchStr.length();
        if(begin==-1){
            return span;
        }
        int color = presenter.getMatchColor();
        if(color!=-1) {
            span.setSpan(new ForegroundColorSpan(color),
                    begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    public void accept(){
        presenter.acceptRequest(requestFriend, new AcceptCallBack());
    }

    public class AcceptCallBack{
        public void accept(){
            setDisplayStatus(FriendRequest.DISPLAY_STATUS_ADDED);
        }

        public void fail(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.accept_fail),Toast.LENGTH_SHORT);
        }
    }
}
