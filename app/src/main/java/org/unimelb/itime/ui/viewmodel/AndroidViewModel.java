package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Paul on 18/10/16.
 */
public class AndroidViewModel extends BaseObservable {


    @BindingAdapter("android:onFocusChange")
    public static void setOnFocusChangeListener(View view, View.OnFocusChangeListener onFocusChangeListener){
        view.setOnFocusChangeListener(onFocusChangeListener);
    }

    @BindingAdapter("android:onListItemClick")
    public static void setOnListItemClickListener(View view, AdapterView.OnItemClickListener listener){
        ((ListView)view).setOnItemClickListener(listener);
    }

}
