package org.unimelb.itime.ui.fragment.eventdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.ui.presenter.EmptyPresenter;
import org.unimelb.itime.widget.NonScrollListView;

import java.util.ArrayList;

/**
 * Created by Paul on 4/09/2016.
 */
public class InviteeTimeslotFragment extends BaseUiFragment {

    private View root;
    private InviteeTimeslotFragment self;
    private long time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_selected_invitee, container,false);
        self = this;
        return root;
    }

    @Override
    public MvpPresenter createPresenter() {
        return new EmptyPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();

    }

    public void initListener(){
        TextView backBtn = (TextView) root.findViewById(R.id.invitee_view_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(self, (EventDetailGroupFragment)getFrom());
            }
        });
    }

    public void initData(){
        NonScrollListView listView1 = (NonScrollListView) root.findViewById(R.id.invitee_accept_listview);
        ArrayList<String> names = new ArrayList<>();
        names.add("Chi");
        names.add("hoho");
        names.add("asdad");
        names.add("Zed");
        names.add("Quinn");
        names.add("asdad");
        listView1.setAdapter(new ArrayAdapter<String>(this.getContext(),R.layout.listview_accept, R.id.accept_listview_name,names));

        NonScrollListView listView2 = (NonScrollListView)root.findViewById(R.id.invitee_reject_listview);
        ArrayList<String> rejectors = new ArrayList<>();
        rejectors.add("David");
        rejectors.add("Tim");
        rejectors.add("Jackson");
        listView2.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.listview_reject, R.id.accept_reject_name, rejectors));

        NonScrollListView listView3 = (NonScrollListView) root.findViewById(R.id.not_respond_listview);
        ArrayList<String> notResponders = new ArrayList<>();
        notResponders.add("Jack");
        notResponders.add("Lucy");
        listView3.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.listview_not_response, R.id.not_respond_listview_name, notResponders));
    }

    public void setTime(long time){
        this.time = time;
    }




}
