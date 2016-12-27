package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;


/**
 * Created by 37925 on 2016/12/20.
 */

public class QRCodeScanViewModel extends BaseObservable {
    String title;
    CaptureActivityContact activity;

    public QRCodeScanViewModel(CaptureActivityContact activity){
        this.activity = activity;
    }

    @Bindable
    public String getTitle(){
        return activity.getString(R.string.qr_code);
    }

    @Bindable
    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        };
    }

    @Bindable
    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    @Bindable
    public View.OnClickListener getMyQRCodeListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goToMyQRCode();
            }
        };
    }
}
