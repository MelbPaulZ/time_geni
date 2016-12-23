package org.unimelb.itime.ui.contact.ViewModelInterface;

import org.unimelb.itime.ui.contact.Beans.BaseContact;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface ContactItem {
    BaseContact getContact();
    void setShowFirstLetter(boolean bool);
}
