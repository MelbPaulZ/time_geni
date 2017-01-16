package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.ui.fragment.contact.BlockContactsFragment;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class BlockContactsActivity extends BaseActivity{

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.contact_home_activity);
        fragmentManager = getSupportFragmentManager();
        BlockContactsFragment fragment = new BlockContactsFragment();
        fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, fragment).commit();
    }
}
