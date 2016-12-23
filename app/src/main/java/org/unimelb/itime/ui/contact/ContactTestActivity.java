package org.unimelb.itime.ui.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.MainActivityBinding;
import org.unimelb.itime.ui.contact.Activities.BaseActivity;
import org.unimelb.itime.ui.contact.Activities.ContactHomePageActivity;
import org.unimelb.itime.ui.contact.Activities.InviteFriendsActivity;
import org.unimelb.itime.ui.contact.QRCode.CaptureActivity;

public class ContactTestActivity extends BaseActivity {
    MainActivityBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setActivity(this);
//        initView();
    }

    public void toHomePage() {
        Intent intent = new Intent();
        intent.setClass(this, ContactHomePageActivity.class);
        startActivity(intent);
    }

    public void toInvitee() {
        Intent intent = new Intent();
        intent.setClass(this, InviteFriendsActivity.class);
        startActivity(intent);
    }

    public void toQRCode() {
        startActivityForResult(new Intent(this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                System.out.println(result);
            }
        }
    }

    //生成二维码 可以设置Logo
    public Bitmap make() {
        String input = "123456";
        //Bitmap qrCode = EncodingUtils.createQRCode(input, 500, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));//CheckBox选中就设置Logo
        //return qrCode;
        return null;
    }

}
