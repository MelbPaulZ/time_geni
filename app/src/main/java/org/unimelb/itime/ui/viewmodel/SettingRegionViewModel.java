package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Region;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.util.UserUtil;

import me.tatarka.bindingcollectionadapter.ItemView;
import com.android.databinding.library.baseAdapters.BR;

import java.util.List;


/**
 * Created by Paul on 30/1/17.
 */

public class SettingRegionViewModel extends CommonViewModel {

    private Context context;
    private UserPresenter<SettingRegionMvpView> presenter;

    public final ObservableList<RegionWrapper> items = new ObservableArrayList<>();
    public final ItemView itemView = ItemView.of(BR.regionWrapper, R.layout.listview_region);

    public final ObservableList<RegionWrapper> cities = new ObservableArrayList<>();
    public final ItemView cityView = ItemView.of(BR.regionWrapper, R.layout.listview_region);

    private String selectedCountryStr;
    private String selectedCityStr;
    private User user;


    /**
     *
     * @param presenter
     * @param locationId {-1,locationId}  -1 refers to get all countries,
     *                   locationId refers to get cities where belongs to this locationId
     */
    public SettingRegionViewModel(UserPresenter<SettingRegionMvpView> presenter, long locationId) {
        this.presenter = presenter;
        this.context = presenter.getContext();
        user = UserUtil.getInstance(context).getUser();
        if (locationId == -1) {
            initCountries();
        }else{
            initCity(locationId);
            selectedCountryStr = DBManager.getInstance(context).findCountry(locationId).getName();
        }
    }

    private void initCity(long locationId){
        cities.clear();
        List<Region> cityList = DBManager.getInstance(context).getCityList(locationId);
        for (Region region: cityList){
            cities.add(new RegionWrapper(region));
        }
    }

    private void initCountries(){
        if (items.size() == 0) {
            for (Region region : DBManager.getInstance(context).getCountryList()) {
                items.add(new RegionWrapper(region));
            }
        }
    }

    public ListView.OnItemClickListener onClickCountry(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // the position needs to be position-1, because the
                RegionWrapper selectedWrapper = items.get(position-1);
                int currentSelection = selectedWrapper.getIsSelected();
                unselectAllWrapper(items);
                selectedWrapper.setSelected(currentSelection == 0 ? 1 : 0);
                if (presenter.getView()!=null && selectedWrapper.getIsSelected()==1){
                    presenter.getView().toSelectCity(selectedWrapper.getRegion().getLocationId());
                }
            }
        };
    }


    public ListView.OnItemClickListener onClickCity(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegionWrapper selectedWrapper = cities.get(position-1);
                int currentSelection = selectedWrapper.getIsSelected();
                unselectAllWrapper(cities);
                selectedWrapper.setSelected(currentSelection == 0 ? 1 : 0);
                if (selectedWrapper.getIsSelected()==1) {
                    selectedCityStr = selectedWrapper.getRegion().getName();
                }else{
                    selectedCityStr = "";
                }
                user.setLocation(selectedCountryStr + ", " + selectedCityStr);
            }
        };
    }

    public User getUser() {
        return user;
    }

    public String getSelectedCityStr() {
        return selectedCityStr;
    }

    public String getSelectedCountryStr() {
        return selectedCountryStr;
    }

    /**
     * this method is for counter-selecting all countries
     */
    private void unselectAllWrapper(List<RegionWrapper> wrapperList){
        for (RegionWrapper wrapper: wrapperList){
            wrapper.setSelected(0);
        }
    }


    public static class RegionWrapper extends BaseObservable{
        private int isSelected; // 0 = false, 1 = true
        private Region region;

        public RegionWrapper(Region region) {
            this.region = region;
            this.isSelected = 0;
        }

        @Bindable
        public int getIsSelected() {
            return isSelected;
        }


        public void setSelected(int selected) {
            isSelected = selected;
            notifyPropertyChanged(BR.isSelected);
        }

        @Bindable
        public Region getRegion() {
            return region;
        }

        public void setRegion(Region region) {
            this.region = region;
            notifyPropertyChanged(BR.region);
        }
    }
}
