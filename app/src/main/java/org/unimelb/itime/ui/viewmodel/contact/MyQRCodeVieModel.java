package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.contact.MyQRCodeMvpView;
import org.unimelb.itime.ui.presenter.contact.MyQRCodePresenter;
import org.unimelb.itime.widget.QRCode.encoding.EncodingUtils;
import org.unimelb.itime.widget.TimeGeniiPopupWindow;

import me.fesky.library.widget.ios.ActionSheetDialog;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeVieModel extends BaseObservable {
    private MyQRCodePresenter presenter;
    private ITimeUser user;
    private ActionSheetDialog popupWindow;
    private MyQRCodeMvpView mvpView;

    @Bindable
    public String getTitle(){
        return mvpView.getActivity().getResources().getString(R.string.my_qr_code_title);
    }

    @Bindable
    public boolean getShowGender(){
        if(user.getSex().equals(User.MALE)||user.getSex().equals(User.FEMALE)){
            return true;
        }
        return false;
    }

    @Bindable
    public ITimeUser getUser() {
        return user;
    }

    public void setUser(ITimeUser user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
        notifyPropertyChanged(BR.myQRCode);
        notifyPropertyChanged(BR.showGender);
    }

    public MyQRCodeVieModel(MyQRCodePresenter<MyQRCodeMvpView> presenter){
        this.presenter = presenter;
        mvpView = presenter.getView();
    }

    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.back();
                }
//                presenter.onBackPress();
            }
        };
    }

    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow==null){
                    initPopupWindow();
                }
                if (mvpView!=null) {
//                    popupWindow.showAtLocation(mvpView.getContentView(), Gravity.CENTER, 0, 0);
                    popupWindow.show();
                }
            }
        };
    }

    public void initPopupWindow(){
        if (mvpView!=null) {
            popupWindow = new ActionSheetDialog(mvpView.getActivity())
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(mvpView.getActivity().getResources().getText(R.string.save_to_album).toString(),
                            null,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    mvpView.saveQRCode();
                                }
                            })
                    .addSheetItem(mvpView.getActivity().getResources().getText(R.string.scan_qr_code).toString(),
                            null,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    mvpView.goToScanQRCode();
                                }
                            });
        }
    }

    @Bindable
    public Bitmap getMyQRCode(){
        return EncodingUtils.createQRCode(user.getEmail(), 1000, 1000, null);
    }

}
