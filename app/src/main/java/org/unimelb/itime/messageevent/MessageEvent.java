package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 25/08/2016.
 */
public class MessageEvent {
    public final static int INIT_DB = 1;
    public String message;
    public int task;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int task){
        this.task = task;
    }
}
