package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.EventEditPresenter;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends BaseObservable {

    private String eventEditViewTitle;
    private String eventEditViewLocation;
    private String eventEditViewRepeat;
    private String eventEditViewSuggestTimeSlotFst;
    private String eventEditViewSuggestTimeSlotSnd;
    private String eventEditViewSuggestTimeSlotTrd;
    private String eventEditViewAttendeeString;
    private String eventEditViewUrl;
    private String eventEditViewNote;

    private Event eventEditViewEvent;

    private EventEditPresenter eventEditPresenter;
    public EventEditViewModel(EventEditPresenter eventEditPresenter) {
        this.eventEditPresenter = eventEditPresenter;
    }

    @Bindable
    public String getEventEditViewTitle() {
        return eventEditViewTitle;
    }

    public void setEventEditViewTitle(String eventEditViewTitle) {
        this.eventEditViewTitle = eventEditViewTitle;
        notifyPropertyChanged(BR.eventEditViewTitle);
    }

    @Bindable
    public String getEventEditViewLocation() {
        return eventEditViewLocation;
    }

    public void setEventEditViewLocation(String eventEditViewLocation) {
        this.eventEditViewLocation = eventEditViewLocation;
        notifyPropertyChanged(BR.eventEditViewLocation);
    }

    @Bindable
    public String getEventEditViewRepeat() {
        return eventEditViewRepeat;
    }

    public void setEventEditViewRepeat(String eventEditViewRepeat) {
        this.eventEditViewRepeat = eventEditViewRepeat;
        notifyPropertyChanged(BR.eventEditViewRepeat);
    }

    @Bindable
    public String getEventEditViewSuggestTimeSlotFst() {
        return eventEditViewSuggestTimeSlotFst;
    }

    public void setEventEditViewSuggestTimeSlotFst(String eventEditViewSuggestTimeSlotFst) {
        this.eventEditViewSuggestTimeSlotFst = eventEditViewSuggestTimeSlotFst;
        notifyPropertyChanged(BR.eventEditViewSuggestTimeSlotFst);
    }

    @Bindable
    public String getEventEditViewSuggestTimeSlotSnd() {
        return eventEditViewSuggestTimeSlotSnd;
    }

    public void setEventEditViewSuggestTimeSlotSnd(String eventEditViewSuggestTimeSlotSnd) {
        this.eventEditViewSuggestTimeSlotSnd = eventEditViewSuggestTimeSlotSnd;
        notifyPropertyChanged(BR.eventEditViewSuggestTimeSlotSnd);
    }

    @Bindable
    public String getEventEditViewSuggestTimeSlotTrd() {
        return eventEditViewSuggestTimeSlotTrd;
    }

    public void setEventEditViewSuggestTimeSlotTrd(String eventEditViewSuggestTimeSlotTrd) {
        this.eventEditViewSuggestTimeSlotTrd = eventEditViewSuggestTimeSlotTrd;
        notifyPropertyChanged(BR.eventEditViewSuggestTimeSlotTrd);
    }

    @Bindable
    public String getEventEditViewAttendeeString() {
        return eventEditViewAttendeeString;
    }

    public void setEventEditViewAttendeeString(String eventEditViewAttendeeString) {
        this.eventEditViewAttendeeString = eventEditViewAttendeeString;
        notifyPropertyChanged(BR.eventEditViewAttendeeString);
    }

    @Bindable
    public String getEventEditViewUrl() {
        return eventEditViewUrl;
    }

    public void setEventEditViewUrl(String eventEditViewUrl) {
        this.eventEditViewUrl = eventEditViewUrl;
        notifyPropertyChanged(BR.eventEditViewUrl);
    }

    @Bindable
    public String getEventEditViewNote() {
        return eventEditViewNote;
    }

    public void setEventEditViewNote(String eventEditViewNote) {
        this.eventEditViewNote = eventEditViewNote;
        notifyPropertyChanged(BR.eventEditViewNote);
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
