package unimelb.org.itime.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import unimelb.org.itime.base.BaseUiAuthFragment;
import unimelb.org.main.R;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class CalendarFragment extends BaseUiAuthFragment{


    public CalendarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }


}
