package org.unimelb.itime.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.presenter.EventDetailForHostPresenter;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailForHostViewModel extends BaseObservable {
    private EventDetailForHostPresenter presenter;
    private Event EvDtlHostEvent;
    private LayoutInflater inflater;

    private String tag;

    public EventDetailForHostViewModel(EventDetailForHostPresenter presenter) {
        this.presenter = presenter;
        this.inflater = presenter.getInflater();
        tag = presenter.getContext().getString(R.string.tag_host_event_detail);
    }

    public Context getContext() {
        return presenter.getContext();
    }

    public View.OnClickListener editEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toEditEvent(EvDtlHostEvent);
            }
        };
    }

    private void unSelectRestTimeSlots(int selectTimeSlotIndex) {
        for (int i = 0; i < EvDtlHostEvent.getTimeslots().size(); i++) {
            if (i != selectTimeSlotIndex) {
                EvDtlHostEvent.getTimeslots().get(i).setStatus(getContext().getString(R.string.timeslot_status_pending));
            }
        }
    }

    public View.OnClickListener onHostTimeSlotSelect1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EvDtlHostEvent.getHostUserUid().equals(UserUtil.getUserUid())) {
                    // this is for host choose timeslot
                    if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(0))) {
                        EvDtlHostEvent.getTimeslots().get(0).setStatus(getContext().getString(R.string.timeslot_status_pending));
                    } else {
                        if (TimeSlotUtil.chooseAtLeastOnTimeSlot(getContext(), EvDtlHostEvent.getTimeslots())) {
                            unSelectRestTimeSlots(0);
                        }
                        EvDtlHostEvent.getTimeslots().get(0).setStatus(getContext().getString(R.string.timeslot_status_accept));
                    }
//                    setEvDtlHostEvent(EvDtlHostEvent);
                } else {
                    // this is for invitee choose timeslots
                    if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(0))) {
                        EvDtlHostEvent.getTimeslots().get(0).setStatus(getContext().getString(R.string.timeslot_status_pending));
                    } else {
                        EvDtlHostEvent.getTimeslots().get(0).setStatus(getContext().getString(R.string.timeslot_status_accept));
                    }
                }
                setEvDtlHostEvent(EvDtlHostEvent); // update
            }
        };
    }


    public View.OnClickListener onHostTimeSlotSelect2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EvDtlHostEvent.getHostUserUid().equals(UserUtil.getUserUid())) {
                    // host picks timeslot
                    if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(1))) {
                        EvDtlHostEvent.getTimeslots().get(1).setStatus(getContext().getString(R.string.timeslot_status_pending));
                    } else {
                        if (TimeSlotUtil.chooseAtLeastOnTimeSlot(getContext(), EvDtlHostEvent.getTimeslots())) {
                            unSelectRestTimeSlots(1);
                        }
                        EvDtlHostEvent.getTimeslots().get(1).setStatus(getContext().getString(R.string.timeslot_status_accept));
                    }
//                    setEvDtlHostEvent(EvDtlHostEvent);
                } else {
                    // invitee picks timeslot
                    if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(1))) {
                        EvDtlHostEvent.getTimeslots().get(1).setStatus(getContext().getString(R.string.timeslot_status_pending));
                    } else {
                        EvDtlHostEvent.getTimeslots().get(1).setStatus(getContext().getString(R.string.timeslot_status_accept));
                    }
                }
                setEvDtlHostEvent(EvDtlHostEvent);
            }
        };
    }


    public View.OnClickListener onHostTimeSlotSelect3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EvDtlHostEvent.getHostUserUid().equals(UserUtil.getUserUid())) {
                    // host picks timeslot
                    if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(2))) {
                        EvDtlHostEvent.getTimeslots().get(2).setStatus(getContext().getString(R.string.timeslot_status_pending));
                    } else {
                        if (TimeSlotUtil.chooseAtLeastOnTimeSlot(getContext(), EvDtlHostEvent.getTimeslots())) {
                            unSelectRestTimeSlots(2);
                        }
                        EvDtlHostEvent.getTimeslots().get(2).setStatus(getContext().getString(R.string.timeslot_status_accept));
                    }
//                setEvDtlHostEvent(EvDtlHostEvent);
                } else {
                    if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(2))) {
                        EvDtlHostEvent.getTimeslots().get(2).setStatus(getContext().getString(R.string.timeslot_status_pending));
                    } else {
                        EvDtlHostEvent.getTimeslots().get(2).setStatus(getContext().getString(R.string.timeslot_status_accept));
                    }
                }
                setEvDtlHostEvent(EvDtlHostEvent);
            }
        };
    }

    public View.OnClickListener viewInCalendar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.viewInCalendar(tag);
            }
        };
    }

    public View.OnClickListener gotoUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = EvDtlHostEvent.getUrl();
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }

    public View.OnClickListener onClickHostConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to save data later, implement later
                presenter.toWeekView();
            }
        };
    }

    public View.OnClickListener onClickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                presenter.toEventDetailHost(); // wrong
                presenter.toWeekView();
            }
        };
    }

    public View.OnClickListener toAttendeeView1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getTimeslots().get(0).getStartTime());
            }
        };
    }

    public View.OnClickListener toAttendeeView2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getTimeslots().get(1).getStartTime());
            }
        };
    }

    public View.OnClickListener toAttendeeView3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getTimeslots().get(2).getStartTime());
            }
        };
    }

    public View.OnClickListener onClickRejectAll() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();

                inflater = presenter.getInflater();
                View root = inflater.inflate(R.layout.event_detail_reject_alert_view, null);

                TextView button_cancel = (TextView) root.findViewById(R.id.alert_message_cancel_button);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                TextView button_reject = (TextView) root.findViewById(R.id.alert_message_reject_button);
                button_reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence msg = "send reject message";
                        Toast.makeText(presenter.getContext(), msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(root);
                alertDialog.show();
            }
        };
    }

    public View.OnClickListener onClickConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.confirmAndGotoWeekViewCalendar(EvDtlHostEvent);
            }
        };
    }


//    ***************************************************************

    @Bindable
    public Event getEvDtlHostEvent() {
        return EvDtlHostEvent;
    }

    public void setEvDtlHostEvent(Event evDtlHostEvent) {
        EvDtlHostEvent = evDtlHostEvent;
        notifyPropertyChanged(BR.evDtlHostEvent);
    }


//    public void setTimeSlotChooseArray(boolean[] timeSlotChooseArray) {
//        this.timeSlotChooseArray = timeSlotChooseArray;
//
//    }

}
