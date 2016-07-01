package unimelb.org.itime.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import unimelb.org.itime.base.BaseActivity;
import unimelb.org.main.R;

public class LoginActivity extends BaseActivity{

    public final static String TAG = "LoginActivity";


    @ViewInject(id = R.id.main_hello, click = "onHelloClick")
    TextView helloView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, helloView.getText().toString());

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
