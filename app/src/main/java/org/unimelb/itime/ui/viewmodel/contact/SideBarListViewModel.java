package org.unimelb.itime.ui.viewmodel.contact;

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

    @Bindable
    public ObservableList getItems() {
        return items;
    }

    public void setItems(ObservableList items) {
        this.items = items;
    }

    @Bindable
    public ItemView getItemView() {
        return itemView;
    }

    public void setItemView(ItemView itemView) {
        this.itemView = itemView;
    }
}
