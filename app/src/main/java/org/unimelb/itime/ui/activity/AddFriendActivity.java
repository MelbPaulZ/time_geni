package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.contact.AddFriendsFragment;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class AddFriendActivity extends EmptyActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.contact_home_activity);
        fragmentManager = getSupportFragmentManager();
        AddFriendsFragment fragment = new AddFriendsFragment();
        fragmentManager.beginTransaction().replace(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }
}
