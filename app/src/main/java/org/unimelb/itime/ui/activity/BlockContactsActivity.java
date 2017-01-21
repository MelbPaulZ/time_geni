package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.settings.SettingBlockContactsFragment;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class BlockContactsActivity extends EmptyActivity{

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_contacts);
        fragmentManager = getSupportFragmentManager();
        SettingBlockContactsFragment fragment = new SettingBlockContactsFragment();
        fragmentManager.beginTransaction().replace(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }
}
