package org.unimelb.itime.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.squareup.picasso.Transformation;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailGroupFragment;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.LoadImgHelper;

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

    ArrayList<View> views = new ArrayList<>();

    public InviteeResponseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setInvitees(List<EventDetailGroupFragment.StatusKeyStruct> responses, Event event){
        this.responses = responses;
        this.accepted = new ArrayList<>();
        this.rejected = new ArrayList<>();
        this.pending = new ArrayList<>();
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

        this.initViews(event);
    }

    @Override
    public int getCount() {
        return this.getItemCount();
    }

    private int getItemCount(){
        return this.views.size();
    }

    private void initViews(Event event){
        this.views.clear();

        View rootA = layoutInflater.inflate(R.layout.listview_timeslot_section, null);
        TextView textViewA = (TextView) rootA.findViewById(R.id.section_title);
        textViewA.setText(accepted.size() + " accepted this slot");
        textViewA.setTextColor(Color.BLACK);

        this.views.add(rootA);

        for (Invitee invitee:accepted
             ) {
            View root = layoutInflater.inflate(R.layout.listview_timeslot_response, null);
            ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
            TextView txtView = (TextView) root.findViewById(R.id.text_view);
            bindUrlHelper(context, invitee.getPhoto(), imgView, new CircleTransform());
            txtView.setText(invitee.getName() + ifHost(event, invitee));
            this.views.add(root);
        }

        View rootR = layoutInflater.inflate(R.layout.listview_timeslot_section, null);
        TextView textViewR = (TextView) rootR.findViewById(R.id.section_title);
        textViewR.setText(rejected.size() + " rejected this slot");
        textViewR.setTextColor(Color.BLACK);
        this.views.add(rootR);

        for (Invitee invitee:rejected
                ) {
            View root = layoutInflater.inflate(R.layout.listview_timeslot_response, null);
            ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
            TextView txtView = (TextView) root.findViewById(R.id.text_view);
            bindUrlHelper(context, invitee.getPhoto(), imgView, new CircleTransform());
            txtView.setText(invitee.getName() + ifHost(event, invitee));
            this.views.add(root);
        }

        View rootP = layoutInflater.inflate(R.layout.listview_timeslot_section, null);
        TextView textViewP = (TextView) rootP.findViewById(R.id.section_title);
        textViewP.setText(pending.size() + " not responded yet");
        textViewP.setTextColor(Color.BLACK);
        this.views.add(rootP);

        for (Invitee invitee:pending
                ) {
            View root = layoutInflater.inflate(R.layout.listview_timeslot_response, null);
            ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
            TextView txtView = (TextView) root.findViewById(R.id.text_view);
            bindUrlHelper(context, invitee.getPhoto(), imgView, new CircleTransform());
            txtView.setText(invitee.getName() + ifHost(event, invitee));
            this.views.add(root);
        }

    }

    private <T extends Transformation> void bindUrlHelper(Context context, String url, ImageView view, T transformer){
        if (url != null && !url.equals("")){
            Picasso.with(context).load(url).transform(transformer).into(view);
        }else {
            Picasso.with(context).load(org.unimelb.itime.vendor.R.drawable.invitee_selected_default_picture).transform(transformer).into(view);
        }
    }

    private String ifHost(Event event, Invitee invitee){
        if (event.getUserUid().equals(invitee.getUserUid())){
            return "(Host)";
        }
        return "";
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
        return this.views.get(position);
    }
}
