package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.places.AutocompletePrediction;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.ui.mvpview.LocationPickerMvpView;
import org.unimelb.itime.ui.presenter.LocationPresenter;
import org.unimelb.itime.widget.InviteeGroupView;
import org.unimelb.itime.widget.SearchBar;

import me.tatarka.bindingcollectionadapter.ItemView;

import static android.webkit.WebSettings.PluginState.ON;

/**
 * Created by Paul on 24/1/17.
 */

public class LocationViewModel extends CommonViewModel {
    private LocationPresenter<LocationPickerMvpView<AutocompletePrediction>> presenter;
    private Context context;
    private LocationPickerMvpView<AutocompletePrediction> mvpView;

    private ObservableList<LocationInfoViewModel> locations = new ObservableArrayList<>();
    private ItemView locationItemView = ItemView.of(BR.location, R.layout.listview_location);

    public LocationViewModel(LocationPresenter<LocationPickerMvpView<AutocompletePrediction>> presenter) {
        this.presenter = presenter;
        this.context = presenter.getContext();
        this.mvpView = presenter.getView();
    }


    public SearchBar.OnEditListener onSearching(){
        return new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                presenter.getFilter().filter(text);
            }
        };
    }

    public ListView.OnItemClickListener onLocationClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationInfoViewModel selectedLocation = locations.get(position);
                if (mvpView!=null){
                    mvpView.onChooseLocation(selectedLocation.getPrimaryText()
                            + " "+ selectedLocation.getSecondaryText());
                }
            }
        };
    }

    @Bindable
    public ObservableList<LocationInfoViewModel> getLocations() {
        return locations;
    }

    public void setLocations(ObservableList<LocationInfoViewModel> locations){
        this.locations = locations;
        notifyPropertyChanged(BR.locations);
    }

    @Bindable
    public ItemView getLocationItemView() {
        return locationItemView;
    }

    public void setLocationItemView(ItemView locationItemView) {
        this.locationItemView = locationItemView;
        notifyPropertyChanged(BR.locationItemView);
    }

    public static class LocationInfoViewModel extends BaseObservable{
        private String primaryText;
        private String secondaryText;

        public LocationInfoViewModel(String primaryText, String secondaryText) {
            this.primaryText = primaryText;
            this.secondaryText = secondaryText;
        }

        public String getPrimaryText() {
            return primaryText;
        }

        public void setPrimaryText(String primaryText) {
            this.primaryText = primaryText;
        }

        public String getSecondaryText() {
            return secondaryText;
        }

        public void setSecondaryText(String secondaryText) {
            this.secondaryText = secondaryText;
        }
    }
}
