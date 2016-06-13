package cn.rongcloud.livechatroom.livekit.template;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BaseContainerView extends LinearLayout {
    private String objectName;
    private View convertView;

    public BaseContainerView(Context context) {
        super(context);
        convertView = null;
    }

    public BaseContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        convertView = null;
    }

    public View inflate(BaseMessageTemplate template, int position, String objectName, UIMessage uiMessage) {
        /**
         * 根据模板名字检查是否回收 view，提升性能.
         * objectName 作为该模板的唯一 name.
         */
        if(this.objectName == null || !this.objectName.equals(objectName)) {
            removeAllViews();
            convertView = template.getView(null, position, this, uiMessage);
        } else {
            convertView = template.getView(convertView, position, this, uiMessage);
        }
        if(convertView.getParent() == null) {
            addView(convertView);
        }
        this.objectName = objectName;
        return convertView;
    }
}
