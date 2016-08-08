package cn.rongcloud.live.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.rongcloud.live.R;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;

public class InfoMsgView extends BaseMsgView {

    private TextView username;
    private TextView infoText;

    public InfoMsgView(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.msg_info_view, this);
        username = (TextView) view.findViewById(R.id.username);
        infoText = (TextView) view.findViewById(R.id.info_text);
    }

    @Override
    public void setContent(MessageContent msgContent) {
        InformationNotificationMessage msg = (InformationNotificationMessage) msgContent;
        username.setText(msg.getUserInfo().getName() + " ");
        infoText.setText(msg.getMessage());
    }
}
