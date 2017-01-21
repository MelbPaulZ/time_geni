package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ActivityInviteFriendsBinding;
import org.unimelb.itime.ui.fragment.contact.MyQRCodeFragment;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeActivity extends EmptyActivity {
    ActivityInviteFriendsBinding binding;
    FragmentManager fragmentManager;
    public static int SCAN_QR_CODE = 1;
    public static String PREVIEW = "preview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friends);
        fragmentManager = getSupportFragmentManager();
        MyQRCodeFragment myQRCodeFragment = new MyQRCodeFragment();
//        fragmentManager.beginTransaction().add(R.id.setting_activity_framelayout, myQRCodeFragment).commit();
        fragmentManager.beginTransaction().replace(getFragmentContainerId(), myQRCodeFragment).commit();
        myQRCodeFragment.setPreview(getIntent().getIntExtra(MyQRCodeFragment.PREVIEW,0));
    }
}
