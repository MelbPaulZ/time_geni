package unimelb.org.itime.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unimelb.org.itime.base.BaseUiFragment;
import unimelb.org.main.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends BaseUiFragment{

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}
