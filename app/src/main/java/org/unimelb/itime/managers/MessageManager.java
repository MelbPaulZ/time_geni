package org.unimelb.itime.managers;

/**
 * Created by yuhaoliu on 1/12/16.
 */
public class MessageManager {
    private static MessageManager ourInstance = new MessageManager();

    public static MessageManager getInstance() {
        return ourInstance;
    }

    private MessageManager() {
    }


}
