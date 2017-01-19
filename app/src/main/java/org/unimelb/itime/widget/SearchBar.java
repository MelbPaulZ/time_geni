package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;


/**
 * Created by 37925 on 2016/12/5.
 */

public class SearchBar extends FrameLayout {

    private LinearLayout mainLayout;
    private RelativeLayout searchButton;
    private RelativeLayout inputView;
    private PureEditText inputText;
    private TextView cancelButton;
    private ImageView cleanIcon;
    private int inputFontSize = 12;
    private OnEditListener onEditListener;
    private boolean onEditing;
    private OnClickListener cancelListener;
    private int cancelButtonId = View.generateViewId();
    private int iconId = View.generateViewId();
    private int cleanIconId = View.generateViewId();

    private boolean showCancel = false;
    private String searchHintText;
    private String searchHintTitle;

    public SearchBar(Context context) {
        super(context);
        searchHintText = getContext().getString(R.string.search_hint);
        searchHintTitle = getContext().getString(R.string.search);
        init();
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDeclaredStyle(context, attrs, 0);
        init();
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDeclaredStyle(context, attrs, defStyleAttr);
        init();
    }

    private void initDeclaredStyle(Context context, AttributeSet attrs, int defStyleAttr){
        searchHintText = getContext().getString(R.string.search_hint);
        searchHintTitle = getContext().getString(R.string.search);
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBar, defStyleAttr, 0);
        try {
            searchHintText = arr.getString(R.styleable.SearchBar_searchHintText);
            searchHintTitle = arr.getString(R.styleable.SearchBar_searchHintTitle);
            showCancel = arr.getBoolean(R.styleable.SearchBar_showCancel, false);
        } finally {
            arr.recycle();
        }
    }

    private void init(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                SizeUtil.dp2px(getContext(), 50));
        mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setLayoutParams(params);
        mainLayout.setBackground(getResources().getDrawable(R.color.grey_six));
        mainLayout.setPadding(SizeUtil.dp2px(getContext(),8),
                SizeUtil.dp2px(getContext(),7.5),
                SizeUtil.dp2px(getContext(),8),
                SizeUtil.dp2px(getContext(),7.5));
        this.addView(mainLayout);
        this.initSearchButton();
        this.initInputView();
        this.hideInputView();
    }

    public void setShowCancel(boolean show){
        showCancel = show;
    }

    public boolean isOnEditing(){
        return !inputText.getText().toString().equals("");
    }



    public void initSearchButton(){
        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        searchButton = new RelativeLayout(getContext());
        searchButton.setLayoutParams(params);
        searchButton.setBackgroundResource(R.drawable.corner_border);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputView();
            }
        });

        LinearLayout linearLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.setLayoutParams(linearParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        iconParams.setMargins(0, 0,SizeUtil.dp2px(getContext(),10),0);
        iconParams.gravity = Gravity.CENTER_VERTICAL;
        ImageView icon = new ImageView(getContext());
        icon.setLayoutParams(iconParams);
        icon.setImageResource(R.drawable.icon_general_search);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        TextView search = new TextView(getContext());
        search.setLayoutParams(textParams);
        search.setText(searchHintTitle);
        search.getPaint().setFakeBoldText(true);
        search.setTextSize(inputFontSize);

        linearLayout.addView(icon);
        linearLayout.addView(search);
        searchButton.addView(linearLayout);

        mainLayout.addView(searchButton);
    }

    public void initInputView(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
//        params.setMargins(SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10));
        params.addRule(RelativeLayout.LEFT_OF, cancelButtonId);
        RelativeLayout editTextLayout = new RelativeLayout(getContext());
        editTextLayout.setLayoutParams(params);
        editTextLayout.setBackground(getResources().getDrawable(R.drawable.corner_border));

        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        iconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        iconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                0,
                SizeUtil.dp2px(getContext(),10),
                0);
        ImageView icon = new ImageView(getContext());
        icon.setLayoutParams(iconParams);
        icon.setImageResource(R.drawable.icon_general_search);
        icon.setId(iconId);
//        icon.setId(2);

        RelativeLayout.LayoutParams cleanIconParams = new RelativeLayout.LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        cleanIconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                0,
                SizeUtil.dp2px(getContext(),10),
                0);
        cleanIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cleanIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        cleanIcon = new ImageView(getContext());
        cleanIcon.setLayoutParams(cleanIconParams);
        cleanIcon.setImageResource(R.drawable.invitee_selected_emotionstore_progresscancelbtn);
        cleanIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText("");
            }
        });
        cleanIcon.setVisibility(GONE);
        cleanIcon.setId(cleanIconId);
//        cleanIcon.setId(3);

        inputText = new PureEditText(getContext());
        RelativeLayout.LayoutParams inputTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        inputTextParams.addRule(RelativeLayout.RIGHT_OF, iconId);
        inputTextParams.addRule(RelativeLayout.LEFT_OF, cleanIconId);
        inputTextParams.addRule(RelativeLayout.CENTER_VERTICAL);
        inputTextParams.setMargins(0,0, SizeUtil.dp2px(getContext(),10),0);
        inputText.setTextSize(inputFontSize);
        inputText.setHint(searchHintText);
        inputText.setHintTextColor(getResources().getColor(R.color.normalGrey));
        inputText.setSingleLine(true);
        inputText.setLayoutParams(inputTextParams);
        editTextLayout.addView(icon);
        editTextLayout.addView(inputText);
        editTextLayout.addView(cleanIcon);
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(onEditListener!=null) {
                    if(isOnEditing()){
                        if(showCancel) {
                            cancelButton.setVisibility(VISIBLE);
                        }
                        cleanIcon.setVisibility(VISIBLE);
                    }else{
                        cleanIcon.setVisibility(GONE);
                    }
                    onEditListener.onEditing(inputText, inputText.getText().toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        LinearLayout.LayoutParams inputViewParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
//        inputViewParams.setMargins(SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10));
        inputView = new RelativeLayout(getContext());
        inputView.setLayoutParams(inputViewParams);


        cancelButton = new TextView(getContext());
        RelativeLayout.LayoutParams cancelButtonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        cancelButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cancelButtonParams.setMargins(SizeUtil.dp2px(getContext(),10),
                0,
                0,
                0);
        cancelButton.setLayoutParams(cancelButtonParams);
        cancelButton.setText("Cancel");
        cancelButton.setTextColor(getResources().getColor(R.color.red_alert));
        cancelButton.setId(cancelButtonId);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideInputView();
                inputText.setText("");
                if(cancelListener!=null) {
                    cancelListener.onClick(view);
                }
            }
        });
        cancelButton.setVisibility(GONE);

        inputView.addView(editTextLayout);
        inputView.addView(cancelButton);
        mainLayout.addView(inputView);
    }

    private void showInputView(){
        searchButton.setVisibility(GONE);
        inputView.setVisibility(VISIBLE);
        inputText.setFocusableInTouchMode(true);
        inputText.requestFocus();
        showKeyboard();
    }

    private void showKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager)inputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(inputText, 0);
    }

    private void hideKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager)inputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager!=null) {
            inputManager.hideSoftInputFromWindow(inputText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void hideInputView(){
        searchButton.setVisibility(VISIBLE);
        inputView.setVisibility(GONE);
        cancelButton.setVisibility(GONE);
        searchButton.requestFocus();
        hideKeyboard();
    }

    public void setSearchListener(OnEditListener listener){
        this.onEditListener = listener;
    }

    public void setCancelListener(OnClickListener listener){
        cancelListener = listener;
    }

    public interface OnEditListener{
        void onEditing(View view, String text);
    }
}
