package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 25/08/2016.
 */
public class MessageEventTime {
    public String tag;
    public int hour;
    public int minute;

    public MessageEventTime(String tag ,int hour, int minute) {
        this.tag = tag;
        this.hour = hour;
        this.minute = minute;
    }
}
