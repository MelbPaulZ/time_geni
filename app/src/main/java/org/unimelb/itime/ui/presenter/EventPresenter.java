package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class EventPresenter<V extends TaskBasedMvpView<List<Event>>> extends MvpBasePresenter<V> {

    private Context context;

    public EventPresenter(Context context){
        this.context = context;
    }

    public Context getContext(){
        return this.context;
    }




    public void updateEvent(Event event, String type, long originalStartTime){

    }


    public void insertEvent(Event event){

    }

    public void deleteEvent(Event event, String type, long orgStartTime) {

    }
}
