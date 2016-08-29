package org.unimelb.itime.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventCreateActivity;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventLocationPickerFragment extends Fragment implements PlaceSelectionListener{
    private View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root== null)
            root = inflater.inflate(R.layout.fragment_event_location_pick, container, false);
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }

    @Override
    public void onPlaceSelected(Place place) {
        EventBus.getDefault().post(new MessageLocation((String) place.getName()));
        ((EventCreateActivity)getActivity()).toCreateEventNewFragment(this);
    }

    @Override
    public void onError(Status status) {
        Log.e("error", "onError: Status = " + status.toString());
    }
}
