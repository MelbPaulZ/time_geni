package org.unimelb.itime.adapter;

import android.content.Context;
import android.util.Log;
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
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailGroupFragment;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhaoliu on 3/12/2016.
 */

public class InviteeResponseAdapter extends BaseAdapter{
    private static final String TAG = "Response";
    List<EventDetailGroupFragment.StatusKeyStruct> responses = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;

    ArrayList<Invitee> accepted = new ArrayList<>();
    ArrayList<Invitee> rejected = new ArrayList<>();
    ArrayList<Invitee> pending = new ArrayList<>();

    public static final int ITEM = 0;
    public static final int SECTION_ACCEPTED = 1;
    public static final int SECTION_REJECTED = 2;
    public static final int SECTION_PENDING = 3;

    ArrayList<Invitee> invitees = new ArrayList<>();

    ArrayList<View> views = new ArrayList<>();

    public InviteeResponseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }



    public void setInvitees(List<EventDetailGroupFragment.StatusKeyStruct> responses){
        this.responses = responses;
        for (EventDetailGroupFragment.StatusKeyStruct structs: responses
             ) {
            if (structs.getStatus().equals("accepted")){
                this.accepted.addAll(structs.getInviteeList());
            }else if(structs.getStatus().equals("rejected")){
                this.rejected.addAll(structs.getInviteeList());
            }else if(structs.getStatus().equals("pending")){
                this.pending.addAll(structs.getInviteeList());
            }
        }
    }

    @Override
    public int getCount() {
        return this.getItemCount();
    }

    private int getItemCount(){
        int count = rejected.size() + accepted.size() + pending.size();
        return count + 3;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return SECTION_ACCEPTED;
        }else if(position == (accepted.size() + 1)){
            return SECTION_REJECTED;
        }else if(position == (accepted.size() + 1 + rejected.size() + 1)){
            return SECTION_PENDING;
        }else{
            return ITEM;
        }
    }

    private void initViews(){

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
        switch (getItemViewType(position)){
            case SECTION_ACCEPTED:{
                root = layoutInflater.inflate(R.layout.listview_timeslot_section, parent, false);
                TextView textView = (TextView) root.findViewById(R.id.section_title);
                textView.setText(accepted.size() + " accepted this slot");
                break;}
            case SECTION_REJECTED:{
                root = layoutInflater.inflate(R.layout.listview_timeslot_section, parent, false);
                TextView textView = (TextView) root.findViewById(R.id.section_title);
                textView.setText(rejected.size() + " rejected this slot");
                break;}
            case SECTION_PENDING:{
                root = layoutInflater.inflate(R.layout.listview_timeslot_section, parent, false);
                TextView textView = (TextView) root.findViewById(R.id.section_title);
                textView.setText(pending.size() + " not responded ye");
                break;}
            default:{
                root = layoutInflater.inflate(R.layout.listview_timeslot_response, parent, false);
                ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
                TextView txtView = (TextView) root.findViewById(R.id.text_view);
                Invitee invitee;
                if (position <= accepted.size()){
                    if (accepted.size() == 0){
                        return null;
                    }
                    invitee = accepted.get(position - 1);
                    Log.i(TAG, "getView: ");
                }else if(position >= accepted.size() + 1 && position <= accepted.size()+ 1 + rejected.size()){
                    Log.i(TAG, "getView: ");
                    if (rejected.size() == 0){
                        return null;
                    }
                    invitee = rejected.get(position - (accepted.size() + 1));
                }else {
                    Log.i(TAG, "getView: ");

                    if (pending.size() == 0){
                        return null;
                    }
                    invitee = pending.get(position + 1 - (accepted.size() + 1 + rejected.size() + 1));

                }

                Picasso.with(context).load(invitee.getPhoto()).into(imgView);
                txtView.setText(invitees.get(position).getName());
                break;}
        }

        return root;
    }
}
