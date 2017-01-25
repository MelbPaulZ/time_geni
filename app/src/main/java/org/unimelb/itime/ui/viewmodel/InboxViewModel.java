package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.SearchBar;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

import static org.unimelb.itime.ui.presenter.contact.ContextPresenter.getContext;


/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends CommonViewModel {
    private final static String TAG = "InboxViewModel";
    private MainInboxPresenter presenter;
    private DBManager dbManager;
    private List<ItemViewModel> list;
    private Context context;
    private boolean showSpinnerMenu = false;

    private ArrayList<String> menuItems = new ArrayList<>();
    private ItemView menuItemView = ItemView.of(BR.item, R.layout.listview_simple_menu_dropdown_item);

    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
        this.context = presenter.getContext();
        this.list = new ArrayList<>();
        this.dbManager = DBManager.getInstance(getContext());

        menuItems.add(context.getString(R.string.inbox_menu_mark_all_read));
        menuItems.add(context.getString(R.string.inbox_menu_clear_inbox));
    }

    /**
     * hide the spinner when outside area clicked
     * @return
     */
    public View.OnClickListener onOutsideClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowSpinnerMenu(false);
            }
        };
    }

    public AdapterView.OnItemClickListener onMenuSpinnerClicked() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setShowSpinnerMenu(false);
                if(dbManager.getAllMessages().isEmpty()){
                    return;
                }
                switch (i) {
                    case 0:
                        // mark all as read
                        presenter.markAsRead(dbManager.getAllMessages(), true);
                        break;
                    case 1:
                        // clear inbox
                        presenter.clearAll();
                        break;
                }

            }
        };
    }


    public AdapterView.OnItemClickListener onMessageItemClicked() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = list.get(i).getMessage();
                if (!message.getIsRead()) {
                    //unread to read
                    message.setRead(true);
                    message.update();
                    EventBus.getDefault().post(new MessageInboxMessage(new ArrayList<Message>()));
                    presenter.markAsRead(message);
                }

                // check the event before going to event detail page
                Event event = dbManager.getEvent(message.getEventUid());
                if (event == null) {
                    presenter.fetchEvent("-1", message.getEventUid());
                } else {
                    presenter.getView().toEventDetailPage(event);
                }
            }
        };
    }

    public SwipeMenuListView.OnMenuItemClickListener onMessageDeleteClicked() {
        return new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Message msg = DBManager.getInstance(getContext()).getAllMessages().get(position);
                msg.setDeleteLevel(1);
                msg.update();
                presenter.delete(msg);
                return false;
            }
        };
    }

    /**
     * search bar
     *
     * @return
     */
    public SearchBar.OnEditListener onSearching() {
        return new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                presenter.getFilter().filter(text);
            }
        };
    }

    public List<ItemViewModel> getList() {
        return list;
    }

    public void setList(List<ItemViewModel> list) {
        this.list = list;
    }

    @Bindable
    public ArrayList<String> getMenuItems() {
        return menuItems;
    }

    @Bindable
    public ItemView getMenuItemView() {
        return menuItemView;
    }

    @Bindable
    public boolean isShowSpinnerMenu() {
        return showSpinnerMenu;
    }

    public void setShowSpinnerMenu(boolean showSpinnerMenu) {
        this.showSpinnerMenu = showSpinnerMenu;
        notifyPropertyChanged(BR.showSpinnerMenu);
    }

    /**
     * the view model of each host
     */
    public static class ItemViewModel extends CommonViewModel {
        private MainInboxPresenter presenter;
        private String timeString = "";
        private Message message;
        private Context context;

        public ItemViewModel(MainInboxPresenter presenter, Message message) {
            this.presenter = presenter;
            this.context = presenter.getContext();
            setMessage(message);
        }

        @Bindable
        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
            setTimeString(message.getCreatedAt());
            notifyPropertyChanged(BR.message);
        }

        @Bindable
        public String getTimeString() {
            return timeString;
        }

        public void setTimeString(String timeString) {

            this.timeString = EventUtil.parseRelativeTime(presenter.getContext(), timeString);
            notifyPropertyChanged(BR.timeString);
        }
    }


}
