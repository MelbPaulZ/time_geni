package org.unimelb.itime.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.MessageAdapter;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.FragmentMainInboxBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.fragment.calendars.CalendarBaseViewFragment;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.ui.viewmodel.InboxViewModel;
import org.unimelb.itime.ui.viewmodel.InboxViewModel.ItemViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * required signIn, need to extend BaseUiAuthFragment
 */
public class MainInboxFragment extends BaseUiAuthFragment<MainInboxMvpView, MainInboxPresenter> implements  MainInboxMvpView {

    private FragmentMainInboxBinding binding;
    private MainInboxPresenter presenter;
    private MessageAdapter adapter;

    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private InboxViewModel contentViewModel;

    private List<ItemViewModel> list;
    private DBManager dbManager;

    public MainInboxFragment(){
        list = new ArrayList<>();
    }

    @Override
    public MainInboxPresenter createPresenter() {
        if (presenter == null) {
            presenter = new MainInboxPresenter(getContext());
        }

        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_inbox, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbManager = DBManager.getInstance(getContext());
        adapter = new MessageAdapter(getContext(), presenter);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitleStr(getString(R.string.inbox));
        toolbarViewModel.setRightDrawable(getResources().getDrawable(R.drawable.icon_three_lines));

        contentViewModel = new InboxViewModel(getPresenter());
        list = transform(dbManager.getAllMessages());
        adapter.setData(list);
        contentViewModel.setList(list);

        binding.setToolbarVM(toolbarViewModel);
        binding.setContentVM(contentViewModel);

        initView();
    }

    /**
     * transform from message list to messageVM list
     * @param msgList
     * @return
     */
    private List<ItemViewModel> transform(List<Message> msgList){
        List<ItemViewModel> vmList = new ArrayList<>();
        for(Message msg : msgList){
            ItemViewModel itemVM = new ItemViewModel(getPresenter(), msg);
            vmList.add(itemVM);
        }
        return vmList;
    }

    private void initView(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF4,0x5B,0x69)));
                deleteItem.setWidth(167);
                deleteItem.setIcon(R.drawable.icon_event_trash);
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        binding.inboxListview.setMenuCreator(creator);
        binding.inboxListview.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        binding.inboxListview.setAdapter(adapter);
    }

    public void setData(List<Message> data){
        this.list = transform(data);
        contentViewModel.setList(list);
        adapter.setData(list);
    }

    @Override
    public void onFilterMessage(List<Message> filterList) {
        this.list = transform(filterList);
        contentViewModel.setList(list);
        adapter.setData(list);
    }

    @Override
    public void toEventDetailPage(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("event_uid", event.getEventUid());
        intent.putExtra("start_time", event.getStartTime());
        EventManager.getInstance(getContext()).setCurrentEvent(event);
        getActivity().startActivityForResult(intent, CalendarBaseViewFragment.REQ_EVENT_DETAIL);
    }


    @Override
    public void onTaskStart(int taskId) {
        if(taskId == MainInboxPresenter.TASK_EVENT_GET){
            showProgressDialog();
        }
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        if(taskId == MainInboxPresenter.TASK_EVENT_GET){
            Event event = (Event) data;
            toEventDetailPage(event);
            return;
        }

        if(taskId == MainInboxPresenter.TASK_MSG_DELETE || taskId == MainInboxPresenter.TASK_MSG_READ){
            List<ItemViewModel> messages = transform(dbManager.getAllMessages());
            contentViewModel.setList(messages);
            adapter.setData(messages);
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        showDialog("Error", (String) data);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }
}
