package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.UserMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.util.AppUtil;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;



/**
 * Created by yinchuandong on 11/1/17.
 */

public class UserProfileViewModel extends CommonViewModel {

    public final static String TAG = "UserProfileViewModel";

    private UserMvpView mvpView;
    private UserPresenter<? extends TaskBasedMvpView<User>> presenter;
    private User user;

    private String password;
    private String passwordConfirmation;

    private List<GenderWrapper> genderWrapperList;
    private ItemView genderItemView;


    public UserProfileViewModel(UserPresenter<? extends TaskBasedMvpView<User>> presenter){
        this.presenter = presenter;
        if(presenter.getView() instanceof UserMvpView){
            this.mvpView = (UserMvpView) presenter.getView();
        }
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    public View.OnClickListener onEditNameDoneClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updateProfile(user);
            }
        };
    }

    public View.OnClickListener onAvatarClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditPhotoPage();
            }
        };
    }

    public View.OnClickListener onNameClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditNamePage();
            }
        };
    }

    public View.OnClickListener onResetPasswordClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditPasswordPage();
            }
        };
    }

    public View.OnClickListener onMyQrCodeClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditMyQrCodePage();
            }
        };
    }

    public View.OnClickListener onGenderClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditGenderPage();
            }
        };
    }

    public View.OnClickListener onRegionClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditRegionPage();
            }
        };
    }

    public AdapterView.OnItemClickListener onGenderItemClicked(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int k = 0; k < genderWrapperList.size(); k++){
                    genderWrapperList.get(k).setSelected(i == k);
                }
                user.setGender(genderWrapperList.get(i).gCode);
                presenter.updateProfile(user);
            }
        };
    }


    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
        notifyPropertyChanged(BR.passwordConfirmation);
    }

    @Bindable
    public List<GenderWrapper> getGenderWrapperList() {
        return genderWrapperList;
    }

    public void setGenderWrapperList(List<GenderWrapper> genderWrapperList) {
        this.genderWrapperList = genderWrapperList;
        notifyPropertyChanged(BR.genderWrapperList);
    }

    @Bindable
    public ItemView getGenderItemView() {
        return genderItemView;
    }

    public void setGenderItemView(ItemView genderItemView) {
        this.genderItemView = genderItemView;
        notifyPropertyChanged(BR.genderItemView);
    }

    public static class GenderWrapper extends BaseObservable{
        private String name;
        private String gCode = "2";
        private boolean selected;

        public GenderWrapper(String gCode, boolean selected){
            this.gCode = gCode;
            this.selected = selected;
            this.name = AppUtil.getGenderStr(gCode);
        }

        public String getgCode() {
            return gCode;
        }

        @Bindable
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
            notifyPropertyChanged(BR.name);
        }

        @Bindable
        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            notifyPropertyChanged(BR.selected);
        }
    }


}
