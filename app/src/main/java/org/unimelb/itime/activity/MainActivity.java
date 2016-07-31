package org.unimelb.itime.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.fragment.MainCalendarFragment;
import org.unimelb.itime.fragment.MainContactsFragment;
import org.unimelb.itime.fragment.MainInboxFragment;
import org.unimelb.itime.fragment.MainSettingsFragment;

import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private BaseUiFragment[] tagFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
    }

    private void init(){
        tagFragments = new BaseUiFragment[4];
        tagFragments[0] = new MainCalendarFragment();
        tagFragments[1] = new MainContactsFragment();
        tagFragments[2] = new MainInboxFragment();
        tagFragments[3] = new MainSettingsFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[0]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[1]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[2]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[3]);
        fragmentTransaction.commit();
        showMainFragments(0);
    }

    private void showMainFragments(int pageId){
        fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < tagFragments.length; i++){
            if (pageId == i){
                fragmentTransaction.show(tagFragments[i]);
            }else{
                fragmentTransaction.hide(tagFragments[i]);
            }
        }
        fragmentTransaction.commit();
    }

    @OnClick({R.id.main_tab_calendar_tv, R.id.main_tab_contact_tv,
            R.id.main_tab_inbox_tv, R.id.main_tab_setting_tv
    })
    void onMainTabClick(View v){
        TextView tab = (TextView) v;
        Log.d(TAG, "onMainTabClick:" + tab.getText().toString());
        switch (tab.getId()) {
            case R.id.main_tab_calendar_tv:
                showMainFragments(0);
                break;
            case R.id.main_fragment_container:
                showMainFragments(1);
                break;
            case R.id.main_tab_inbox_tv:
                showMainFragments(2);
                break;
            case R.id.main_tab_setting_tv:
                showMainFragments(3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
