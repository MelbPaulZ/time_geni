package org.unimelb.itime.ui.activity;

import android.app.Fragment;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.databinding.EventTestBinding;
import org.unimelb.itime.ui.fragment.EventDetailFragment;
import org.unimelb.itime.ui.fragment.EventEditFragment;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.TestEvent;

/**
 * Created by Paul on 23/08/2016.
 */
public class TestActivityPaul extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paul);

//        EventEditFragment eventEditFragment = new EventEditFragment();
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        getFragmentManager().beginTransaction().add(R.id.test_paul_fragment,eventDetailFragment).commit();

    }

}
