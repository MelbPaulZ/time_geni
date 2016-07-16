package org.unimelb.itime.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TestActivity extends BaseActivity{

    private final static String TAG = "TestActivity";

    private Unbinder butterKnifeUnbinder;

    @BindView(R.id.main_hello_tv) TextView helloTv;
    TextView tabCalendarTv;
    TextView tabContactTv;
    TextView tabInboxTv;
    TextView tabSettingTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        butterKnifeUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Log.d(TAG, helloTv.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        butterKnifeUnbinder.unbind();
    }

    /**
     * will be automatically called by view injection
     * @param v
     */
    @OnClick(R.id.main_hello_tv)
    void onHelloClick(View v){
        Log.d(TAG, "onHelloClick:" + v.getId());
        EventBus.getDefault().post("hello, everyone");
        EventBus.getDefault().post(123345);
    }

    @OnClick({R.id.main_tab_calendar_tv, R.id.main_tab_contact_tv,
            R.id.main_tab_inbox_tv, R.id.main_tab_setting_tv
    })
    void onMainTabClick(View v){
        TextView tab = (TextView) v;
        Log.d(TAG, "onMainTabClick:" + tab.getText().toString());
    }


    // demo for event bus
    @Subscribe
    public void onMsgReceive(String text){
        Log.d(TAG, "onMsgReceive:" + text);
    }

    @Subscribe
    public void onMsgReceive(Integer number){
        Log.d(TAG, "onMsgReceive222:" + number);
    }
}
