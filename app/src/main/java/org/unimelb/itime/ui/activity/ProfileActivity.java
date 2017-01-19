package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class ProfileActivity extends EmptyActivity {
    private FragmentManager fragmentManager;
    public static String USER = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.contact_home_activity);
        fragmentManager = getSupportFragmentManager();
        ProfileFragment fragment = new ProfileFragment();
        Contact user = (Contact) getIntent().getSerializableExtra(USER);
        fragment.setUser(user);
        fragmentManager.beginTransaction().replace(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }
}
