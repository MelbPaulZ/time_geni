package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 25/08/2016.
 */
public class MessageEventDate {
    public final String tag;
    public final int year;
    public final int month;
    public final int day;

    public MessageEventDate(String tag, int year, int month, int day) {
        this.tag = tag;
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
