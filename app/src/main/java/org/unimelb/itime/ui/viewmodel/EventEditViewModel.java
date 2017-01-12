package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.fesky.library.widget.ios.ActionSheetDialog;
import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditViewModel extends EventCommonViewModel {

    private Event event;
    private EventPresenter<? extends TaskBasedMvpView<List<Event>>> presenter;
    private ObservableField<Boolean> editEventIsRepeat = new ObservableField<>();
    private EventEditMvpView mvpView;
    private PickerTask currentTask;
    private int startTimeVisibility, endTimeVisibility;
    private long evStartTime, evEndTime;

    private List<Timeslot> timeslotList = new ArrayList<>();
    private ItemView itemView = ItemView.of(BR.timeslot, R.layout.timeslot_listview_show);

    private EventManager eventManager;
    private boolean isEndTimeChanged = false;


    public EventEditViewModel(EventPresenter<? extends TaskBasedMvpView<List<Event>>> presenter) {
        this.presenter = presenter;
        eventManager = EventManager.getInstance(getContext());
        mvpView = (EventEditMvpView) presenter.getView();
        editEventIsRepeat = new ObservableField<>(false); // TODO: 11/12/2016 change this and isAlldayEvent later.... needs to be bind with event
        initDialog();
    }


    private void initDialog() {
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                switch (currentTask) {
                    case END_TIME:
                        event.setEndTime(updateYearMonthDay(event.getEndTime(), year, monthOfYear, dayOfMonth).getTimeInMillis());
                        isEndTimeChanged = true;
                        break;
                    case START_TIME:
                        event.setStartTime(updateYearMonthDay(event.getStartTime(), year, monthOfYear, dayOfMonth).getTimeInMillis());
                        if (!isEndTimeChanged) {
                            event.setEndTime(event.getStartTime() + 60 * 60 * 1000);
                        }
                        break;
                    case END_REPEAT:
                        onEndRepeatSelected(year, monthOfYear, dayOfMonth);
                        break;
                }
                notifyPropertyChanged(BR.eventEditViewEvent);
            }
        };

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (currentTask) {
                    case END_TIME:
                        event.setEndTime(updateHourMin(event.getEndTime(), hourOfDay, minute).getTimeInMillis());
                        isEndTimeChanged = true;
                        break;
                    case START_TIME:
                        event.setStartTime(updateHourMin(event.getStartTime(), hourOfDay, minute).getTimeInMillis());
                        if (!isEndTimeChanged) {
                            event.setEndTime(event.getStartTime() + 60 * 60 * 1000);
                        }
                        break;
                }
                notifyPropertyChanged(BR.eventEditViewEvent);
            }
        };
    }


    private void onEndRepeatSelected(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        event.getRule().setUntil(c.getTime());
        event.setRecurrence(event.getRule().getRecurrence());
        notifyPropertyChanged(BR.eventEditViewEvent);
    }

    /**
     * check the focus of email edit text
     *
     * @return
     */
    public View.OnFocusChangeListener onEditFocusChange() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showKeyBoard(view);
                } else {
                    closeKeyBoard(view);
                }
            }
        };
    }


    public Context getContext() {
        return presenter.getContext();
    }


    // click cancel button on edit event page
    public View.OnClickListener cancelEdit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.toEventDetailPage();
                }
            }
        };
    }

    public void onBack() {
        if (mvpView != null) {
            mvpView.toEventDetailPage();
        }
    }

    public void onNext() {
        // popup alertDialog to choose whether change all or just one
        List<Timeslot> timeslots = EventUtil.getTimeslotFromPending(getContext(), event);
        event.setTimeslot(timeslots);

        if (eventManager.getCurrentEvent().getRecurrence().length > 0) {
            // the event is repeat event
            final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View root = inflater.inflate(R.layout.repeat_event_change_alert_view, null);
            alertDialog.setView(root);
            alertDialog.setTitle(getContext().getString(R.string.change_all_repeat_or_just_this_event));
            alertDialog.show();

            TextView button_change_all = (TextView) root.findViewById(R.id.alert_message_change_all_button);
            button_change_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    event.setEventType(EventUtil.getEventType(event, UserUtil.getInstance(getContext()).getUserUid()));
                    changeAllEvent(event);
                    alertDialog.dismiss();
                }
            });

            TextView button_only_this = (TextView) root.findViewById(R.id.alert_message_only_this_event_button);
            button_only_this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    event.setEventType(EventUtil.getEventType(event, UserUtil.getInstance(getContext()).getUserUid()));
                    changeOnlyThisEvent(event);
                    alertDialog.dismiss();

                }
            });

        } else {
            // set event type
            EventUtil.addSelfInInvitee(getContext(), event);
            event.setEventType(EventUtil.getEventType(event, UserUtil.getInstance(getContext()).getUserUid()));

            // if solo, need to manually set status confirmed
            if (event.getEventType().equals(Event.TYPE_SOLO)) {
                event.setStatus(Event.STATUS_CONFIRMED);
            }

            Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
            presenter.updateEvent(event, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
            // this if might change later, because the host can be kicked??????
        }
    }


    public Switch.OnCheckedChangeListener eventAlldayChange(final Event event) {
        return new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setEventToAlldayEvent(event);
                } else {
                    setAlldayEventToNotAllday(event);
                }
            }
        };
    }

    private void setEventToAlldayEvent(Event event) {
        evStartTime = event.getStartTime();
        evEndTime = event.getEndTime();
        long beginOfStartDay = EventUtil.getDayBeginMilliseconds(event.getStartTime());
        long endOfEndDay = EventUtil.getDayEndMilliseconds(event.getEndTime());
        event.setStartTime(beginOfStartDay);
        event.setEndTime(endOfEndDay);
        notifyPropertyChanged(BR.newEvDtlEvent);
    }

    private void setAlldayEventToNotAllday(Event event) {
        event.setStartTime(evStartTime);
        event.setEndTime(evEndTime);
        notifyPropertyChanged(BR.newEvDtlEvent);
    }

    // TODO: test this change all
    private void changeAllEvent(Event event) {
        Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
        presenter.updateEvent(event, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
    }

    private void changeOnlyThisEvent(Event event) {
        event.setRecurrence(event.getRule().getRecurrence()); // set the repeat string
        EventUtil.addSelfInInvitee(getContext(), event);
        event.setEventType(EventUtil.getEventType(event, UserUtil.getInstance(getContext()).getUserUid()));

        if (!EventUtil.isGroupEvent(getContext(), event)) {
            event.setStatus(Event.STATUS_CONFIRMED);
        }
        // here change the event as a new event
        Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
        presenter.updateEvent(event, EventCommonPresenter.UPDATE_THIS, orgEvent.getStartTime());

    }


    public View.OnClickListener editTimeSlot() {
        // chuan can shu next to do
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.toTimeslotViewPage(event);// tiao zhuan wei zhi
                }
            }
        };
    }


    public View.OnClickListener changeLocation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.toLocationPage();
                }
            }
        };
    }


    public String getRepeatString(Event event) {
        return EventUtil.getRepeatString(getContext(), event);
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] repeats;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_repeat));
                repeats = EventUtil.getRepeats(getContext(), event);
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set event recurrence
                        EventUtil.changeEventFrequency(event, i);
                        setEvent(event);
                    }
                });
                builder.show();
            }

        };
    }

    public View.OnClickListener onChooseAlertTime() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] alertTimes;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.choose_alert_time));
                alertTimes = EventUtil.getAlertTimes(getContext());
                builder.setItems(alertTimes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        event.setReminder(i);
                        setEvent(event);
                    }
                });
                builder.show();
            }
        };
    }


    public View.OnClickListener onChooseCalendarType() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] calendarType;
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle(getContext().getString(R.string.calendar));
                calendarType = EventUtil.getCalendarTypes(getContext());
                builder.setItems(calendarType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        event.setCalendarUid(CalendarUtil.getInstance(getContext()).getCalendar().get(i).getCalendarUid());
                        setEvent(event);
                    }
                });
                builder.show();
            }
        };
    }

    public View.OnClickListener toInviteePicker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.toInviteePickerPage(event);
                }
            }
        };
    }

    public View.OnClickListener onClickDelete() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event orgEvent = EventManager.getInstance(getContext()).getCurrentEvent();
                if (EventUtil.isEventRepeat(event)) {
                    // repeat event delete
                    if (event.getRecurrence().length == 0) {
                        presenter.deleteEvent(event, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                    } else {
                        repeatDeletePopup(orgEvent);
                    }
                } else {
                    // none repeat event delete
                    presenter.deleteEvent(event, EventCommonPresenter.UPDATE_ALL, orgEvent.getStartTime());
                }
            }
        };
    }

    private void repeatDeletePopup(final Event orgEvent) {
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(getContext())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getContext().getString(R.string.event_delete_repeat_text1), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                presenter.deleteEvent(event, EventCommonPresenter.UPDATE_THIS, orgEvent.getStartTime());
                            }
                        })
                .addSheetItem(getContext().getString(R.string.event_delete_repeat_text2), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int i) {
                                presenter.deleteEvent(event, EventCommonPresenter.UPDATE_FOLLOWING, orgEvent.getStartTime());
                            }
                        });
        actionSheetDialog.show();
    }

    public View.OnClickListener pickPhoto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.toPhotoPickerPage();
                }
            }
        };
    }

    public void setPhotos(ArrayList<String> photos) {
        event.setPhoto(EventUtil.fromStringToPhotoUrlList(getContext(), photos));
        setEvent(event);
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        if (event.hasTimeslots()) {
            timeslotList = event.getTimeslot();
        }
        notifyPropertyChanged(BR.eventEditViewEvent);
        notifyPropertyChanged(BR.startTimeVisibility);
        notifyPropertyChanged(BR.endTimeVisibility);
        notifyPropertyChanged(BR.editEventIsRepeat);
        notifyPropertyChanged(BR.timeslotList);
    }

    @Bindable
    public Boolean getEditEventIsRepeat() {
        if (event.hasRecurrence()) {
            editEventIsRepeat.set(true);
        } else {
            editEventIsRepeat.set(false);
        }
        return editEventIsRepeat.get();
    }

    public void setEditEventIsRepeat(Boolean editEventIsRepeat) {
        this.editEventIsRepeat.set(editEventIsRepeat);
        notifyPropertyChanged(BR.editEventIsRepeat);
    }

    public View.OnClickListener onChangeTime(final PickerTask task) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask = task;
                Calendar c = Calendar.getInstance();
                switch (currentTask) {
                    case END_TIME:
                        c.setTimeInMillis(event.getEndTime());
                        break;
                    case START_TIME:
                        c.setTimeInMillis(event.getStartTime());
                }
                timePickerDialog = new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, timeSetListener,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        };
    }

    public View.OnClickListener onChangeDate(final PickerTask task) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask = task;
                Calendar c = Calendar.getInstance();
                switch (currentTask) {
                    case END_REPEAT:
                        if (event.getRule().getUntil() != null) {
                            c.setTime(event.getRule().getUntil());
                        }
                        break;
                    case END_TIME:
                        c.setTimeInMillis(event.getEndTime());
                        break;
                    case START_TIME:
                        c.setTimeInMillis(event.getStartTime());
                }
                datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
    }


    @Bindable
    public int getStartTimeVisibility() {
        if (!EventUtil.hasOtherInviteeExceptSelf(getContext(), event)) {
            startTimeVisibility = View.VISIBLE;
        } else {
            startTimeVisibility = View.GONE;
        }
        return startTimeVisibility;
    }

    public void setStartTimeVisibility(int startTimeVisibility) {
        this.startTimeVisibility = startTimeVisibility;
        notifyPropertyChanged(BR.startTimeVisibility);
    }

    @Bindable
    public int getEndTimeVisibility() {
        return getStartTimeVisibility();
    }

    public void setEndTimeVisibility(int endTimeVisibility) {
        this.endTimeVisibility = endTimeVisibility;
        notifyPropertyChanged(BR.endTimeVisibility);
    }

    @Bindable
    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    @Bindable
    public ItemView getItemView() {
        return itemView;
    }
}
