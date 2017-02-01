package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.adapters.ListenerUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import org.unimelb.itime.vendor.weekview.WeekView;

import static android.R.attr.type;


/**
 * Created by Qiushuo Huang on 2016/12/5.
 */

@InverseBindingMethods({
        @InverseBindingMethod(type = SearchBar.class, attribute = "android:inputText"),
})
@BindingMethods({
        @BindingMethod(type = SearchBar.class, attribute = "android:onTextChanged", method = "setSearchListener")
})
public class SearchBar extends FrameLayout {

    private static final int DEFAULT_SEARCH_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEFAULT_HINT_TEXT_COLOR = Color.parseColor("#949494");
    private static final int DEFAULT_SEARCH_TEXT_COLOR = Color.parseColor("#6D6D6D");

    private LinearLayout mainLayout;
    private RelativeLayout searchButton;
    private RelativeLayout inputView;
    private PureEditText inputText;
    private ImageView cleanIcon;
    private int inputFontSize = SizeUtil.px2dp(getContext(), getContext().getResources().getDimension(R.dimen.font_small));
    private OnEditListener onEditListener;
    private boolean onEditing;
    private OnClickListener cancelListener;
    private int iconId = View.generateViewId();
    private int cleanIconId = View.generateViewId();

    private String searchHintText;
    private String searchHintTitle;
    private int searchBackgroundColor;
    private int searchHintTextColor;
    private int searchTextColor;
    private boolean showSearchButton = true;

    public SearchBar(Context context) {
        super(context);
        searchHintText = getContext().getString(R.string.search_hint);
        searchHintTitle = getContext().getString(R.string.search);
        searchBackgroundColor = DEFAULT_SEARCH_BACKGROUND_COLOR;
        searchHintTextColor = DEFAULT_SEARCH_BACKGROUND_COLOR;
        searchTextColor = DEFAULT_SEARCH_TEXT_COLOR;
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

    /**
     * this is for set the text of search view from outside
     * @param text
     */
    public void setInputText(String text){
        inputText.setText(text);
    }


    /**
     * get current display text from out set
     * @return
     */
    public String getInputText(){
        return inputText.getText().toString();
    }

    private void initDeclaredStyle(Context context, AttributeSet attrs, int defStyleAttr){
        searchHintText = getContext().getString(R.string.search_hint);
        searchHintTitle = getContext().getString(R.string.search);
        searchBackgroundColor = DEFAULT_SEARCH_BACKGROUND_COLOR;
        searchHintTextColor = DEFAULT_HINT_TEXT_COLOR;
        searchTextColor = DEFAULT_SEARCH_TEXT_COLOR;

        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBar, defStyleAttr, 0);
        try {
            if(arr.getString(R.styleable.SearchBar_searchHintText)!=null)
                searchHintText = arr.getString(R.styleable.SearchBar_searchHintText);
            if(arr.getString(R.styleable.SearchBar_searchHintTitle)!=null)
                searchHintTitle = arr.getString(R.styleable.SearchBar_searchHintTitle);
            searchBackgroundColor = arr.getColor(R.styleable.SearchBar_searchBackGroundColor, DEFAULT_SEARCH_BACKGROUND_COLOR);
            searchHintTextColor = arr.getColor(R.styleable.SearchBar_searchHintTextColor, DEFAULT_HINT_TEXT_COLOR);
            searchTextColor = arr.getColor(R.styleable.SearchBar_searchTextColor, DEFAULT_SEARCH_TEXT_COLOR);
            showSearchButton = arr.getBoolean(R.styleable.SearchBar_showSearchButton, true);
        } finally {
            arr.recycle();
        }
    }

