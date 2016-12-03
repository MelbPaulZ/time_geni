package org.unimelb.itime.ui.fragment.eventdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.InviteeResponseAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentSelectedInviteeBinding;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.ui.presenter.TimeslotInviteeResponsePresenter;
import org.unimelb.itime.ui.viewmodel.InboxViewModel;
import org.unimelb.itime.ui.viewmodel.TimeslotInviteeResponseViewModel;
import org.unimelb.itime.widget.NonScrollListView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 4/09/2016.
 */
public class InviteeTimeslotFragment extends BaseUiFragment {
    private FragmentSelectedInviteeBinding binding;
    private TimeslotInviteeResponseViewModel viewModel;
    private TimeslotInviteeResponsePresenter presenter;

    private Timeslot timeslot;
    private Event event;

    private View root;
    private InviteeTimeslotFragment self;
    private long time;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_selected_invitee, container,false);
        self = this;
        root = binding.getRoot();
        listView = (ListView) root.findViewById(R.id.listView);
        listView.setAdapter(new InviteeResponseAdapter(getContext()));

        return root;
    }

    @Override
    public MvpPresenter createPresenter() {
        if (presenter == null) {
            presenter = new TimeslotInviteeResponsePresenter(getContext());
        }
        return presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new TimeslotInviteeResponseViewModel(presenter);
        binding.setVm(viewModel);
        initListener();
        initData();
    }

    public void setData(List<EventDetailGroupFragment.StatusKeyStruct> data, Timeslot timeslot){
//        this.event = event;
        this.timeslot = timeslot;
        ((InviteeResponseAdapter)this.listView.getAdapter()).setInvitees(data);
        this.viewModel.setResponseTimeslot(this.timeslot);

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
//        NonScrollListView listView1 = (NonScrollListView) root.findViewById(R.id.invitee_accept_listview);
//        ArrayList<String> names = new ArrayList<>();
//        names.add("Chi");
//        names.add("hoho");
//        names.add("asdad");
//        names.add("Zed");
//        names.add("Quinn");
//        names.add("asdad");
//        listView1.setAdapter(new ArrayAdapter<String>(this.getContext(),R.layout.listview_accept, R.id.accept_listview_name,names));
//
//        NonScrollListView listView2 = (NonScrollListView)root.findViewById(R.id.invitee_reject_listview);
//        ArrayList<String> rejectors = new ArrayList<>();
//        rejectors.add("David");
//        rejectors.add("Tim");
//        rejectors.add("Jackson");
//        listView2.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.listview_reject, R.id.accept_reject_name, rejectors));
//
//        NonScrollListView listView3 = (NonScrollListView) root.findViewById(R.id.not_respond_listview);
//        ArrayList<String> notResponders = new ArrayList<>();
//        notResponders.add("Jack");
//        notResponders.add("Lucy");
//        listView3.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.listview_not_response, R.id.not_respond_listview_name, notResponders));
    }

    public void setTime(long time){
        this.time = time;
    }



//    public class UidKeySturct{
//        String slotUid;
//        //slotUid is key
//        ArrayList<StatusKeyStruct> response = new ArrayList<>();
//
//        public UidKeySturct(String slotUid) {
//            this.slotUid = slotUid;
//        }
//
//        public void addInvitee(staa){
//
//        }

//    public void add

//    }



}
