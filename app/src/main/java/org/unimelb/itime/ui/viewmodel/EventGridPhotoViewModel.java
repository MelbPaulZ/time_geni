package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.GridView;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.PhotoAdapter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import com.android.databinding.library.baseAdapters.BR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 15/1/17.
 */

public class EventGridPhotoViewModel extends BaseObservable {
    private Event event;


    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }


    // set grid view data binding
    @BindingAdapter("app:event")
    public static void setGradView(GridView gridView, Event event){

//        List<String> urls = new ArrayList<>();
        if (event!=null) {
//            for (PhotoUrl photoUrl : event.getPhoto()) {
//                File file = new File(photoUrl.getLocalPath());
//                if (file.exists()){
//                    urls.add(photoUrl.getUrl());
//                }
//            }
            PhotoAdapter adapter = new PhotoAdapter(gridView.getContext(), R.id.gridview_photo, event.getPhoto());
            gridView.setAdapter(adapter);
        }
    }
}
