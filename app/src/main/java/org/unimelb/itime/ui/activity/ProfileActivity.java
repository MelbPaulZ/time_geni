package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class ProfileActivity extends EmptyActivity {
    private FragmentManager fragmentManager;
    public static String USER_ID = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_contacts);
        fragmentManager = getSupportFragmentManager();
        ProfileFragment fragment = new ProfileFragment();
        String userId = getIntent().getStringExtra(USER_ID);
        if(userId!=null) {
            fragment.setUserId(userId);
        }
        fragment.setStartMode(ProfileFragment.MODE_CONTACT);
        fragmentManager.beginTransaction().replace(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }
}
