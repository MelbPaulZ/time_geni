package org.unimelb.itime.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.databinding.FragmentLocationPickerBinding;
import org.unimelb.itime.ui.mvpview.LocationPickerMvpView;
import org.unimelb.itime.ui.presenter.LocationPresenter;
import org.unimelb.itime.ui.viewmodel.LocationViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Paul on 27/08/2016.
 * todo: change to data binding
 */
public class LocationPickerFragment extends BaseUiAuthFragment<LocationPickerMvpView<AutocompletePrediction>, LocationPresenter<LocationPickerMvpView<AutocompletePrediction>>>
        implements LocationPickerMvpView<AutocompletePrediction>, GoogleApiClient.OnConnectionFailedListener {

    private final static String TAG = "EventLocationPickerFragment";
    public final static String LOCATION = "location";

    public final static int RET_LOCATION_SUCCESS = 1000;
    public final static int RET_LOCATION_CANCEL = 1001;


    private FragmentLocationPickerBinding binding;
    private LocationViewModel viewModel;
    private ToolbarViewModel toolbarViewModel;

    protected GoogleApiClient mGoogleApiClient;

    private LocationPresenter.PlaceAutoCompleteAdapter mAdapter;

    private ITimeLocationAdapter strAdapter;

//    private AutoCompleteTextView mAutocompleteView;

    private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
    private String place;
    ArrayList<String> locations = new ArrayList<>();
    double longitude, latitude;
    private boolean isCurrentLocationGet = false;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_picker, container, false);
        return binding.getRoot();
    }

    @Override
    public LocationPresenter<LocationPickerMvpView<AutocompletePrediction>> createPresenter() {
        return new LocationPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initTitles();
//        initListeners();
    }

    private void init() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            longitude = EventUtil.longitude;
            latitude = EventUtil.latitude;
        }

        // dynamically calculate location and search by this
        LatLngBounds locationNearByBounds = new LatLngBounds( // set the bias area
                new LatLng(latitude - 0.10, longitude - 0.10), new LatLng(latitude + 0.10, longitude + 0.10));

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        viewModel = new LocationViewModel(getPresenter());
//
//        mAutocompleteView = (AutoCompleteTextView)
//                root.findViewById(R.id.autocomplete_places);

        mAdapter = new LocationPresenter.PlaceAutoCompleteAdapter(getContext(), mGoogleApiClient, locationNearByBounds, null);
        mAdapter.setMvpView(this);
        presenter.setPlaceAutoCompleteAdapter(mAdapter);
        binding.setLocationVM(viewModel);

//        locations.add(getString(R.string.current_location));
//
//        strAdapter = new ITimeLocationAdapter(getContext());

//        mAutocompleteView.setOnItemClickListener(currentLocationListener);
//        mAutocompleteView.setAdapter(strAdapter);
//
//        mAutocompleteView.setText(place);
    }

    private void initTitles(){
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
        binding.setToolbarVM(toolbarViewModel);

    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


//    public void initListeners() {
//        final TextView backBtn = (TextView) root.findViewById(R.id.location_picker_back_btn);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                back();
//            }
//        });
//
//
//        mAutocompleteView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (mAutocompleteView.getAdapter().equals(strAdapter) && mAutocompleteView.getText().length() == 0) {
//                    mAutocompleteView.showDropDown();
//                }
//                return false;
//            }
//        });
//
//
//        TextView doneBtn = (TextView) root.findViewById(R.id.location_picker_done_btn);
//        doneBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                next();
//            }
//        });
//
//        mAutocompleteView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                place = editable.toString();
//                if (editable.length() == 1 && mAutocompleteView.getAdapter().equals(strAdapter)) {
//                    mAutocompleteView.setAdapter(mAdapter);
//                    mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
//                } else if (editable.length() == 0 && mAutocompleteView.getAdapter().equals(mAdapter)) {
//                    mAutocompleteView.setAdapter(strAdapter);
//                    mAutocompleteView.setOnItemClickListener(currentLocationListener);
//                    mAutocompleteView.showDropDown();
//                }
//            }
//        });
//
//        ImageView cleanBtn = (ImageView) root.findViewById(R.id.location_picker_clean_btn);
//        cleanBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAutocompleteView.setText("");
//                mAutocompleteView.setAdapter(strAdapter);
//                mAutocompleteView.setOnItemClickListener(currentLocationListener);
//                strAdapter.notifyDataSetChanged();
//                mAutocompleteView.showDropDown();
//                place = "";
//            }
//        });
//    }


    public void getCurrentLocation() {
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOC);
            } else {
                final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);

                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                        double bestLikelihood = 0.0;
                        String fullAddress = "";
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            if (placeLikelihood.getLikelihood() > bestLikelihood) {
                                // can use this to change later
                                fullAddress = (String) placeLikelihood.getPlace().getAddress(); // here will get a long address
                                locations.add(fullAddress);
                                EventUtil.latitude = placeLikelihood.getPlace().getLatLng().latitude;
                                EventUtil.longitude = placeLikelihood.getPlace().getLatLng().longitude;
                                bestLikelihood = placeLikelihood.getLikelihood();
                            }
                        }
                        likelyPlaces.release();
                        place = fullAddress;
                        isCurrentLocationGet = true;
                    }
                });
            }
        }
    }

