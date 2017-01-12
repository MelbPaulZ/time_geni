package org.unimelb.itime.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;

/**
 * use for user authorization,
 * except for LoginFragment, all others should extend this class
 */
public abstract class BaseUiAuthFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {


    protected BaseActivity baseActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baseActivity = (BaseActivity)getActivity();
    }

    public BaseActivity getBaseActivity(){
        return baseActivity;
    }

    protected void showDialog(String title, String msg){
        TextView unsupportedEmailTitle = new TextView(getContext());
        unsupportedEmailTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        unsupportedEmailTitle.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        unsupportedEmailTitle.setText(title);
        unsupportedEmailTitle.setTextSize(18);
        unsupportedEmailTitle.setPadding(0,50,0,0);
        unsupportedEmailTitle.setTextColor(getResources().getColor(R.color.black));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(unsupportedEmailTitle)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
         builder.create().show();
    }
}
