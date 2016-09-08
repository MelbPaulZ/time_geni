package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.databinding.library.baseAdapters.BR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.messageevent.MessageEventTime;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.messageevent.MessageNewEvent;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewVIewModel extends BaseObservable {
    private EventCreateNewPresenter presenter;
    private Event event;

    private String tag;

    private ObservableField<Boolean> isEventRepeat;
    private boolean isEndTimeChanged = false;

    private CharSequence repeats[] = null;
    EventCreateNewVIewModel viewModel;
    private boolean isEndRepeatChanged = false;


    public EventCreateNewVIewModel(EventCreateNewPresenter presenter) {
        this.presenter = presenter;
        isEventRepeat = new ObservableField<>(false);
//        EventBus.getDefault().register(this);
        tag = presenter.getContext().getString(R.string.tag_create_event);
        this.viewModel = this;
    }

    public Context getContext() {
        return presenter.getContext();
    }

    public void init() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        if (!isEndTimeChanged) {
            event.setEndTime(event.getStartTime() + 3600000);
        }
        String dayOfWeek = EventUtil.getDayOfWeekFull(getContext(), calendar.get(Calendar.DAY_OF_WEEK));
        repeats = new CharSequence[]{
                getContext().getString(R.string.repeat_never),
                getContext().getString(R.string.repeat_everyday),
                String.format(getContext().getString(R.string.repeat_everyweek), dayOfWeek),
                String.format(getContext().getString(R.string.repeat_every_month)),
                String.format(getContext().getString(R.string.repeat_every_year))};
    }


    public View.OnClickListener test() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }


// ****************************************************

    public View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submit(event);
            }
        };
    }

    public View.OnClickListener pickStartDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = presenter.getContext().getString(R.string.tag_start_time);
                presenter.pickDate(tag);
            }
        };
    }

    public View.OnClickListener chooseRepeat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CharSequence repeats[] = new CharSequence[6];
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(event.getStartTime());
                String dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle("Choose a repeat type");
                builder.setItems(repeats, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!repeats[i].equals(getContext().getString(R.string.repeat_never))) {
                            setIsEventRepeat(true);
                            if (!isEndRepeatChanged) { // if the user didn't change end repeat time before
                                event.setRepeatEndsTime(calendar.getTimeInMillis() + 24*3600000); // default end in next day
                            }
                            event.setRepeatTypeId(i);
                            viewModel.setEvent(event);
                        }
                    }
                });
                builder.show();
                //
            }

        };
    }

    public View.OnClickListener chooseCalendarType() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence types[] = new CharSequence[]{"Work", "Private", "Group", "Public"};
                AlertDialog.Builder builder = new AlertDialog.Builder(presenter.getContext());
                builder.setTitle("Choose a calendar type");
                builder.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        event.setCalendarTypeId((String) types[i]);
                        viewModel.setEvent(event);
                    }
                });
                builder.show();
            }
        };
    }


    public View.OnClickListener gotoUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = event.getUrl();
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }


    public View.OnClickListener pickEndDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = presenter.getContext().getString(R.string.tag_end_time);
                isEndTimeChanged = true;
                presenter.pickDate(tag);
            }
        };
    }

    public View.OnClickListener pickEndRepeatDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEndRepeatChanged = true;
                tag = presenter.getContext().getString(R.string.tag_end_repeat);
                presenter.pickDate(tag);
            }
        };
    }

    public View.OnClickListener cancelNewEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.gotoWeekViewCalendar();
            }
        };
    }

    public View.OnClickListener locationPicker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.pickLocation(tag);
            }
        };
    }

    // click done btn
    public View.OnClickListener toCreateSoloEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!event.hasEventTitle()) {
                    event.setTitle(presenter.getContext().getString(R.string.new_event));
                }
                presenter.toCreateSoloEvent(event);
            }
        };
    }

    public View.OnClickListener attendeePicker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.pickAttendee();
            }
        };
    }





    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return presenter.getContext().getString(R.string.day_of_week_1_full);
            case 2:
                return presenter.getContext().getString(R.string.day_of_week_2_full);
            case 3:
                return presenter.getContext().getString(R.string.day_of_week_3_full);
            case 4:
                return presenter.getContext().getString(R.string.day_of_week_4_full);
            case 5:
                return presenter.getContext().getString(R.string.day_of_week_5_full);
            case 6:
                return presenter.getContext().getString(R.string.day_of_week_6_full);
            case 7:
                return presenter.getContext().getString(R.string.day_of_week_7_full);
        }
        return "error get day of week";
    }


//    ****************************************************************

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
        init();

    }


    @Bindable
    public Boolean getIsEventRepeat() {
        return isEventRepeat.get();
    }

    public void setIsEventRepeat(Boolean value) {
        this.isEventRepeat.set(value);
        notifyPropertyChanged(BR.isEventRepeat);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }
}
