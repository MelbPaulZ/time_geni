package org.unimelb.itime.ui.contact.Fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.MyQrCodeBinding;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Dao.InviteeDao;
import org.unimelb.itime.ui.contact.MvpView.MyQRCodeMvpView;
import org.unimelb.itime.ui.contact.Presenter.MyQRCodePresenter;
import org.unimelb.itime.ui.contact.QRCode.CaptureActivity;
import org.unimelb.itime.ui.contact.ViewModel.MyQRCodeVieModel;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeFragment extends MvpFragment<MyQRCodeMvpView, MyQRCodePresenter> implements MyQRCodeMvpView {

    public static int SCAN_QR_CODE = 1;
    public static String PREVIEW = "preview";

    private MyQrCodeBinding binding;
    private MyQRCodeVieModel viewModel;
    private int preview;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.my_qr_code, container, false);
        presenter = createPresenter();
        viewModel = new MyQRCodeVieModel(presenter);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    public void setPreview(int preview){
        this.preview = preview;
    }

    public void onStart(){
        super.onStart();
        ITimeUser user = new InviteeDao().getITimeFriends().get(0);
        viewModel.setUser(user);
    }

    @Override
    public MyQRCodePresenter createPresenter() {
        return new MyQRCodePresenter();
    }

    @Override
    public View getContentView() {
        return getView();
    }

    @Override
    public void goToScanQRCode() {
        if(preview == SCAN_QR_CODE){
            getActivity().onBackPressed();
        }else{
            Intent intent = new Intent();
            intent.setClass(getActivity(), CaptureActivity.class);
            intent.putExtra(CaptureActivity.PREVIEW, CaptureActivity.MY_QR_CODE);
            startActivity(intent);
        }
    }
}
