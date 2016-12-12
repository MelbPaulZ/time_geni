package org.unimelb.itime.managers;

import android.util.Log;

import org.unimelb.itime.bean.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by yuhaoliu on 1/12/16.
 */
public class MessageManager {
    private static final String TAG = "MessageManager";
    private static MessageManager ourInstance = new MessageManager();

    private List<Message> deletedWaitingList = Collections.synchronizedList(new ArrayList<Message>());

    public static MessageManager getInstance() {
        return ourInstance;
    }

    private MessageManager() {
    }

    public void insertWaitMessage(Message message){
        this.deletedWaitingList.add(message);

    }

    public void deleteWaitMessage(Message message){
        if (this.deletedWaitingList.contains(message)){
            this.deletedWaitingList.remove(message);
        }else{
            Log.i(TAG, "deleteWaitMessage: " + "Can't find correspond msg");
        }
    }

    public boolean isInWaitList(String msgUID){
        for (Message msg:this.deletedWaitingList
             ) {
            if (msg.getMessageUid().equals(msgUID)){
                return true;
            }
        }

        return false;
    }

}
