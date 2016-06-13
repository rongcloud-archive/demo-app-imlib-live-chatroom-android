package cn.rongcloud.livechatroom.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import cn.rongcloud.livechatroom.LoginActivity;
import cn.rongcloud.livechatroom.R;

public class LoginEdit extends RelativeLayout {

    private View underline;
    private EditText editText;

    public LoginEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_loginedit, this, true);
        underline = view.findViewById(R.id.underline);
        underline.setEnabled(false);
        editText = (EditText) view.findViewById(R.id.content);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                underline.setEnabled(hasFocus);
            }
        });
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }

    public void setTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public String getText() {
        return editText.getText().toString();
    }
}
