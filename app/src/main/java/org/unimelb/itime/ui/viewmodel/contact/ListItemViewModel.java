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
 * Created by 37925 on 2016/12/5.
 */

public class ListItemViewModel extends BaseObservable implements ContactItem {

    private BaseContact contact;
    private boolean selected;
    private boolean checkable = true;
    private boolean showFirstLetter = false;
    private SpannableString name;
    private SpannableString contactId;
    private int matchColor;
    private View.OnClickListener onClickListener;

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
        notifyPropertyChanged(BR.onClickListener);
    }

    @Bindable
    public boolean getShowFirstLetter() {
        return showFirstLetter;
    }

    public void setShowFirstLetter(boolean showFirstLetter) {
        this.showFirstLetter = showFirstLetter;
        notifyPropertyChanged(BR.showFirstLetter);
    }

    @Bindable
    public SpannableString getName() {
       return changeMatchColor(contact.getName(), contact.getMatchStr());
    }

    public void setName(SpannableString name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public SpannableString getContactId() {
        return changeMatchColor(contact.getContactId(), contact.getMatchStr());
    }

    public void setContactId(SpannableString contactId) {
        this.contactId = contactId;
        notifyPropertyChanged(BR.contactId);
    }

    @Bindable
    public BaseContact getContact() {
        return contact;
    }


    public void setContact(BaseContact contact) {
        this.contact = contact;
        notifyPropertyChanged(BR.contact);
    }

    @Bindable
    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public boolean isSelected() {
        return selected;
    }

    @Bindable
    public boolean getCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
        notifyPropertyChanged(BR.checkable);
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
    public void setMatchColor(int color){
        matchColor = color;
    }
}
