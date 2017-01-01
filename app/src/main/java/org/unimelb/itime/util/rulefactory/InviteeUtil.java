package org.unimelb.itime.util.rulefactory;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.util.AppUtil;

/**
 * Created by Qiushuo Huang on 2016/12/30.
 */

public class InviteeUtil {
    private static InviteeUtil instance = null;

    private InviteeUtil(){};

    public static InviteeUtil getInstance(){
        if(instance == null){
            instance = new InviteeUtil();
        }
        return instance;
    }

    public Invitee contactToInvitee(Contact contact, Event event) {
        Invitee invitee = new Invitee();
        invitee.setEventUid(event.getEventUid());
        // need to check if the contact in the invitee list
        invitee.setInviteeUid(getInviteeUid(contact,event));
        invitee.setUserUid(contact.getContactUid());
//        invitee.setUserId(contact.getAliasName());
        // Changed by Qiushuo Huang
        invitee.setUserId(contact.getUserDetail().getUserId());
        invitee.setStatus("needsAction");
        invitee.setAliasPhoto(contact.getPhoto());
        invitee.setAliasName(contact.getName());
        invitee.setUserStatus(contact.getStatus());
        return invitee;
    }

    // str is email or phone
    public Invitee unactivatedInvitee(String str, Event event) {
        Invitee invitee = new Invitee();
        invitee.setUserStatus(Invitee.USER_STATUS_UNACTIVATED);
        invitee.setUserId(str);
        invitee.setInviteeUid(str);
        //please replace these two with right value
        invitee.setAliasName(str);
        invitee.setUserUid(str);

        return  invitee;
    }

    private String getInviteeUid(Contact contact,Event event){
        for (Invitee invitee : event.getInvitee()){
            if(invitee.getUserUid().equals(contact.getContactUid())){
                return invitee.getInviteeUid();
            }
        }
        return AppUtil.generateUuid();
    }
}
