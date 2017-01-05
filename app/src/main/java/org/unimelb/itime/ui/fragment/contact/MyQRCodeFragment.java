package org.unimelb.itime.ui.fragment.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentMyQrCodeBinding;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileFragment;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;
import org.unimelb.itime.ui.mvpview.contact.MyQRCodeMvpView;
import org.unimelb.itime.ui.presenter.contact.MyQRCodePresenter;
import org.unimelb.itime.ui.viewmodel.contact.MyQRCodeVieModel;
import org.unimelb.itime.util.UserUtil;

import java.io.File;
import java.io.FileOutputStream;

import static org.unimelb.itime.ui.presenter.contact.ContextPresenter.getContext;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeFragment extends BaseUiFragment<Object,MyQRCodeMvpView, MyQRCodePresenter<MyQRCodeMvpView>> implements MyQRCodeMvpView {

    public static int SCAN_QR_CODE = 1;
    public static String PREVIEW = "preview";

    private FragmentMyQrCodeBinding binding;
    private MyQRCodeVieModel viewModel;
    private int preview;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_my_qr_code, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new MyQRCodeVieModel(presenter);
        binding.setViewModel(viewModel);
    }

    public void setPreview(int preview){
        this.preview = preview;
    }

    public void onStart(){
        super.onStart();
        ITimeUser user = new ITimeUser(UserUtil.getInstance(getContext()).getUser());
        viewModel.setUser(user);
    }

    public void saveQRCode(){
        View view = binding.QRCodeView;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if(bitmap != null) {
            try{
                File extDir = Environment.getExternalStorageDirectory();
                String filename = "qr_" + System.currentTimeMillis()+".png";
                File folder = new File(extDir, "Timegenii");
                if(!folder.exists()){
                    folder.mkdir();
                }
                File file = new File(folder.getPath(), filename);
                if(!file.exists()) {
                    file.createNewFile();
                }
                file.setWritable(Boolean.TRUE);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100, out);
                Toast.makeText(getContext(), getString(R.string.save_qr_code_success), Toast.LENGTH_SHORT).show();
            }catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), getString(R.string.save_qr_code_failed), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), getString(R.string.save_qr_code_failed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void back() {
        if (getFrom() instanceof SettingMyProfileFragment){
            closeFragment(this, (SettingMyProfileFragment)getFrom());
        }else{
            getActivity().onBackPressed();
        }
    }

    @Override
    public MyQRCodePresenter<MyQRCodeMvpView> createPresenter() {
        return new MyQRCodePresenter<>();
    }

    @Override
    public View getContentView() {
        return getView();
    }

    @Override
    public void goToScanQRCode() {
        if(preview == SCAN_QR_CODE){
            getActivity().onBackPressed();
//            closeFragment(this, (SettingMyProfileFragment)getFrom());
        }else{
            Intent intent = new Intent();
            intent.setClass(getActivity(), CaptureActivityContact.class);
            intent.putExtra(CaptureActivityContact.PREVIEW, CaptureActivityContact.MY_QR_CODE);
            startActivity(intent);
        }
    }


    @Override
    public void setData(Object o) {

    }
}
