package org.unimelb.itime.base;

import android.os.Bundle;

/**
 * use for user authorization,
 * except for LoginFragment, all others should extend this class
 */
public abstract class BaseUiAuthFragment extends BaseUiFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
