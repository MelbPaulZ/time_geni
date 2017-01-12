package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.UserMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;

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

    private ObservableList<GenderWrapper> genderWrapperList;
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
                genderWrapperList.get(i).isSelected = true;
                notifyPropertyChanged(BR.genderWrapperList);
                notifyPropertyChanged(BR.genderItemView);
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
    public ObservableList<GenderWrapper> getGenderWrapperList() {
        return genderWrapperList;
    }

    public void setGenderWrapperList(ObservableList<GenderWrapper> genderWrapperList) {
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

    public static class GenderWrapper{
        public String name;
        public boolean isSelected;

        public GenderWrapper(String name, boolean isSelected){
            this.name = name;
            this.isSelected = isSelected;
        }
    }


}
