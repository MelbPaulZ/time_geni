package org.unimelb.itime.messageevent;

/**
 * Created by Paul on 27/08/2016.
 */
public class MessageLocation {
    public String tag;
    public final String locationString;

    public MessageLocation(String tag,String locationString) {
        this.tag = tag;
        this.locationString = locationString;
    }
}