//    private AdapterView.OnItemClickListener currentLocationListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            String clickStr = (String) strAdapter.getItem(i);
//            if (i == 0) {
//                getCurrentLocation();
//                // TODO: 21/1/17 chongzuo zheli
////                while (!isCurrentLocationGet){
////
////                }
////                isCurrentLocationGet = false;
//                mAutocompleteView.setText(place);
//            } else {
//                mAutocompleteView.setText(clickStr);
//            }
//
//            if (checkNetwork()) {
//                mAutocompleteView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
//                mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
//            } else {
//                Toast.makeText(getContext(), "network error, cannot find current location", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    };

    private boolean checkNetwork() {
        // check network connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (int i = 0; i < networkInfo.length; i++) {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }


//    private AdapterView.OnItemClickListener mAutocompleteClickListener
//            = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            final AutocompletePrediction item = mAdapter.getItem(position);
//            final CharSequence primaryText = item.getPrimaryText(null);
//
//            locations.add(1, (String) primaryText);
//            if (locations.size() > 5) {
//                locations.remove(5);
//            }
//            // here should change to mAdapter, after click on autoComplete Text
//            mAutocompleteView.setAdapter(mAdapter);
//            mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
//            strAdapter.notifyDataSetChanged();
//        }
//    };


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getContext(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, AutocompletePrediction data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onBack() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onNext() {
        Intent intent = new Intent();
        intent.putExtra("location", place);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onFilterLocations(List<AutocompletePrediction> locations) {
        ObservableList<LocationViewModel.LocationInfoViewModel> infoViewModels = new ObservableArrayList<>();
        for (AutocompletePrediction prediction : locations){
            infoViewModels.add(
                    new LocationViewModel.LocationInfoViewModel((String)prediction.getPrimaryText(null),
                            (String)prediction.getSecondaryText(null)));
        }
        viewModel.setLocations(infoViewModels);
    }

    @Override
    public void onChooseLocation(String location) {
        place = location;
        onNext();
    }

//    public class PlaceAutoCompleteAdapter
//            extends ArrayAdapter<AutocompletePrediction> implements Filterable {
//
//        private static final String TAG = "AutocompleteAdapter";
//        private final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
//        /**
//         * Current results returned by this adapter.
//         */
//        private ArrayList<AutocompletePrediction> mResultList = new ArrayList<>();
//
//        /**
//         * Handles autocomplete requests.
//         */
//        private GoogleApiClient mGoogleApiClient;
//
//        /**
//         * The bounds used for Places Geo Data autocomplete API requests.
//         */
//        private LatLngBounds mBounds;
//
//        /**
//         * The autocomplete filter used to restrict queries to a specific set of place types.
//         */
//        private AutocompleteFilter mPlaceFilter;
//
//        /**
//         * Initializes with a resource for text rows and autocomplete query bounds.
//         *
//         * @see android.widget.ArrayAdapter#ArrayAdapter(android.content.Context, int)
//         */
//        public PlaceAutoCompleteAdapter(Context context, GoogleApiClient googleApiClient,
//                                        LatLngBounds bounds, AutocompleteFilter filter) {
//            super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
//            mGoogleApiClient = googleApiClient;
//            mBounds = bounds;
//            mPlaceFilter = filter;
//        }
//
//        /**
//         * Sets the bounds for all subsequent queries.
//         */
//        public void setBounds(LatLngBounds bounds) {
//            mBounds = bounds;
//        }
//
//        /**
//         * Returns the number of results received in the last autocomplete query.
//         */
//        @Override
//        public int getCount() {
//            return mResultList.size();
//        }
//
//        /**
//         * Returns an item from the last autocomplete query.
//         */
//        @Override
//        public AutocompletePrediction getItem(int position) {
//            return mResultList.get(position);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View row = super.getView(position, convertView, parent);
//
//            // Sets the primary and secondary text for a row.
//            // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
//            // styling based on the given CharacterStyle.
//
//            AutocompletePrediction item = getItem(position);
//
//            TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
//            TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
//            textView1.setText(item.getPrimaryText(STYLE_BOLD));
//            textView2.setText(item.getSecondaryText(STYLE_BOLD));
//
//            return row;
//        }
//
//        /**
//         * Returns the filter for the current set of autocomplete results.
//         */
//        @Override
//        public Filter getFilter() {
//            return new Filter() {
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    FilterResults results = new FilterResults();
//
//                    // We need a separate list to store the results, since
//                    // this is run asynchronously.
//                    ArrayList<AutocompletePrediction> filterData = new ArrayList<>();
//
//                    // Skip the autocomplete query if no constraints are given.
//                    if (constraint != null) {
//                        // Query the autocomplete API for the (constraint) search string.
//                        filterData = getAutocomplete(constraint);
//                    }
//
//                    results.values = filterData;
//                    if (filterData != null) {
//                        results.count = filterData.size();
//                    } else {
//                        results.count = 0;
//                    }
//
//                    return results;
//                }
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (mAutocompleteView != null && mAutocompleteView.getAdapter().equals(mAdapter)) {
//                        if (results != null && results.count > 0) {
//                            // The API returned at least one result, update the data.
//                            mResultList = (ArrayList<AutocompletePrediction>) results.values;
//                            notifyDataSetChanged();
//                        } else {
//                            // The API did not return any results, invalidate the data set.
//                            notifyDataSetInvalidated();
//                        }
//                    }
//                }
//
//                @Override
//                public CharSequence convertResultToString(Object resultValue) {
//                    // Override this method to display a readable result in the AutocompleteTextView
//                    // when clicked.
//                    if (resultValue instanceof AutocompletePrediction) {
//                        return ((AutocompletePrediction) resultValue).getPrimaryText(null);
//                    } else {
//                        return super.convertResultToString(resultValue);
//                    }
//                }
//            };
//        }
//
//        /**
//         * Submits an autocomplete query to the Places Geo Data Autocomplete API.
//         * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
//         * objects to store the Place ID and description that the API returns.
//         * Returns an empty list if no results were found.
//         * Returns null if the API client is not available or the query did not complete
//         * successfully.
//         * This method MUST be called off the main UI thread, as it will block until data is returned
//         * from the API, which may include a network request.
//         *
//         * @param constraint Autocomplete query string
//         * @return Results from the autocomplete API or null if the query was not successful.
//         * @see Places#GEO_DATA_API#getAutocomplete(CharSequence)
//         * @see AutocompletePrediction#freeze()
//         */
//        private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
//            if (mGoogleApiClient.isConnected()) {
//                Log.i(TAG, "Starting autocomplete query for: " + constraint);
//
//                // Submit the query to the autocomplete API and retrieve a PendingResult that will
//                // contain the results when the query completes.
//                PendingResult<AutocompletePredictionBuffer> results =
//                        Places.GeoDataApi
//                                .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
//                                        mBounds, mPlaceFilter);
//
//                // This method should have been called off the main UI thread. Block and wait for at most 60s
//                // for a result from the API.
//                AutocompletePredictionBuffer autocompletePredictions = results
//                        .await(60, TimeUnit.SECONDS);
//
//                // Confirm that the query completed successfully, otherwise return null
//                final Status status = autocompletePredictions.getStatus();
//                if (!status.isSuccess()) {
////                    Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
////                            Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "Error getting autocomplete prediction API call" + status.toString());
//                    autocompletePredictions.release();
//                    return null;
//                }
//
//                Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
//                        + " predictions.");
//
//                // Freeze the results immutable representation that can be stored safely.
//                return DataBufferUtils.freezeAndClose(autocompletePredictions);
//            }
//            Log.e(TAG, "Google API client is not connected for autocomplete query.");
//            return null;
//        }
//
//
//    }

    public class ITimeLocationAdapter extends BaseAdapter implements Filterable {
        LayoutInflater inflater;
        LocationFilter locationFilter;

        public ITimeLocationAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return locations.size();
        }

        @Override
        public Object getItem(int i) {
            return locations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, viewGroup, false);
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(locations.get(i));
            return view;
        }

        @Override
        public Filter getFilter() {
            if (locationFilter == null) {
                locationFilter = new LocationFilter();
            }
            return locationFilter;
        }

        public class LocationFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constrains) {
                FilterResults filterResults = new FilterResults();
                filterResults.count = locations.size();
                filterResults.values = locations;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        }
    }
}
