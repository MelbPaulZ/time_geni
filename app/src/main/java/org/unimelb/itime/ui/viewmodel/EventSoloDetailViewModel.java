package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventDetailSoloPresenter;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventSoloDetailViewModel extends BaseObservable {
    private EventDetailSoloPresenter presenter;
    private Event soloEvent;

    public EventSoloDetailViewModel(EventDetailSoloPresenter presenter) {
        this.presenter = presenter;

    }

    public Context getContext(){
        return presenter.getContext();
    }

    public View.OnClickListener onClickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.getView()!=null){
                    presenter.getView().toWeekView();
                }
            }
        };
    }

    public View.OnClickListener toEditEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.getView()!=null){
                    presenter.getView().toEditEvent();
                }
            }
        };
    }



//    **********************************************************************************

    @Bindable
    public Event getSoloEvent() {
        return soloEvent;
    }

    public void setSoloEvent(Event soloEvent) {
        this.soloEvent = soloEvent;
        notifyPropertyChanged(BR.soloEvent);
    }
}
