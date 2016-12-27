package org.unimelb.itime.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.SizeCollector;
import org.unimelb.itime.util.SizeUtil;


/**
 * Created by 37925 on 2016/12/5.
 */

public class SearchBar extends LinearLayout {

    private LinearLayout searchButton;
    private RelativeLayout inputView;
    private PureEditText inputText;
    private TextView cancelButton;
    private int inputFontSize = SizeCollector.FONT_SMALL;
    private OnEditListener onEditListener;

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(params);
        this.setBackground(getResources().getDrawable(R.color.lightGrey));
        this.initSearchButton();
        this.initInputView();
        this.hideInputView();
    }

    public void initSearchButton(){
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10));
        searchButton = new LinearLayout(getContext());
        searchButton.setOrientation(LinearLayout.VERTICAL);
        searchButton.setLayoutParams(params);
        searchButton.setBackgroundResource(R.drawable.corner_border);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputView();
            }
        });

        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams linearParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearParams.gravity= Gravity.CENTER_HORIZONTAL;
        linearLayout.setLayoutParams(linearParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams iconParams = new LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        iconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10));
        iconParams.gravity = Gravity.CENTER_VERTICAL;
        ImageView icon = new ImageView(getContext());
        icon.setLayoutParams(iconParams);
        icon.setImageResource(R.drawable.icon_general_search);

        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        TextView search = new TextView(getContext());
        search.setLayoutParams(textParams);
        search.setText("Search");

        linearLayout.addView(icon);
        linearLayout.addView(search);
        searchButton.addView(linearLayout);

        this.addView(searchButton);
    }

    public void initInputView(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10),
//                SizeUtil.dp2px(getContext(),10));
        params.addRule(RelativeLayout.LEFT_OF, 1);
        LinearLayout editTextLayout = new LinearLayout(getContext());
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setLayoutParams(params);
        editTextLayout.setBackground(getResources().getDrawable(R.drawable.corner_border));

        LayoutParams iconParams = new LayoutParams(SizeUtil.dp2px(getContext(),15),
                SizeUtil.dp2px(getContext(),15));
        iconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10));
        ImageView icon = new ImageView(getContext());
        icon.setLayoutParams(iconParams);
        icon.setImageResource(R.drawable.icon_general_search);

        inputText = new PureEditText(getContext());
        LayoutParams inputTextParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        inputTextParams.setMargins(0,0, SizeUtil.dp2px(getContext(),10),0);
        inputText.setTextSize(inputFontSize);
        inputText.setHint(getContext().getString(R.string.search_hint));
        inputText.setHintTextColor(getResources().getColor(R.color.normalGrey));
        inputText.setSingleLine(true);
        inputText.setLayoutParams(inputTextParams);
        editTextLayout.addView(icon);
        editTextLayout.addView(inputText);
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(onEditListener!=null) {
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

        LinearLayout.LayoutParams inputViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        inputViewParams.setMargins(SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10));
        inputView = new RelativeLayout(getContext());
        inputView.setLayoutParams(inputViewParams);


        cancelButton = new TextView(getContext());
        RelativeLayout.LayoutParams cancelButtonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cancelButtonParams.setMargins(SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                0,
                SizeUtil.dp2px(getContext(),10));
        cancelButton.setLayoutParams(cancelButtonParams);
        cancelButton.setText("Cancel");
        cancelButton.setTextColor(getResources().getColor(R.color.lightRed));
        cancelButton.setId(1);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideInputView();
                inputText.setText("");
            }
        });


        inputView.addView(editTextLayout);
        inputView.addView(cancelButton);
        this.addView(inputView);
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

    public void setOnEditListener(OnEditListener listener){
        this.onEditListener = listener;
    }

    public interface OnEditListener{
        void onEditing(View view, String text);
    }
}
