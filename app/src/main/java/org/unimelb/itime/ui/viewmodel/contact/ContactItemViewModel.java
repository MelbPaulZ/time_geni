package org.unimelb.itime.ui.viewmodel.contact;

import android.text.SpannableString;

import org.unimelb.itime.bean.BaseContact;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface ContactItemViewModel {
    BaseContact getContact();

    void setShowFirstLetter(boolean bool);

    boolean getShowFirstLetter();

    SpannableString getName();

    SpannableString getContactId();

    boolean getShowDetail();

    void setShowDetail(boolean showDetail);

    String getPhoto();

}
