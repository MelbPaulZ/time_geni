package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditPresenter extends MvpBasePresenter<EventEditMvpView> {
    private Context context;

    public EventEditPresenter(Context context) {
        this.context = context;
    }

    public void toHostEventDetail(Event event){
        EventEditMvpView view = getView();
        if (view != null){
            view.toHostEventDetail(event);
        }
    }

    public void changeLocation(){
        EventEditMvpView view = getView();
        if (view != null){
            view.changeLocation();
        }
    }

    public void toTimeSlotView(String tag){
        EventEditMvpView view= getView();
        if (view != null){
            view.toTimeSlotView(tag);
        }
    }

    public void toInviteePicker(String tag){
        EventEditMvpView view= getView();
        if (view != null){
            view.toInviteePicker(tag);
        }
    }

    public void toPhotoPicker(String tag){
        EventEditMvpView view= getView();
        if (view != null){
            view.toPhotoPicker(tag);
        }
    }

    public void toSoloEventDetail(){
        EventEditMvpView view= getView();
        if (view != null){
            view.toSoloEventDetail();
        }
    }

    public void toSoloEventDetail(Event event){
        EventEditMvpView view= getView();
        if (view != null){
            view.toSoloEventDetail(event);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
