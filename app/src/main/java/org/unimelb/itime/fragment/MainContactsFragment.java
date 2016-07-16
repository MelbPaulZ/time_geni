package org.unimelb.itime.fragment;


import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainContactsFragment extends BaseUiAuthFragment{


    public MainContactsFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }


}
