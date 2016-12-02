package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.Message;

import java.util.List;

/**
 * Created by yuhaoliu on 1/12/16.
 */
public class MessageInboxMessage {
    public List<Message> messages;

    public MessageInboxMessage(List<Message> messages) {
        this.messages = messages;
    }
}
