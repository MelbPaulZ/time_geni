package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class EventPresenter<V extends TaskBasedMvpView<List<Event>>> extends MvpBasePresenter<V> {

    private Context context;
    private EventApi eventApi;

    public EventPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
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
