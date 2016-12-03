package org.unimelb.itime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailGroupFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhaoliu on 3/12/2016.
 */

public class InviteeResponseAdapter extends BaseAdapter{
    List<EventDetailGroupFragment.StatusKeyStruct> responses = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;

    ArrayList<Invitee> invitees = new ArrayList<>();


    public InviteeResponseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setInvitees(List<EventDetailGroupFragment.StatusKeyStruct> responses){
        this.responses = responses;
        for (EventDetailGroupFragment.StatusKeyStruct structs: responses
             ) {
            this.invitees.addAll(structs.getInviteeList());
        }
    }

    @Override
    public int getCount() {
        return this.getItemCount();
    }

    private int getItemCount(){
        int count = 0;
        for(EventDetailGroupFragment.StatusKeyStruct struct: responses){
            count += struct.getInviteeList().size();
        }

        return this.invitees.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;
        root = layoutInflater.inflate(R.layout.listview_timeslot_response, parent, false);
        ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
        TextView txtView = (TextView) root.findViewById(R.id.text_view);

        Picasso.with(context).load(invitees.get(position).getPhoto()).into(imgView);
        txtView.setText(invitees.get(position).getName());

        return root;
    }
}
