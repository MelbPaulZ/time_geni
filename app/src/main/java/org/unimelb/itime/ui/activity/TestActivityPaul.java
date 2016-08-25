package org.unimelb.itime.ui.activity;

import android.app.usage.UsageEvents;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.databinding.EventTestBinding;
import org.unimelb.itime.ui.fragment.TestEvent;

/**
 * Created by Paul on 23/08/2016.
 */
public class TestActivityPaul extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventTestBinding binding = DataBindingUtil.setContentView(this, R.layout.event_test);
        TestEvent testEvent = new TestEvent("itime","melbourne");
        binding.setTestEvent(testEvent);

    }
}
