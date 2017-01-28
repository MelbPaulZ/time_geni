package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

import java.util.List;

/**
 * Created by xiaojiew1 on 26/01/17.
 */

public class TimeslotViewResponseViewModel extends BaseObservable {
    boolean showAccepted = true;
    boolean showRejected = true;
    boolean showNoResponse = true;

    int iconAccepted = R.drawable.icon_responses_accepted;
    int iconRejected = R.drawable.icon_responses_rejected;
    int iconNoResponse = R.drawable.icon_responses_noreply;

    List<String> inviteesAccepted;
    List<String> inviteesRejected;
    List<String> inviteesNoResponse;

    String timeRange;
    String dayOfWeek;

    String rightButtonText;

    public String getRightButtonText() {
        return rightButtonText;
    }

    public void setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getIconNoResponse() {
        return iconNoResponse;
    }

    public int getIconRejected() {
        return iconRejected;
    }

    public int getIconAccepted() {
        return iconAccepted;
    }

    @Bindable
    public boolean getShowAccepted() {
        return showAccepted;
    }

    public void setShowAccepted(boolean showAccepted) {
        this.showAccepted = showAccepted;
        notifyPropertyChanged(BR.showAccepted);
    }

    public boolean getShowRejected() {
        return showRejected;
    }

    public void setShowRejected(boolean showRejected) {
        this.showRejected = showRejected;
    }

    public boolean getShowNoResponse() {
        return showNoResponse;
    }

    public void setShowNoResponse(boolean showNoResponse) {
        this.showNoResponse = showNoResponse;
    }

    public List<String> getInviteesAccepted() {
        return inviteesAccepted;
    }

    public void setInviteesAccepted(List<String> inviteesAccepted) {
        this.inviteesAccepted = inviteesAccepted;
    }

    public List<String> getInviteesRejected() {
        return inviteesRejected;
    }

    public void setInviteesRejected(List<String> inviteesRejected) {
        this.inviteesRejected = inviteesRejected;
    }

    public List<String> getInviteesNoResponse() {
        return inviteesNoResponse;
    }

    public void setInviteesNoResponse(List<String> inviteesNoResponse) {
        this.inviteesNoResponse = inviteesNoResponse;
    }

    @BindingAdapter({"app:icon", "app:items"})
    public static void addItems(LinearLayout view, int icon, List<String> items) {
        Context context = view.getContext();
        for (String item:items) {
            LinearLayout child = new LinearLayout(context);
            child.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            child.setLayoutParams(linearLayoutParams);

            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                    SizeUtil.dp2px(context, 12.5), SizeUtil.dp2px(context, 10.0));
            imageViewParams.setMargins(SizeUtil.dp2px(context, 0.0), SizeUtil.dp2px(context, 0.0),
                    SizeUtil.dp2px(context, 6.5), SizeUtil.dp2px(context, 0.0));
            imageViewParams.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(imageViewParams);
            Picasso.with(context).load(icon).into(imageView);
            child.addView(imageView);

            TextView textView = new TextView(context);
            textView.setText(item);
            textView.setTextColor(context.getResources().getColor(R.color.text_gray));
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewParams.gravity = Gravity.CENTER_VERTICAL;
            textView.setLayoutParams(textViewParams);
            textView.setTextSize(14.0f);
            textView.setGravity(Gravity.LEFT);
            child.addView(textView);

            view.addView(child);
        }
    }

    @BindingAdapter("app:text")
    public static void setText(TextView view, String text) {
        view.setText(text);
    }
}
