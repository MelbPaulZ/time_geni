package org.unimelb.itime.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * use afinal: https://github.com/yangfuhai/afinal
 */
public class BaseActivity extends AppCompatActivity{
    public final static String TASK = "task";
    public final static int TASK_INVITE_OTHER_CREATE_EVENT = 1;
    public final static int TASK_SELF_CREATE_EVENT  = 2;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
