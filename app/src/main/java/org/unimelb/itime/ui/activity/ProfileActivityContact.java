package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class ProfileActivityContact extends ContactBaseActivity {
    private FragmentManager fragmentManager;
    public static String USER = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.contact_home_activity);
        fragmentManager = getSupportFragmentManager();
        ProfileFragment fragment = new ProfileFragment();
        ITimeUser user = (ITimeUser) getIntent().getSerializableExtra(USER);
        fragment.setUser(user);
        fragment.setShowRightButton(true);
        fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, fragment).addToBackStack(null).commit();
    }
}
