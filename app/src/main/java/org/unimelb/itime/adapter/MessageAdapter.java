package org.unimelb.itime.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.ListviewInboxHostBinding;
import org.unimelb.itime.databinding.ListviewInboxInviteeBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.viewmodel.InboxViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 1/12/16.
 */
public class MessageAdapter extends BaseAdapter {
    private List<Message> messageList;
    private Context context;
    private ListviewInboxHostBinding inboxHostBinding;
    private ListviewInboxInviteeBinding inboxInviteeBinding;
    private InboxViewModel viewModel;
    private final static int TYPE_INVITEE = 0;
    private final static int TYPE_HOST = 1;

    public MessageAdapter(Context context, List<Message> messageList, InboxViewModel inboxViewModel) {
        this.context = context;
        if (this.messageList == null) {
            this.messageList = new ArrayList<>();
        } else {
            this.messageList = messageList;
        }
        this.viewModel = inboxViewModel;
//        this.viewModel = new InboxViewModel();
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
        viewModel.setMessages(messageList);
    }


    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
        if (EventUtil.isUserHostOfEvent(event)) {
            return TYPE_HOST;
        } else {
            return TYPE_INVITEE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (getItemViewType(position) == TYPE_HOST) {
            inboxHostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.listview_inbox_host, viewGroup, false);
            inboxHostBinding.setVm(viewModel);
            inboxHostBinding.setPosition(position);
            return inboxHostBinding.getRoot();
        } else {
            inboxInviteeBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.listview_inbox_invitee, viewGroup, false);
            inboxInviteeBinding.setVm(viewModel);
            inboxInviteeBinding.setPosition(position);
            return inboxInviteeBinding.getRoot();
        }

    }


}
