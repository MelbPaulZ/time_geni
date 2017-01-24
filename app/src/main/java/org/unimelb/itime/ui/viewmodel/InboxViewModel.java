package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.SearchBar;

import java.util.ArrayList;
import java.util.List;

import static org.unimelb.itime.ui.presenter.contact.ContextPresenter.getContext;


/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends CommonViewModel {
    private final static String TAG = "InboxViewModel";
    private MainInboxPresenter presenter;
    private DBManager dbManager;
    private List<ItemViewModel> list;


    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
        this.list = new ArrayList<>();
        this.dbManager = DBManager.getInstance(getContext());
    }


    public AdapterView.OnItemClickListener onItemClicked(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = list.get(i).getMessage();
                if (!message.getIsRead()){
                    //unread to read
                    message.setRead(true);
                    message.update();
                    presenter.markAsRead(message);
                }

                // check the event before going to event detail page
                Event event = dbManager.getEvent(message.getEventUid());
                if (event == null){
                    presenter.fetchEvent("-1", message.getEventUid());
                }else {
                    presenter.getView().toEventDetailPage(event);
                }
            }
        };
    }

    public SwipeMenuListView.OnMenuItemClickListener onDeleteClicked(){
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
     * @return
     */
    public SearchBar.OnEditListener onSearching(){
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


    /**
     * the view model of each host
     */
    public static class ItemViewModel extends CommonViewModel{
        private MainInboxPresenter presenter;
        private String timeString = "";
        private Message message;
        private Context context;

        public ItemViewModel(MainInboxPresenter presenter, Message message){
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
            setTimeString(message.getUpdatedAt());
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
