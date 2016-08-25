package org.unimelb.itime.ui.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;


/**
 * Created by Paul on 23/08/2016.
 */
public class TestEvent extends BaseObservable{

    private String eventTitle;
    private String eventLocation;

    public TestEvent(String eventTitle, String eventLocation) {
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
    }

    @Bindable
    public String getEventTitle(){
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
        notifyPropertyChanged(BR.eventTitle);
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
