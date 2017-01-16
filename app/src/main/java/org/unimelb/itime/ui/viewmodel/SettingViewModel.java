package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.presenter.SettingPresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class SettingViewModel extends CommonViewModel{
    private SettingPresenter presenter;
    private List<AlertWrapper> alertSet = new ArrayList<>();
    private ItemView alertItemView;
    private User user;
    private Setting setting;

    public SettingViewModel(SettingPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AdapterView.OnItemClickListener onAlertItemClicked(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < alertSet.size(); j++) {
                    alertSet.get(j).setSelected(j==i);
                }
                // TODO: 12/01/2017 update server and local
                setting.setDefaultAlertTime(alertSet.get(i).key);
                presenter.update(setting);
            }
        };
    }

    @Bindable
    public List<AlertWrapper> getAlertSet() {
        return alertSet;
    }

    public void setAlertSet(List<AlertWrapper> alertSet) {
        this.alertSet = alertSet;
        for (AlertWrapper wrapper:alertSet
             ) {
            wrapper.setSelected(wrapper.key == setting.getDefaultAlertTime());
        }
        notifyPropertyChanged(BR.alertSet);
    }

    @Bindable
    public ItemView getAlertItemView() {
        return alertItemView;
    }

    public void setAlertItemView(ItemView alertItemView) {
        this.alertItemView = alertItemView;
        notifyPropertyChanged(BR.alertItemView);
    }

    public static class AlertWrapper extends BaseObservable {
        boolean isSelected;
        String name;
        int key;

        public AlertWrapper(boolean isSelected, String name, int key) {
            this.isSelected = isSelected;
            this.name = name;
            this.key = key;
        }

        @Bindable
        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
            notifyPropertyChanged(BR.selected);
        }

        @Bindable
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
            notifyPropertyChanged(BR.name);
        }
    }

    /**
     * For Notification Setting
     */
    public Switch.OnCheckedChangeListener onShowPreviewTextChange(){
        return new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setShowPreviewText(isChecked);
                notifyPropertyChanged(BR.setting);
                presenter.update(setting);
            }
        };
    }

    public Switch.OnCheckedChangeListener onInAppAlertSoundChange(){
        return new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setAppAlertSound(isChecked);
                notifyPropertyChanged(BR.setting);
                presenter.update(setting);
            }
        };
    }

    public Switch.OnCheckedChangeListener onVibrateChange(){
        return new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setSystemVibrate(isChecked);
                notifyPropertyChanged(BR.setting);
                presenter.update(setting);
            }
        };
    }
}

