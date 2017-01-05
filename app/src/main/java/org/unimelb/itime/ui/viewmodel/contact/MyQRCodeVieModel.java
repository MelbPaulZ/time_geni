package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.contact.MyQRCodeMvpView;
import org.unimelb.itime.ui.presenter.contact.MyQRCodePresenter;
import org.unimelb.itime.widget.QRCode.encoding.EncodingUtils;
import org.unimelb.itime.widget.ContactPopupWindow;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeVieModel extends BaseObservable {
    private MyQRCodePresenter presenter;
    private ITimeUser user;
    private ContactPopupWindow popupWindow;
    private MyQRCodeMvpView mvpView;

    @Bindable
    public String getTitle(){
        return mvpView.getActivity().getResources().getString(R.string.my_qr_code_title);
    }

    @Bindable
    public ITimeUser getUser() {
        return user;
    }

    public void setUser(ITimeUser user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
        notifyPropertyChanged(BR.myQRCode);
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
                    popupWindow.showAtLocation(mvpView.getContentView(), Gravity.CENTER, 0, 0);
                }
            }
        };
    }

    public void initPopupWindow(){
        if (mvpView!=null) {
            popupWindow = new ContactPopupWindow(mvpView.getActivity());
            popupWindow.setFirstButtonText(
                    mvpView.getActivity().getResources().
                            getText(R.string.save_to_album).toString());
            popupWindow.setFirstButtonLisenter(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mvpView.saveQRCode();
                }
            });

            popupWindow.setSecondButtonText(mvpView.getActivity().getResources().
                    getText(R.string.scan_qr_code).toString());
            popupWindow.setSecondButtonLisenter(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mvpView.goToScanQRCode();
                    popupWindow.dismiss();
                }
            });
        }
    }

    @Bindable
    public Bitmap getMyQRCode(){
        return EncodingUtils.createQRCode(user.getEmail(), 1000, 1000, null);
    }

}
