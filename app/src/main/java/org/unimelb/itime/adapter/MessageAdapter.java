package org.unimelb.itime.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.ListviewInboxHostBinding;
import org.unimelb.itime.databinding.ListviewInboxInviteeBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.ui.viewmodel.InboxViewModel;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 1/12/16.
 */
public class MessageAdapter extends BaseAdapter implements Filterable {
    private List<Message> filteredMessageList;
    private MessageFilter messageFilter;
    private Context context;
    private ListviewInboxHostBinding inboxHostBinding;
    private ListviewInboxInviteeBinding inboxInviteeBinding;
    private final static int TYPE_INVITEE = 0;
    private final static int TYPE_HOST = 1;
    private MainInboxPresenter presenter;

    public MessageAdapter(Context context, MainInboxPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        this.filteredMessageList = new ArrayList<>();
    }

    public void setMessageList(List<Message> filteredMessageList) {
        this.filteredMessageList = filteredMessageList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return filteredMessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredMessageList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public int getViewTypeCount() {
        return 3;
    }

    /**
     * not reuse any convertView, because the view in each cell might be different
     */
    @Override
    public int getItemViewType(int position) {
        Message message = filteredMessageList.get(position);
        switch (message.getTemplate()){
            case Message.TPL_HOST_CONFIRMED:
                return 0;
            case Message.TPL_HOST_UNCONFIRMED:
                return 1;
            case Message.TPL_INVITEE:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        InboxViewModel viewModel = new InboxViewModel(presenter);
        Message message = filteredMessageList.get(position);
        if (convertView==null) {
            if (message.getTemplate().equals(Message.TPL_HOST_CONFIRMED)) {
                inboxHostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.listview_inbox_host, viewGroup, false);
                inboxHostBinding.setVm(viewModel);
                inboxHostBinding.setMessage(filteredMessageList.get(position));
                convertView = inboxHostBinding.getRoot();
            }else if (message.getTemplate().equals(Message.TPL_HOST_UNCONFIRMED)){
                inboxHostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.listview_inbox_host, viewGroup, false);
                inboxHostBinding.setVm(viewModel);
                inboxHostBinding.setMessage(filteredMessageList.get(position));
                convertView = inboxHostBinding.getRoot();
            }
            else {
                inboxInviteeBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.listview_inbox_invitee, viewGroup, false);
                inboxInviteeBinding.setVm(viewModel);
                inboxInviteeBinding.setMessage(filteredMessageList.get(position));
                convertView = inboxInviteeBinding.getRoot();
                //david added
                setImage(((ImageView)convertView.findViewById(R.id.inbox_avatar)));

            }
            convertView.setTag(viewModel);
        }else{
            viewModel = (InboxViewModel) convertView.getTag();
        }
        viewModel.setMessage(filteredMessageList.get(position));
        return convertView;
    }

    //david added
    public void setImage(ImageView view){
        Picasso.with(presenter.getContext()).load(org.unimelb.itime.vendor.R.drawable.invitee_selected_default_picture).transform(new CircleTransform()).into(view);
    }

    @Override
    public Filter getFilter() {
        if (messageFilter == null) {
            messageFilter = new MessageFilter();
        }
        return messageFilter;
    }

    private class MessageFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Message> matchList = new ArrayList<>();
                for (Message message : DBManager.getInstance().getAllMessages()) {
                    if (message.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        matchList.add(message);
                    }
                }

                filterResults.values = matchList;
                filterResults.count = matchList.size();
            } else {
                filterResults.values = DBManager.getInstance().getAllMessages();
                filterResults.count = DBManager.getInstance().getAllMessages().size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

//            viewModel.setMessages((List<Message>) results.values);
            filteredMessageList = (List<Message>) results.values;
            notifyDataSetChanged();
        }
    }
}
