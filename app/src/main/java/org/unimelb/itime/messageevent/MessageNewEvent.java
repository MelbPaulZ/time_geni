package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 3/09/2016.
 */
public class MessageNewEvent {
    public final Event event;

    public MessageNewEvent(Event event) {
        this.event = event;
    }
}
