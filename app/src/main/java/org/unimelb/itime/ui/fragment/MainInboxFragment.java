package org.unimelb.itime.ui.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.adapter.MessageAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.FragmentMainInboxBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.calendars.CalendarBaseViewFragment;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.AppUtil;

import java.util.List;

/**
 * required signIn, need to extend BaseUiAuthFragment
 */
public class MainInboxFragment extends BaseUiFragment<Object, MainInboxMvpView, MainInboxPresenter> implements  MainInboxMvpView, SearchView.OnQueryTextListener{

    private FragmentMainInboxBinding binding;
    private MainInboxPresenter presenter;
    private MessageAdapter messageAdapter;

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
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF4,0x5B,0x69)));
                // set item width
                deleteItem.setWidth(167);

                // set a icon
                deleteItem.setIcon(R.drawable.icon_event_trash);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        binding.inboxListview.setMenuCreator(creator);
        binding.inboxListview.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        messageAdapter = new MessageAdapter(getContext(),presenter);
        binding.inboxListview.setAdapter(messageAdapter);

        binding.inboxListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = (Message) messageAdapter.getItem(i);
                if (!message.getIsRead()){
                    //unread to read
                    message.setRead(true);
                    message.update();
                    presenter.updateMessage(message);
                    // try copy message list and reset
                    messageAdapter.notifyDataSetChanged();

                }else{
                    //read to read
                }

//                Event event = EventManager.getInstance(getContext()).findEventByUid(message.getEventUid());
                Event event = DBManager.getInstance(getContext()).getEvent(message.getEventUid());
                if (event==null){
                    presenter.fetchEvent(String.valueOf(-1),message.getEventUid());
                    Toast.makeText(getContext(), "cannot find event, please try later", Toast.LENGTH_SHORT).show();

                }else {
                    intentToEventDetail(event);
                }
            }
        });

        binding.inboxListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Toast.makeText(getContext(), "Delete Message",Toast.LENGTH_SHORT).show();
                Message msg = DBManager.getInstance(getContext()).getAllMessages().get(position);
                // TODO: 8/12/2016 message delete ask chuandong about server
                presenter.deleteMessage(msg);
                msg.delete();
                EventBus.getDefault().post(new MessageInboxMessage(DBManager.getInstance(getContext()).getAllMessages()));
                messageAdapter.setMessageList(DBManager.getInstance(getContext()).getAllMessages());
                return false;
            }
        });
        initSearch();
    }

    private void intentToEventDetail(Event event){
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("event_uid", event.getEventUid());
        intent.putExtra("start_time", event.getStartTime());
        EventManager.getInstance(getContext()).setCurrentEvent(event);
        getActivity().startActivityForResult(intent, CalendarBaseViewFragment.REQ_EVENT_DETAIL);
    }

    private void initSearch(){
        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) binding.getRoot().findViewById(R.id.message_searchview);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getInboxMessage(MessageInboxMessage messageInboxMessage){
        List<Message> messageList = DBManager.getInstance(getContext()).getAllMessages();
        messageAdapter.setMessageList(messageList);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        List<Message> messageList = DBManager.getInstance(getContext()).getAllMessages();
        messageAdapter.setMessageList(messageList);
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String constrains) {
        messageAdapter.getFilter().filter(constrains);
        return false;
    }

    @Override
    public void setData(Object o) {

    }

    @Override
    public void onTaskStart(int taskId) {
        AppUtil.showProgressBar(getActivity(),"","Please wait...");
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        AppUtil.hideProgressBar();
        if (!(data instanceof List) && (((List)data).size()) == 0){
            return;
        }
        List<? extends Object> list = (List) data;
        switch (taskId){
            case MainInboxPresenter.TASK_EVENT_GET:{
                if (list.get(0) instanceof Event){
                    intentToEventDetail((Event) ((List) data).get(0));
                }
                break;
            }

        }
    }

    @Override
    public void onTaskError(int taskId) {
        AppUtil.hideProgressBar();
    }
}
