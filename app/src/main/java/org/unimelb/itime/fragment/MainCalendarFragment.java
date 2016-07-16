package org.unimelb.itime.fragment;


import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends BaseUiAuthFragment{


    public MainCalendarFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }


}
