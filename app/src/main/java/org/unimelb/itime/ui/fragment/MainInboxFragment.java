package org.unimelb.itime.ui.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventAdapter;
import org.unimelb.itime.adapter.MessageAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.FragmentMainInboxBinding;
import org.unimelb.itime.databinding.ListviewInboxHostBinding;
import org.unimelb.itime.databinding.ListviewInboxInviteeBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.ui.viewmodel.InboxViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.helper.Text2Drawable;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainInboxFragment extends BaseUiFragment<MainInboxMvpView, MainInboxPresenter> implements  MainInboxMvpView{

    private FragmentMainInboxBinding binding;
    private InboxViewModel inboxViewModel;
    private MainInboxPresenter presenter;
    private MessageApi msgApi;
    private MessageAdapter messageAdapter;
    private MainInboxFragment self;

    @Override
    public MainInboxPresenter createPresenter() {
        msgApi = HttpUtil.createService(getContext(), MessageApi.class);

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
        inboxViewModel = new InboxViewModel(presenter);
        binding.setVm(inboxViewModel);
        self = this;
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

        messageAdapter = new MessageAdapter(getContext(), null, inboxViewModel);
        binding.inboxListview.setAdapter(messageAdapter);

        binding.inboxListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = inboxViewModel.getMessages().get(i);
                message.setRead(true);
                messageAdapter.setMessageList(inboxViewModel.getMessages());
                Event event = EventManager.getInstance().findEventInEventList(message.getEventUid());
                EventUtil.startEditEventActivity(getContext(), self.getActivity(), event);
            }
        });

        binding.inboxListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Toast.makeText(getContext(), "OnCLick Delete",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void postMessageUpdate(Message msg){
        Observable<HttpResult<String>> observable = msgApi.read(msg.getEventUid(),msg.isRead() ? 1 : 0);
        Subscriber<HttpResult<String>> subscriber = new Subscriber<HttpResult<String>>(){

            public static final String TAG = "postMessageUpdate";

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<String> stringHttpResult) {
                Log.i(TAG, "onNext: ");

            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void setData(List<Message> messages){
        if (messageAdapter!=null){
            messageAdapter.setMessageList(messages);
        }
        inboxViewModel.setData(messages);
    }
}
