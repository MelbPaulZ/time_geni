package org.unimelb.itime.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.ActivityContactMainBinding;
import org.unimelb.itime.databinding.FragmentPhotogridBinding;
import org.unimelb.itime.ui.fragment.event.EventPhotoFragment;
import org.unimelb.itime.util.DefaultPhotoUtil;
import org.unimelb.itime.util.googleAuth.GoogleSignUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import java.util.ArrayList;
import java.util.List;

public class ContactTestActivity extends EmptyActivity {
    ActivityContactMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_main);
        binding.setActivity(this);
//        binding.mainLayout.addView(DefaultPhotoUtil.getInstance().get(getApplicationContext(),"Qiushuo huang"));
//        binding.mainLayout.addView(DefaultPhotoUtil.getInstance().get(getApplicationContext(),"QIUSHUO"));
//        binding.mainLayout.addView(DefaultPhotoUtil.getInstance().get(getApplicationContext(),"q"));
//        binding.mainLayout.addView(DefaultPhotoUtil.getInstance().get(getApplicationContext(),"Qiushuo   huang"));
//        initView();
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }

    public void toHomePage() {
        Intent intent = new Intent();
        intent.setClass(this, BlockContactsActivity.class);
        startActivity(intent);
    }

    public void toInvitee() {
//        Intent intent = new Intent();
//        intent.setClass(this, InviteFriendsActivity.class);
//        startActivity(intent);
        new AlertDialog.Builder(this).setTitle("Title").setMessage("this is a message").setPositiveButton("确定", null)
        .setNegativeButton("取消", null).show();
    }

    public void toQRCode() {
        startActivityForResult(new Intent(this, PhotoGridTestActivity.class), 0);

    }

    public View.OnClickListener getGoogleAuth(){
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignUtil.getInstance().signIn(activity, 1234);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");

            }
        }
        if(resultCode == 1211){
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("authCode");
                System.out.println("get auth code:"+result);
            }
        }
    }

    public Bitmap getPhoto(){
       return DefaultPhotoUtil.getInstance().getPhoto(getApplicationContext(),"Qiushuo zuang");
    }

    //生成二维码 可以设置Logo
    public Bitmap make() {
        String input = "123456";
        //Bitmap qrCode = EncodingUtils.createQRCode(input, 500, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));//CheckBox选中就设置Logo
        //return qrCode;
        return null;
    }

}
