package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Presenter.MyQRCodePresenter;
import org.unimelb.itime.ui.contact.QRCode.encoding.EncodingUtils;
import org.unimelb.itime.ui.contact.Widget.ContactPopupWindow;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeVieModel extends BaseObservable {
    private MyQRCodePresenter presenter;
    private ITimeUser user;
    private ContactPopupWindow popupWindow;

    @Bindable
    public ITimeUser getUser() {
        return user;
    }

    public void setUser(ITimeUser user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
        notifyPropertyChanged(BR.myQRCode);
    }

    public MyQRCodeVieModel(MyQRCodePresenter presenter){
        this.presenter = presenter;
    }

    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackPress();
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
                popupWindow.showAtLocation(presenter.getView().getContentView(), Gravity.CENTER, 0, 0);
            }
        };
    }

    public void initPopupWindow(){
        popupWindow = new ContactPopupWindow(presenter.getView().getActivity());
        popupWindow.setFirstButtonText(
                presenter.getView().getActivity().getResources().
                        getText(R.string.save_to_album).toString());
        popupWindow.setFirstButtonLisenter(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setSecondButtonText(presenter.getView().getActivity().getResources().
                getText(R.string.scan_qr_code).toString());
        popupWindow.setSecondButtonLisenter( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getView().goToScanQRCode();
                popupWindow.dismiss();
            }
        });
    }

    @Bindable
    public Bitmap getMyQRCode(){
        return EncodingUtils.createQRCode(user.getContactId(), 1000, 1000, null);
    }


}
