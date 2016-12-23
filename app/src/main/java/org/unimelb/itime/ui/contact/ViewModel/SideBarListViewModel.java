package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableList;
import com.android.databinding.library.baseAdapters.BR;
import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class SideBarListViewModel extends BaseObservable {
    private ObservableList items;
    private ItemView itemView;

    public SideBarListViewModel(){
    }

    @Bindable
    public ObservableList getItems() {
        return items;
    }

    public void setItems(ObservableList items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
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
