package org.unimelb.itime.ui.activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.MainContactFragment;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ContactHomePageActivity extends EmptyActivity {

        FragmentManager fragmentManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            DataBindingUtil.setContentView(this, R.layout.activity_contacts);
            fragmentManager = getSupportFragmentManager();
            MainContactFragment home = new MainContactFragment();
            fragmentManager.beginTransaction().replace(getFragmentContainerId(), home).commit();
        }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }
}
