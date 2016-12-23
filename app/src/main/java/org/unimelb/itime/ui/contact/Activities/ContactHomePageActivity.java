package org.unimelb.itime.ui.contact.Activities;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Fragments.ContactHomePageFragment;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ContactHomePageActivity extends BaseActivity {

        FragmentManager fragmentManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            DataBindingUtil.setContentView(this, R.layout.contact_home_activity);
            fragmentManager = getSupportFragmentManager();
            ContactHomePageFragment home = new ContactHomePageFragment();
            fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, home).commit();
        }
}
