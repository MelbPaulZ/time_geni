package org.unimelb.itime.ui.contact.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.InviteFriendsActivityBinding;
import org.unimelb.itime.ui.contact.Fragments.MyQRCodeFragment;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeActivity extends BaseActivity{
    InviteFriendsActivityBinding binding;
    FragmentManager fragmentManager;
    public static int SCAN_QR_CODE = 1;
    public static String PREVIEW = "preview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.invite_friends_activity);
        fragmentManager = getSupportFragmentManager();
        MyQRCodeFragment myQRCodeFragment = new MyQRCodeFragment();
        fragmentManager.beginTransaction().add(R.id.contentFrameLayout, myQRCodeFragment).commit();
        myQRCodeFragment.setPreview(getIntent().getIntExtra(MyQRCodeFragment.PREVIEW,0));
    }
}
