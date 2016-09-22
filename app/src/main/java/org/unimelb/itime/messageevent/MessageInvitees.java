package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.Invitee;

import java.util.ArrayList;

/**
 * Created by Paul on 21/09/2016.
 */
public class MessageInvitees {
    public String tag;
    public ArrayList<Invitee> invitees;

    public MessageInvitees(String tag, ArrayList<Invitee> invitees) {
        this.tag = tag;
        this.invitees = invitees;
    }
}
