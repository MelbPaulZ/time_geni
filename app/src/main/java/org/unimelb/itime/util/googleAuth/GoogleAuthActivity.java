package org.unimelb.itime.util.googleAuth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.RequestReadBody;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.restfulapi.BindApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.activity.EmptyActivity;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Qiushuo Huang on 2017/1/2.
 */

public class GoogleAuthActivity extends EmptyActivity{
    public static final int RESULT_SUCCESS = 1211;
    public static final int RESULT_FAILED = 1212;

    private static final String TAG = "Google Auth";
    private static final int RC_SIGN_IN = 9001;
    private BindApi bindApi;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindApi = HttpUtil.createService(getApplicationContext(), BindApi.class);
        Scope s1 = new Scope("https://apps-apis.google.com/a/feeds/groups/");
        Scope s2 = new Scope("https://apps-apis.google.com/a/feeds/alias/");
        Scope s3 = new Scope("https://apps-apis.google.com/a/feeds/user/");
        Scope s4 = new Scope("https://www.google.com/m8/feeds/");
        Scope s5 = new Scope("https://www.google.com/m8/feeds/user/");
        Scope s6 = new Scope("https://www.googleapis.com/auth/userinfo.profile");
        Scope s7 = new Scope("https://www.googleapis.com/auth/calendar");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.serverClientId))
                .requestScopes(s1, s2, s3,s4,s5,s6,s7)
                .requestServerAuthCode(getString(R.string.serverClientId),false)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, getFailedListener())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "email:" + acct.getEmail());
            Log.d(TAG, "authcode:" + acct.getServerAuthCode());
            bind(acct.getServerAuthCode());
        } else {
            setResult(GoogleSignUtil.RESULT_FAILED,getIntent());
            finish();
        }

    }

    private void bind(String authCode){
        Observable<HttpResult<Void>> observable = bindApi.bindGoogle(authCode);
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
                GoogleAuthActivity.this.finish();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Void> result) {
                Log.d(TAG, "onNext: " + result.getInfo()+"   status:"+result.getStatus());
                Intent intent = new Intent();
                intent.putExtra("authCode", result.getInfo());
                if (result.getStatus()==1){
                    GoogleAuthActivity.this.setResult(RESULT_SUCCESS,intent);
                }else {
                    GoogleAuthActivity.this.setResult(RESULT_FAILED,intent);
                }}
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private GoogleApiClient.OnConnectionFailedListener getFailedListener(){
        return new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getApplicationContext(),connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }
}
