package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Activities.BaseActivity;
import org.unimelb.itime.ui.contact.Fragments.AddFriendsFragment;
import org.unimelb.itime.ui.contact.Fragments.NewFriendFragment;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class AddFriendActivity extends BaseActivity{
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.contact_home_activity);
        fragmentManager = getSupportFragmentManager();
        AddFriendsFragment fragment = new AddFriendsFragment();
        fragmentManager.beginTransaction().add(R.id.contentFrameLayout, fragment).commit();
    }
}
