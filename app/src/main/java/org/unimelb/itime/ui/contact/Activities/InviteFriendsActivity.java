package org.unimelb.itime.ui.contact.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.InviteFriendsActivityBinding;
import org.unimelb.itime.ui.contact.Fragments.InviteFriendsFragment;


/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteFriendsActivity extends BaseActivity {
    InviteFriendsActivityBinding binding;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.invite_friends_activity);
        fragmentManager = getSupportFragmentManager();
        InviteFriendsFragment inviteFriendsFragment = new InviteFriendsFragment();
        fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, inviteFriendsFragment).commit();

    }

}
