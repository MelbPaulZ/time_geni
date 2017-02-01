package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.util.SizeUtil;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 37925 on test/11/29.
 */

public class InviteeGroupView extends LinearLayout {

    private int AVATAR_WIDTH_DEFAULT = dp2px(44);
    private int AVATAR_HEIGHT_DEFAULT = dp2px(44);
    private int SQUARE_HEIGHT_DEFAULT = 0;
    private List<? extends ITimeInviteeInterface> inviteeList;
//    private final static int PHONE_COLOR_DEFAULT = Color.rgb(255,128,0);
//    private final static int EMAIL_COLOR_DEFAULT = Color.rgb(50,205,50);
    private FlowLayout avatarFlowLayout;
    private FlowLayout textFlowLayout;
    private PureEditText inputEditText;

    private float INPUT_FONT_SIZE = px2dp(getContext().getResources().getDimension(R.dimen.font_large));
    private float ITEM_FONT_SIZE = px2dp(getContext().getResources().getDimension(R.dimen.font_small));
    private int avatarWidth;
    private int avatarHeight;
    private int squareHeight;
    private int phoneColor = R.color.lightBlue;
    private int emailColor = R.color.lightBlue;
    private OnItemClickListener onInviteeClickListener;
    private OnEditListener onEditListener;
    private Map<String, View> inviteeMap;
    private OnClickListener onAddClickListener;
    private boolean addable = false;
    private TextView addPlusIcon;

    public OnClickListener getOnAddClickListener() {
        return onAddClickListener;
    }

