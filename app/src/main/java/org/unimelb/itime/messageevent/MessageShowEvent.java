package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 12/12/2016.
 */

public class MessageShowEvent {
    public final static int SHOW_EVENT = 100;
    public int task;
    public Event event;

    public MessageShowEvent(int task, Event event) {
        this.task = task;
        this.event = event;
    }
}
