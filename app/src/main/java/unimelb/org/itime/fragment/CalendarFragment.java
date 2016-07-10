package unimelb.org.itime.fragment;


import unimelb.org.itime.base.BaseUiAuthFragment;
import unimelb.org.main.R;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class CalendarFragment extends BaseUiAuthFragment{


    public CalendarFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }


}
