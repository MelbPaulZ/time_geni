package org.unimelb.itime.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.databinding.ActivityContactMainBinding;
import org.unimelb.itime.util.DefaultPhotoUtil;
import org.unimelb.itime.util.googleAuth.GoogleSignUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

public class ContactTestActivityContact extends BaseActivity {
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

    public void toHomePage() {
        Intent intent = new Intent();
        intent.setClass(this, ContactHomePageActivityContact.class);
        startActivity(intent);
    }

    public void toInvitee() {
        Intent intent = new Intent();
        intent.setClass(this, InviteFriendsActivityContact.class);
        startActivity(intent);
    }

    public void toQRCode() {
        startActivityForResult(new Intent(this, CaptureActivityContact.class), 0);
    }

    public View.OnClickListener getGoogleAuth(){
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignUtil.getInstance().signIn(activity);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");

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
