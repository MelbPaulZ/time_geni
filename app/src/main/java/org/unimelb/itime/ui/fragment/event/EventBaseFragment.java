package org.unimelb.itime.ui.fragment.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.contact.BaseContactFragment;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

import static com.wx.wheelview.widget.WheelView.Skin.Common;
import static org.unimelb.itime.R.id.dialog;

/**
 * provide some common methods and initialise parameters
 */
public abstract class EventBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends BaseUiFragment<Event, V, P> implements EventCommonMvpView{

    private AlertDialog dialog;
    protected ToolbarViewModel<?  extends ItimeCommonMvpView> toolbarViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarViewModel = getToolbarViewModel();
        setLeftTitleStringToVM();
        setTitleStringToVM();
        setRightTitleStringToVM();
    }

    @Override
    public void setData(Event event) {

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
        dialog = builder.create();
        dialog.show();
    }

    public abstract void setLeftTitleStringToVM();
    public abstract void setTitleStringToVM();
    public abstract void setRightTitleStringToVM();
    public abstract ToolbarViewModel<? extends ItimeCommonMvpView> getToolbarViewModel();
}
