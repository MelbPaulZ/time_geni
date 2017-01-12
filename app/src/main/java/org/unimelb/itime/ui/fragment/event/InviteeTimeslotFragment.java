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
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentSelectedInviteeBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TimeslotCommonMvpView;
import org.unimelb.itime.ui.presenter.TimeslotCommonPresenter;
import org.unimelb.itime.ui.viewmodel.TimeslotInviteeResponseViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.List;

/**
 * Created by Paul on 4/09/2016.
 * show the response of each invitee to each timeslot
 */
public class InviteeTimeslotFragment extends EventBaseFragment implements ItimeCommonMvpView {
    private FragmentSelectedInviteeBinding binding;
    private TimeslotInviteeResponseViewModel viewModel;
    private TimeslotCommonPresenter<TimeslotCommonMvpView> presenter;

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
            presenter = new TimeslotCommonPresenter<>(getContext());
        }
        return presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new TimeslotInviteeResponseViewModel(presenter);
        binding.setVm(viewModel);
        binding.setToolbarVM(toolbarViewModel);
        initListener();
    }

    public void setData(Event event, List<EventUtil.StatusKeyStruct> data, Timeslot timeslot){
        this.timeslot = timeslot;
        this.adapter.setInvitees(data, event);
        this.adapter.notifyDataSetChanged();
        this.viewModel.setResponseTimeslot(this.timeslot);

    }

    // TODO: 11/1/17 implement after change all toolbar
    public void initListener(){
//        LinearLayout backBtn = (LinearLayout) root.findViewById(R.id.back);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                closeFragment(self, getFrom());
//            }
//        });
    }

    public void setTime(long time){
        this.time = time;
    }

    @Override
    public void setData(Object o) {

    }

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftTitleStr(getString(R.string.back));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.invitee));
    }

    @Override
    public void setRightTitleStringToVM() {
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
    }

    @Override
    public ToolbarViewModel<? extends ItimeCommonMvpView> getToolbarViewModel() {
        return new ToolbarViewModel<>(this);
    }

    @Override
    public void onTaskStart(int task) {

    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {

    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {

    }

    @Override
    public void onBack() {
        closeFragment(self, getFrom());
    }

    @Override
    public void onNext() {

    }
}
