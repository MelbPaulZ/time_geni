package org.unimelb.itime.ui.fragment;


import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainInboxFragment extends BaseUiAuthFragment{


    public MainInboxFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }


}
