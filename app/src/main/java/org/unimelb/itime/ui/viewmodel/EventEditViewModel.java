package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends BaseObservable {

    private Event eventEditViewEvent;

    private EventEditPresenter eventEditPresenter;
    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.eventEditPresenter = eventEditPresenter;
    }


    public View.OnClickListener cancelEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        };
    }

    public View.OnClickListener finishEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        };
    }


    @Bindable
    public Event getEventEditViewEvent() {
        return eventEditViewEvent;
    }

    public void setEventEditViewEvent(Event eventEditViewEvent) {
        this.eventEditViewEvent = eventEditViewEvent;
        notifyPropertyChanged(BR.eventEditViewEvent);
    }

}
