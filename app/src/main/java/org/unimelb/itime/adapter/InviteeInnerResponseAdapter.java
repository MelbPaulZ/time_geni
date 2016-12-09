package org.unimelb.itime.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailGroupFragment;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 3/12/2016.
 */

public class InviteeInnerResponseAdapter extends BaseAdapter{
    private static final String TAG = "Response";
    List<EventUtil.StatusKeyStruct> responses = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;

    ArrayList<Invitee> accepted = new ArrayList<>();
    ArrayList<Invitee> rejected = new ArrayList<>();
    ArrayList<Invitee> pending = new ArrayList<>();

    ArrayList<View> views = new ArrayList<>();

    public InviteeInnerResponseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setInvitees(List<EventUtil.StatusKeyStruct> responses, Event event){
        this.responses = responses;
        this.accepted = new ArrayList<>();
        this.rejected = new ArrayList<>();
        this.pending = new ArrayList<>();
        for (EventUtil.StatusKeyStruct structs: responses
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

        View rootA = layoutInflater.inflate(R.layout.listview_timeslot_inner_section, null);
        TextView textViewA = (TextView) rootA.findViewById(R.id.section_title);
        textViewA.setText("Accepted");
        textViewA.setTextColor(Color.BLACK);
        this.views.add(rootA);

        for (Invitee invitee:accepted
             ) {
            View root = layoutInflater.inflate(R.layout.listview_timeslot_inner_response, null);
            ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
            imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_event_selected_right));

            TextView txtView = (TextView) root.findViewById(R.id.text_view);
            txtView.setText(invitee.getName());
            this.views.add(root);
        }

        View rootR = layoutInflater.inflate(R.layout.listview_timeslot_inner_section, null);
        TextView textViewR = (TextView) rootR.findViewById(R.id.section_title);
        textViewR.setText("Rejected");
        textViewR.setTextColor(Color.BLACK);
        this.views.add(rootR);

        for (Invitee invitee:rejected
                ) {
            View root = layoutInflater.inflate(R.layout.listview_timeslot_inner_response, null);
            ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
            imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_event_status_rejected));

            TextView txtView = (TextView) root.findViewById(R.id.text_view);
            txtView.setText(invitee.getName() + ifHost(event, invitee));
            this.views.add(root);
        }

        View rootP = layoutInflater.inflate(R.layout.listview_timeslot_inner_section, null);
        TextView textViewP = (TextView) rootP.findViewById(R.id.section_title);
        textViewP.setText("No Response");
        textViewP.setTextColor(Color.BLACK);
        this.views.add(rootP);

        for (Invitee invitee:pending
                ) {
            View root = layoutInflater.inflate(R.layout.listview_timeslot_inner_response, null);
            ImageView imgView = (ImageView) root.findViewById(R.id.img_view);
            imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_event_status_pending_small));

            TextView txtView = (TextView) root.findViewById(R.id.text_view);
            txtView.setText(invitee.getName() + ifHost(event, invitee));
            this.views.add(root);
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
