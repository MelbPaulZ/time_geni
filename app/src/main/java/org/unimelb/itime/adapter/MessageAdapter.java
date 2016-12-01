package org.unimelb.itime.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.unimelb.itime.bean.Message;

import java.util.List;

/**
 * Created by Paul on 1/12/16.
 */
public class MessageAdapter extends BaseAdapter {
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }


    @Override
    public int getCount() {
        if (messageList== null)
             return 0;
        else
            return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
