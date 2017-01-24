package org.unimelb.itime.ui.mvpview;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.List;

/**
 * Created by Paul on 24/1/17.
 */

public interface LocationPickerMvpView<T> extends TaskBasedMvpView<T>, ItimeCommonMvpView {
    void onFilterLocations(List<AutocompletePrediction> locations);
    void onChooseLocation(String location);
}
