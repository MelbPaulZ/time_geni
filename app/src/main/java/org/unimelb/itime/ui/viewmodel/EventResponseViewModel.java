package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.EventResponse;
import org.unimelb.itime.ui.presenter.EventResponsePresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventResponseViewModel extends CommonViewModel{

    private EventResponsePresenter presenter;

    private List<EventResponse> itemList;
    private ItemView itemView = ItemView.of(BR.item, R.layout.listview_event_response);


    public EventResponseViewModel(EventResponsePresenter presenter){
        this.presenter = presenter;
        this.itemList = new ArrayList<>();
    }

    public AdapterView.OnItemClickListener onImageClicked(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(presenter.getView() != null){
                    String userUid = itemList.get(i).getUserUid();
                    presenter.getView().toViewProfilePage(userUid);
                }
            }
        };
    }

    @Bindable
    public List<EventResponse> getItemList() {
        return itemList;
    }

    public void setItemList(List<EventResponse> itemList) {
        this.itemList = itemList;
        notifyPropertyChanged(BR.itemList);
    }

    @Bindable
    public ItemView getItemView() {
        return itemView;
    }

    public void setItemView(ItemView itemView) {
        this.itemView = itemView;
        notifyPropertyChanged(BR.itemView);
    }


}
