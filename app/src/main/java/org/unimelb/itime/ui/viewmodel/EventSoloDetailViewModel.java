package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventDetailForSoloPresenter;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventSoloDetailViewModel extends BaseObservable {
    private EventDetailForSoloPresenter presenter;
    private Event soloEvent;

    public EventSoloDetailViewModel(EventDetailForSoloPresenter presenter, Event event) {
        this.soloEvent = event;
        this.presenter = presenter;
    }

    public Context getContext(){
        return presenter.getContext();
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
