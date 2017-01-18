package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCustomRepeatBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventCustomRepeatMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.FrequencyEnum;

import java.util.List;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventCustomRepeatFragment extends BaseUiAuthFragment<EventCustomRepeatMvpView, EventPresenter<EventCustomRepeatMvpView>> implements EventCustomRepeatMvpView{
    /**
     * the key for pass bundle for arguments
     */
    private final static String TAG = "EventCustomRepeatFragment";

    private FragmentEventCustomRepeatBinding binding;
    private Event orgEvent = null;
    private Event editEvent = null;
    private EventManager eventManager;

    private EventEditViewModel eventEditViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_custom_repeat, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventManager = EventManager.getInstance(getContext());
        eventEditViewModel = new EventEditViewModel(getPresenter());
        eventEditViewModel.setEvent(editEvent);
        initToolbar();

        binding.setEventEditVM(eventEditViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initToolbar(){
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.repeat_custom));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
    }

    public void setEvent(Event event){
        this.orgEvent = event;
        this.editEvent = EventUtil.copyEvent(event);
        this.editEvent.getRule().setFrequencyEnum(FrequencyEnum.DAILY);
    }

    @Override
    public EventPresenter<EventCustomRepeatMvpView> createPresenter() {
        return new EventPresenter<>(getContext());
    }

    @Override
    public void onTaskStart(int task) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {
        hideProgressDialog();
        toCalendar();
    }

    @Override
    public void onTaskError(int taskId) {
        hideProgressDialog();
    }

    private void toCalendar(){
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onBack() {
        EventEditFragment frag = new EventEditFragment();
        frag.setEvent(orgEvent);
        getBaseActivity().backFragment(frag);

    }

    @Override
    public void onNext() {
        EventEditFragment frag = new EventEditFragment();
        editEvent.setRecurrence(editEvent.getRule().getRecurrence()); // this use for set recurrence to event
        frag.setEvent(editEvent);
        getBaseActivity().backFragment(frag);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void popUpFrequencyPickerDialog() {
        final String[] values = EventUtil.getRepeatFreqStr();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setCancelable(false);

        final NumberPicker artikkler = new NumberPicker(getContext());

        artikkler.setMaxValue(values.length - 1);
        artikkler.setMinValue(0);
        artikkler.setDisplayedValues(values);
        artikkler.setWrapSelectorWheel(true);
        artikkler.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        alert.setView(artikkler);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                int index = artikkler.getValue();
                editEvent.getRule().setFrequencyEnum(EventUtil.getFreqEnum(values[index]));
                eventEditViewModel.setEvent(editEvent);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    @Override
    public void popUpEveryPickerDialog() {
        final String[] values = EventUtil.getRepeatIntervalStr();
        final AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setCancelable(false);

        final NumberPicker artikkler = new NumberPicker(getContext());

        artikkler.setMaxValue(values.length - 1);
        artikkler.setMinValue(0);
        artikkler.setDisplayedValues(values);
        artikkler.setWrapSelectorWheel(true);
        artikkler.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        alert.setView(artikkler);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                int index = artikkler.getValue();
                editEvent.getRule().setInterval(Integer.valueOf(values[index]));
                eventEditViewModel.setEvent(editEvent);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

}