    public void setOnAddClickListener(OnClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public void setAddable(boolean addable){
        if(addable){
            addPlusIcon.setBackground(getResources().getDrawable(R.drawable.icon_addinvitee_plus_clickable));
        }else{
            addPlusIcon.setBackground(getResources().getDrawable(R.drawable.icon_addinvitee_plus_disabled));
        }
    }

    public InviteeGroupView(Context context, AttributeSet arrs) {
        super(context, arrs);
        TypedArray array = context.obtainStyledAttributes(arrs, R.styleable.InviteeGroupView);
        avatarWidth = array.getDimensionPixelSize(R.styleable.InviteeGroupView_avatarWeight,
                dp2px(AVATAR_WIDTH_DEFAULT));
        avatarHeight = array.getDimensionPixelSize(R.styleable.InviteeGroupView_avatarHeight,
                dp2px(AVATAR_HEIGHT_DEFAULT));
        squareHeight = array.getDimensionPixelSize(R.styleable.InviteeGroupView_squareHeight,
                dp2px(SQUARE_HEIGHT_DEFAULT));
        String hint = array.getString(R.styleable.InviteeGroupView_hintText);

//        phoneColor = array.getColor(R.styleable.PeopleGroupView_phoneColor, PHONE_COLOR_DEFAULT);
//        emailColor = array.getColor(R.styleable.PeopleGroupView_emailColor, EMAIL_COLOR_DEFAULT);
        this.setOrientation(LinearLayout.VERTICAL);
//        this.setPadding(SizeUtil.dp2px(getContext(), 14), SizeUtil.dp2px(getContext(), 20),
//                SizeUtil.dp2px(getContext(), 14), SizeUtil.dp2px(getContext(), 20));
        initAvatarFlowLayout();
        initTextFlowLayout();
        initInputEditText(hint);
        inviteeMap = new HashMap<>();
    }

    public void clearViews(){
        avatarFlowLayout.removeAllViews();
        textFlowLayout.removeAllViews();
        clearInput();
    }

    private void hideFlowLayouts(){
        avatarFlowLayout.setVisibility(GONE);
        textFlowLayout.setVisibility(GONE);
    }

    private void showFlowLayouts(){
        avatarFlowLayout.setVisibility(VISIBLE);
        textFlowLayout.setVisibility(VISIBLE);
    }


    public void setInviteeList(List<? extends ITimeInviteeInterface> inviteeList) {
        if(inviteeList!=null) {
            if(this.inviteeList==null){
                this.inviteeList = inviteeList;
            }
            if(inviteeList.isEmpty()){
                hideFlowLayouts();
            }else{
                showFlowLayouts();
            }
            clearViews();
            for (ITimeInviteeInterface invitee : inviteeList) {
                addInvitee(invitee);
            }
        }else{
            hideFlowLayouts();
        }
    }

    private void initAvatarFlowLayout (){
        avatarFlowLayout = new FlowLayout(this.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, SizeUtil.dp2px(getContext(), 10));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        avatarFlowLayout.setLayoutParams(params);
        avatarFlowLayout.setSpace(6,14);
        this.addView(avatarFlowLayout);
    }

    private void initTextFlowLayout(){
        textFlowLayout = new FlowLayout(this.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 0, 0, SizeUtil.dp2px(getContext(), 10));
        textFlowLayout.setLayoutParams(params);
        textFlowLayout.setSpace(24, 14);
        this.addView(textFlowLayout);
    }

    private void initInputEditText(String hint){
        LinearLayout inputLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        inputLayout.setLayoutParams(layoutParams);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);

        inputEditText = new PureEditText(this.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        inputEditText.setLayoutParams(params);
        inputEditText.setGravity(Gravity.TOP);
        inputEditText.setSingleLine(true);
        inputEditText.setHintTextColor(getResources().getColor(R.color.normalGrey));
        inputEditText.setTextSize(INPUT_FONT_SIZE);
            if(hint!=null){
                setHintText(hint);
        }
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(onEditListener!=null) {
                    onEditListener.onEditing(inputEditText, inputEditText.getText().toString());
                    if(inputEditText.getText().toString().isEmpty()){
                        addPlusIcon.setVisibility(GONE);
                    }else{
                        addPlusIcon.setVisibility(VISIBLE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputLayout.addView(inputEditText);

        addPlusIcon = new TextView(getContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(SizeUtil.dp2px(getContext(), 27.5),
                SizeUtil.dp2px(getContext(), 27.5));
        iconParams.setMargins(SizeUtil.dp2px(getContext(), 10), 0, 0, 0);
        addPlusIcon.setLayoutParams(iconParams);
        addPlusIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClickListener.onClick(view);
            }
        });
        addPlusIcon.setVisibility(GONE);
        inputLayout.addView(addPlusIcon);

        this.addView(inputLayout);
    }

    public void addInvitee(ITimeInviteeInterface invitee){
        hideKeyboard();
        if(invitee.getUserStatus().equals(Invitee.USER_STATUS_ACTIVATED)){
            addAvatarInvitee(invitee);
        }else{
            addEmailInvitee(invitee);
        }
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEditText.getWindowToken(),0);
    }

    public void addEmailInvitee(ITimeInviteeInterface invitee){
        if(inviteeMap.containsKey(invitee.getUserId())){
            textFlowLayout.addView(inviteeMap.get(invitee.getUserId()));
            return;
        }
        TextView emailTextView = new TextView(this.getContext());
        emailTextView.setText(invitee.getUserId());
        if(squareHeight == 0){
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SizeUtil.dp2px(getContext(), 115), ViewGroup.LayoutParams.WRAP_CONTENT);
            emailTextView.setLayoutParams(params);
        }else{
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SizeUtil.dp2px(getContext(), 115), squareHeight);
            emailTextView.setLayoutParams(params);
        }

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(dp2px(2.5));
        drawable.setColor(getResources().getColor(emailColor));
        emailTextView.setBackground(drawable);
        emailTextView.setPadding(dp2px(12),dp2px(5),dp2px(12),dp2px(5));
        emailTextView.setTextColor(Color.WHITE);
        //emailTextView.setBackgroundColor(emailColor);
        emailTextView.setEllipsize(TextUtils.TruncateAt.END);
        emailTextView.setTextSize(ITEM_FONT_SIZE);
        emailTextView.setMaxLines(1);
        emailTextView.setOnClickListener(new InviteeOnClickListener());
        emailTextView.setMaxWidth(SizeUtil.dp2px(getContext(), 115));
        emailTextView.setMinWidth(SizeUtil.dp2px(getContext(), 115));
        textFlowLayout.addView(emailTextView);
        emailTextView.setTag(invitee);
        inviteeMap.put(invitee.getUserId(), emailTextView);
        //addInviteeToList(invitee);
    }


    public void addPhoneInvitee(ITimeInviteeInterface invitee){
//        if(inviteeMap.containsKey(invitee.getUserId())){
//            textFlowLayout.addView(inviteeMap.get(invitee.getUserId()));
//            return;
//        }
        TextView phoneTextView = new TextView(this.getContext());
        phoneTextView.setText(invitee.getUserId());
        if(squareHeight == 0){
            ListView.LayoutParams params = new ListView.LayoutParams(SizeUtil.dp2px(getContext(), 102), SizeUtil.dp2px(getContext(), 22));
            phoneTextView.setLayoutParams(params);
        }else{
            ListView.LayoutParams params = new ListView.LayoutParams(SizeUtil.dp2px(getContext(), 102), squareHeight);
            phoneTextView.setLayoutParams(params);
        }

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(dp2px(5));
        drawable.setColor(getResources().getColor(phoneColor));

        phoneTextView.setBackground(drawable);
        phoneTextView.setPadding(dp2px(5),dp2px(5),dp2px(5),dp2px(5));
        phoneTextView.setTextColor(Color.WHITE);
        phoneTextView.setEllipsize(TextUtils.TruncateAt.END);
        phoneTextView.setMaxLines(1);
        phoneTextView.setTextSize(ITEM_FONT_SIZE);
        phoneTextView.setOnClickListener(new InviteeOnClickListener());
        textFlowLayout.addView(phoneTextView);
        phoneTextView.setTag(invitee);
        inviteeMap.put(invitee.getUserId(), phoneTextView);
        //addInviteeToList(invitee);
    }

    public void addAvatarInvitee(ITimeInviteeInterface invitee){
        if(inviteeMap.containsKey(invitee.getUserId())){
            avatarFlowLayout.addView(inviteeMap.get(invitee.getUserId()));
            return;
        }
        RoundImageView avatarImageView = new RoundImageView(this.getContext());
        Picasso.with(this.getContext())
                .load(invitee.getPhoto())
                .resize(px2dp(avatarWidth),px2dp(avatarHeight))
                .centerCrop()
                .into(avatarImageView);
        avatarImageView.setOnClickListener(new InviteeOnClickListener());
        avatarFlowLayout.addView(avatarImageView);
        avatarImageView.setTag(invitee);
        inviteeMap.put(invitee.getUserId(), avatarImageView);
        //addInviteeToList(invitee);
    }

    public void deleteInvitee(String userId){
        View view = null;
        view = inviteeMap.get(userId);
        if(view != null) {
            avatarFlowLayout.removeView(view);
            textFlowLayout.removeView(view);
        }
    }

    public void setHintText(String hintStr){
        inputEditText.setHint(hintStr);
    }

    public void setOnInviteeClickListener(OnItemClickListener listener){
        this.onInviteeClickListener = listener;
    }

    public void setOnEditListener(OnEditListener listener){
        this.onEditListener = listener;
    }

    // Clear EditText
    public void clearInput(){
        inputEditText.setText("");
    }

    private class InviteeOnClickListener implements OnClickListener
    {
        @Override
        public void onClick(View view) {
            ITimeInviteeInterface invitee = (ITimeInviteeInterface) view.getTag();
            deleteInvitee(invitee.getUserId());
            if(onInviteeClickListener !=null) {
                onInviteeClickListener.onClick(view, invitee);
            }
        }
    }

    public interface OnEditListener{
        void onEditing(View view, String text);
    }

    public interface OnItemClickListener {
        void onClick(View view, ITimeInviteeInterface invitee);
    }

    private int dp2px(double dipValue){
        Context context = getContext();
        float scale=context.getResources().getDisplayMetrics().densityDpi;
        return (int)(dipValue*(scale/160)+0.5f);
    }

    private int px2dp(double pxValue){
        Context context = getContext();
        float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int)((pxValue*160)/scale+0.5f);
    }

    public int countInvitee(){
        return inviteeMap.size();
    }

    public List<ITimeInviteeInterface> getInviteeList(){
        List<ITimeInviteeInterface> result = new ArrayList<>();
        for(View v:inviteeMap.values()){
            result.add((ITimeInviteeInterface) v.getTag());
        }
        return result;
    }
}
