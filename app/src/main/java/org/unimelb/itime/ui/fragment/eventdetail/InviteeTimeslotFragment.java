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
import android.widget.LinearLayout;
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
import org.unimelb.itime.util.EventUtil;
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
    private InviteeResponseAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_selected_invitee, container,false);
        self = this;
        root = binding.getRoot();
        listView = (ListView) root.findViewById(R.id.listView);
        adapter = new InviteeResponseAdapter(getContext());
        listView.setAdapter(adapter);

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
    }

    public void setData(Event event, List<EventUtil.StatusKeyStruct> data, Timeslot timeslot){
//        this.event = event;
        this.timeslot = timeslot;
        this.adapter.setInvitees(data, event);
        this.adapter.notifyDataSetChanged();
        this.viewModel.setResponseTimeslot(this.timeslot);

    }

    public void initListener(){
        LinearLayout backBtn = (LinearLayout) root.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(self, (EventDetailGroupFragment)getFrom());
            }
        });
    }

    public void setTime(long time){
        this.time = time;
    }
}
