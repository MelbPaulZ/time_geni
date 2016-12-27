package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.Contact;

/**
 * Created by Qiushuo Huang on 2016/12/27.
 */

public class MessageRemoveContact {
    public final Contact contact;
    public MessageRemoveContact(Contact contact) {
        this.contact = contact;
    }
}
