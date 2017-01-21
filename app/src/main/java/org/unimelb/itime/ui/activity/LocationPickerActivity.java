package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.LocationPickerFragment;


/**
 * Created by Paul on 19/1/17.
 */

public class LocationPickerActivity extends EmptyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        LocationPickerFragment fragment = new LocationPickerFragment();
        fragment.setPlace(location);
        getSupportFragmentManager().beginTransaction().replace(R.id.location_picker_framelayout, fragment).commit();
    }
}
