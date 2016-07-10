package unimelb.org.itime.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import unimelb.org.itime.base.BaseActivity;
import unimelb.org.main.R;

public class MainActivity extends BaseActivity{

    public final static String TAG = "MainActivity";


    @ViewInject(id = R.id.main_hello, click = "onHelloClick")
    TextView helloTv;

    @ViewInject(id = R.id.main_tab_calendar_tv, click = "onMainTabClick")
    TextView tabCalendarTv;
    @ViewInject(id = R.id.main_tab_contact_tv, click = "onMainTabClick")
    TextView tabContactTv;
    @ViewInject(id = R.id.main_tab_inbox_tv, click = "onMainTabClick")
    TextView tabInboxTv;
    @ViewInject(id = R.id.main_tab_setting_tv, click = "onMainTabClick")
    TextView tabSettingTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, helloTv.getText().toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * will be automatically called by view injection
     * @param v
     */
    public void onHelloClick(View v){
        Log.d(TAG, "onHelloClick:" + v.getId());
        EventBus.getDefault().post("hello, everyone");
        EventBus.getDefault().post(123345);
    }

    public void onMainTabClick(View v){
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
