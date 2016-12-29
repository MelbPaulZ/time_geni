package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.ActivityInviteFriendsBinding;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;


/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteFriendsActivityContact extends ContactBaseActivity {
    ActivityInviteFriendsBinding binding;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friends);
        fragmentManager = getSupportFragmentManager();
        InviteeFragment inviteFriendsFragment = new InviteeFragment();
        inviteFriendsFragment.setEvent(new Event());
        fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, inviteFriendsFragment).commit();

    }

}
