package org.unimelb.itime.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import net.tsz.afinal.FinalActivity;

/**
 * use afinal: https://github.com/yangfuhai/afinal
 */
public class BaseActivity extends FinalActivity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