    private void init(){
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                SizeUtil.dp2px(getContext(), 50));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setLayoutParams(params);
//        mainLayout.setPadding(SizeUtil.dp2px(getContext(),8),
//                SizeUtil.dp2px(getContext(),7.5),
//                SizeUtil.dp2px(getContext(),8),
//                SizeUtil.dp2px(getContext(),7.5));
        this.setBackgroundResource(R.drawable.corner_border);
        this.addView(mainLayout);
        this.initSearchButton();
        this.initInputView();
        this.hideInputView();
        if(showSearchButton==false) {
            showInputView();
        }
    }

    public boolean isOnEditing(){
        return !inputText.getText().toString().equals("");
    }

    public void initSearchButton(){
        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        searchButton = new RelativeLayout(getContext());
        searchButton.setLayoutParams(params);
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
        icon.setImageResource(R.drawable.icon_searchbar_search);

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
        LinearLayout editTextLayout = new LinearLayout(getContext());
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setLayoutParams(params);
        GradientDrawable background = (GradientDrawable) getResources().getDrawable(R.drawable.corner_border);
        background.setColor(searchBackgroundColor);
        editTextLayout.setBackground(background);

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        iconParams.weight = 0;
        iconParams.gravity = Gravity.CENTER_VERTICAL;
        iconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                0,
                SizeUtil.dp2px(getContext(),10),
                0);
        ImageView icon = new ImageView(getContext());
        icon.setLayoutParams(iconParams);
        icon.setImageResource(R.drawable.icon_searchbar_search);
        icon.setId(iconId);
//        icon.setId(2);

        LinearLayout.LayoutParams cleanIconParams = new LinearLayout.LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        cleanIconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                0,
                SizeUtil.dp2px(getContext(),10),
                0);
        cleanIconParams.weight = 0;
        cleanIconParams.gravity = Gravity.CENTER_VERTICAL;
        cleanIcon = new ImageView(getContext());
        cleanIcon.setLayoutParams(cleanIconParams);
        cleanIcon.setImageResource(R.drawable.icon_searchbar_cancel);
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
        LinearLayout.LayoutParams inputTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputTextParams.weight = 1;
        inputTextParams.gravity = Gravity.CENTER_VERTICAL;
        inputTextParams.setMargins(0,0, SizeUtil.dp2px(getContext(),10),0);
        inputText.setTextSize(inputFontSize);
        inputText.setHint(searchHintText);
        inputText.setHintTextColor(searchHintTextColor);
        inputText.setSingleLine(true);
        inputText.setLayoutParams(inputTextParams);
        inputText.setTextColor(searchTextColor);

        editTextLayout.addView(icon);
        editTextLayout.addView(inputText);
        editTextLayout.addView(cleanIcon);
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(onEditListener!=null) {
                    if(isOnEditing()){
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
//
//
//        cancelButton = new TextView(getContext());
//        RelativeLayout.LayoutParams cancelButtonParams = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        cancelButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        cancelButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        cancelButtonParams.setMargins(SizeUtil.dp2px(getContext(),10),
//                0,
//                0,
//                0);
//        cancelButton.setLayoutParams(cancelButtonParams);
//        cancelButton.setText("Cancel");
//        cancelButton.setTextColor(getResources().getColor(R.color.red_alert));
//        cancelButton.setId(cancelButtonId);
//        cancelButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideInputView();
//                inputText.setText("");
//                if(cancelListener!=null) {
//                    cancelListener.onClick(view);
//                }
//            }
//        });
//        cancelButton.setVisibility(GONE);

        inputView.addView(editTextLayout);
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
        searchButton.requestFocus();
        hideKeyboard();
    }

    public void clearInput(){
        inputText.setText("");
    }

    public void setShowSearchButton(boolean bool){
        this.showSearchButton = bool;
        if(showSearchButton==true){
            clearInput();
            hideInputView();
        }else{
            showInputView();
        }
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


    @BindingAdapter(value = {
            "android:inputText", "android:onTextChanged","android:inputTextAttrChanged" }, requireAll = false)
    public static void setListeners(SearchBar view, String inputText,
                                    InputTextChangedListener inputTextChangedListener,
                                    final InverseBindingListener bindingListener){

        InputTextChangedListener newListener;
        if (bindingListener == null){
            newListener = inputTextChangedListener;
        }else{
            newListener = new InputTextChangedListener(inputTextChangedListener, bindingListener);
        }
        view.setSearchListener(newListener);
    }

    public static class InputTextChangedListener implements OnEditListener{
        OnEditListener onEditListener;
        InverseBindingListener inputTextChangedListener;

        public InputTextChangedListener(OnEditListener onEditListener, InverseBindingListener inputTextChangedListener) {
            this.onEditListener = onEditListener;
            this.inputTextChangedListener = inputTextChangedListener;
        }

        @Override
        public void onEditing(View view, String text) {
            if (onEditListener!=null){
                onEditListener.onEditing(view, text);
            }
            if (inputTextChangedListener!=null) {
                inputTextChangedListener.onChange();
            }
        }
    }
}
