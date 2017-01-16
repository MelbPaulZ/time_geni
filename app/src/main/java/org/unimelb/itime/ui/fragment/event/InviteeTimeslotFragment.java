package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.InviteeResponseAdapter;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentSelectedInviteeBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TimeslotMvpView;
import org.unimelb.itime.ui.presenter.TimeslotPresenter;
import org.unimelb.itime.ui.viewmodel.TimeslotInviteeResponseViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.List;

/**
 * Created by Paul on 4/09/2016.
 * show the response of each invitee to each timeslot
 */
public class InviteeTimeslotFragment extends BaseUiAuthFragment implements ItimeCommonMvpView {
    private FragmentSelectedInviteeBinding binding;
    private TimeslotInviteeResponseViewModel viewModel;
    private TimeslotPresenter<TimeslotMvpView> presenter;
    private  List<EventUtil.StatusKeyStruct> data;

    private Timeslot timeslot;
    private Event event;

    private View root;
    private long time;
    private ListView listView;
    private InviteeResponseAdapter adapter;
    private ToolbarViewModel toolbarViewModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_selected_invitee, container,false);
        root = binding.getRoot();
        listView = (ListView) root.findViewById(R.id.listView);
        adapter = new InviteeResponseAdapter(getContext());
        listView.setAdapter(adapter);
        return root;
    }

    @Override
    public MvpPresenter createPresenter() {
        if (presenter == null) {
            presenter = new TimeslotPresenter<>(getContext());
        }
        return presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new TimeslotInviteeResponseViewModel(presenter);
        binding.setVm(viewModel);

        toolbarViewModel= new ToolbarViewModel(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.invitee));
        binding.setToolbarVM(toolbarViewModel);
        initData();
    }



    private void initData(){
        this.adapter.setInvitees(data, event);
        this.adapter.notifyDataSetChanged();
        this.viewModel.setResponseTimeslot(this.timeslot);
    }

    public void setData(Event event, List<EventUtil.StatusKeyStruct> data, Timeslot timeslot){
        this.timeslot = timeslot;
        this.event = event;
        this.data = data;

    }


    public void setTime(long time){
        this.time = time;
    }


    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

    }
}
