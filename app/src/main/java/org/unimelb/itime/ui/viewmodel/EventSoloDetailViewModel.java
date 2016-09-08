package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventDetailForInviteePresenter;
import org.unimelb.itime.ui.presenter.EventDetailForSoloPresenter;

/**
 * Created by Paul on 3/09/2016.
 */
public class EventSoloDetailViewModel extends BaseObservable {
    private EventDetailForSoloPresenter presenter;
    private Event soloEvent;
    private String soloEventTitleString;
    private String soloEventRepeatString;
    private String soloEventLocationString;
    private String soloEventUrl;
    private String soloEventNote;

    public EventSoloDetailViewModel(EventDetailForSoloPresenter presenter, Event event) {
        this.soloEvent = event;
        this.presenter = presenter;
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

    @Bindable
    public String getSoloEventTitleString() {
        return soloEventTitleString;
    }

    public void setSoloEventTitleString(String soloEventTitleString) {
        this.soloEventTitleString = soloEventTitleString;
        notifyPropertyChanged(BR.soloEventTitleString);
    }

    @Bindable
    public String getSoloEventRepeatString() {
        return soloEventRepeatString;
    }

    public void setSoloEventRepeatString(String soloEventRepeatString) {
        this.soloEventRepeatString = soloEventRepeatString;
        notifyPropertyChanged(BR.soloEventRepeatString);
    }

    @Bindable
    public String getSoloEventLocationString() {
        return soloEventLocationString;
    }

    public void setSoloEventLocationString(String soloEventLocationString) {
        this.soloEventLocationString = soloEventLocationString;
        notifyPropertyChanged(BR.soloEventLocationString);
    }

    @Bindable
    public String getSoloEventUrl() {
        return soloEventUrl;
    }

    public void setSoloEventUrl(String soloEventUrl) {
        this.soloEventUrl = soloEventUrl;
        notifyPropertyChanged(BR.soloEventUrl);
    }

    @Bindable
    public String getSoloEventNote() {
        return soloEventNote;
    }

    public void setSoloEventNote(String soloEventNote) {
        this.soloEventNote = soloEventNote;
        notifyPropertyChanged(BR.soloEventUrl);
    }
}
