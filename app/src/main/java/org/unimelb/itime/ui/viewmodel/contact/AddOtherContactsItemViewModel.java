package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import org.unimelb.itime.bean.BaseContact;


/**
 * Created by 37925 on 2016/12/15.
 */

public class AddOtherContactsItemViewModel extends BaseObservable implements ContactItemViewModel {

    public static final int ADD = 0;
    public static final int ADDED = 1;
    public static final int INVITE = 2;
    public static final int INVITED = 3;
    public static final int SENT_ADD = 4;
    public static final int SENT_INVITE = 5;

    private boolean showAdd;
    private boolean showAdded;
    private boolean showSent;
    private boolean showInvite;
    private boolean showFirstLetter;

    private BaseContact contact;
    private int matchColor;


    @Bindable
    public boolean getShowFirstLetter() {
        return showFirstLetter;
    }

    public void setShowFirstLetter(boolean showFirstLetter) {
        this.showFirstLetter = showFirstLetter;
    }

    public void setMatchColor(int color){
        matchColor = color;
    }

    @Bindable
    public boolean getShowAdd() {
        return showAdd;
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
        notifyPropertyChanged(BR.showAdd);
    }

    @Bindable
    public boolean getShowAdded() {
        return showAdded;
    }

    public void setShowAdded(boolean showAdded) {
        this.showAdded = showAdded;
        notifyPropertyChanged(BR.showAdded);
    }

    @Bindable
    public boolean getShowSent() {
        return showSent;
    }

    public void setShowSent(boolean showSent) {
        this.showSent = showSent;
        notifyPropertyChanged(BR.showSent);
    }

    @Bindable
    public boolean getShowInvite() {
        return showInvite;
    }

    public void setShowInvite(boolean showInvite) {
        this.showInvite = showInvite;
        notifyPropertyChanged(BR.showInvite);
    }

    @Bindable
    public String getAddButtonText(){
//        if(contact.getDisplayStatus()==ADD){
//            return "Add";
//        }else{
//            return "Re-add";
//        }
        return null;
    }

    @Bindable
    public String getInviteButtonText(){
//        if(contact.getDisplayStatus()==INVITE){
//            return "Invite";
//        }else{
//            return "Re-invite";
//        }
        return null;
    }

    @Bindable
    public BaseContact getContact() {
        return contact;
    }

    public void setContact(BaseContact contact) {
        this.contact = contact;
        initButtons();
        notifyPropertyChanged(BR.contact);
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.contactId);
    }

    private void initButtons(){
        setShowAdd(false);
        setShowSent(false);
        setShowAdded(false);
        setShowInvite(false);
//        switch (contact.getDisplayStatus()){
//            case ADD:
//                setShowAdd(true);
//                break;
//            case SENT_ADD:
//                setShowAdd(true);
//                break;
//            case ADDED:
//                setShowAdded(true);
//                break;
//            case INVITE:
//                setShowInvite(true);
//                break;
//            case SENT_INVITE:
//                setShowInvite(true);
//                break;
//            case INVITED:
//                setShowSent(true);
//                break;
//        }
    }

    @Bindable
    public SpannableString getName(){
        return changeMatchColor(contact.getName(), contact.getMatchStr());
    }

    @Bindable
    public SpannableString getContactId(){
        return changeMatchColor(contact.getContactId(), contact.getMatchStr());
    }

    @Override
    public boolean getShowDetail() {
        return false;
    }

    @Override
    public void setShowDetail(boolean showDetail) {

    }

    @Override
    public String getPhoto() {
        return null;
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
            span.setSpan(new ForegroundColorSpan(matchColor),
                    begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public View.OnClickListener getAddListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contact.setDisplayStatus(SENT_ADD);
                setShowAdd(false);
                setShowAdded(true);
            }
        };
    }

    public View.OnClickListener getInviteListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // contact.setDisplayStatus(SENT_INVITE);
                setShowInvite(false);
                setShowSent(true);
            }
        };
    }
}
