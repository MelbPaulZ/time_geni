package org.unimelb.itime.ui.fragment;


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
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentMainInboxBinding;
import org.unimelb.itime.databinding.InboxHostBinding;
import org.unimelb.itime.databinding.InboxInviteeBinding;
import org.unimelb.itime.ui.mvpview.MainInboxMvpView;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.ui.viewmodel.InboxViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.helper.Text2Drawable;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainInboxFragment extends BaseUiFragment<MainInboxMvpView, MainInboxPresenter> implements  MainInboxMvpView{

    private FragmentMainInboxBinding binding;
    private InboxHostBinding inboxHostBinding;
    private InboxInviteeBinding inboxInviteeBinding;
    private InboxViewModel inboxViewModel;
    private MainInboxPresenter presenter;

    @Override
    public MainInboxPresenter createPresenter() {
        if (presenter == null) {
            presenter = new MainInboxPresenter();
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


        SwipeMenuListView listView = (SwipeMenuListView) binding.getRoot().findViewById(R.id.inbox_listView);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(90);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.icon_event_location);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);



    }
}
