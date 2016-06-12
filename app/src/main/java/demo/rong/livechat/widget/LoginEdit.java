package demo.rong.livechat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import demo.rong.livechat.R;

public class LoginEdit extends RelativeLayout {

    private EditText editText;

    public LoginEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_loginedit, this, true);
        editText = (EditText) view.findViewById(R.id.content);
    }

    public void setHint(String hint) {
        editText.setHint(hint);
    }
}
