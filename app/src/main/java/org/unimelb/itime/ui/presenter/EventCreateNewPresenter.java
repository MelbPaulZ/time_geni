package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewPresenter extends MvpBasePresenter<EventCreateNewMvpView>{

    private Context context;
    public EventCreateNewPresenter(Context context){
        this.context = context;
    }

    public void submit(Event event){


    }

    public void pickDate(){
        EventCreateNewMvpView view = getView();
        if (view!=null)
            view.pickDate();
    }


    public Context getContext() {
        return context;
    }

}
