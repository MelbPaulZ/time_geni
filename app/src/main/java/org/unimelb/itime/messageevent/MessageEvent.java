package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 25/08/2016.
 */
public class MessageEvent {
    public final static int RELOAD_EVENT = 1;
    public final static int LOGOUT = 2;
    public final static int LOGOUTWITHDB = 3;
    public String message;
    public int task;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int task){
        this.task = task;
    }
}
