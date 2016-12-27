package org.unimelb.itime.ui.viewmodel.contact;

import org.unimelb.itime.bean.BaseContact;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface ContactItem {
    BaseContact getContact();
    void setShowFirstLetter(boolean bool);
}
