package cn.rongcloud.livechatroom.livekit.template;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.rongcloud.livechatroom.R;
import io.rong.common.RLog;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.toolkit.emoticon.Emoji;

@TemplateTag(messageContent = TextMessage.class)
public class TextMessageTemplate implements BaseMessageTemplate {
    private final static String TAG = "TextMessageTemplate";

    @Override
    public View getView(View convertView, int position, ViewGroup parent, UIMessage data) {
        RLog.e(TAG, "getView " + position + " " + convertView);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_text_message, null);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message msg = data.getMessage();
        UserInfo info = msg.getContent().getUserInfo();
        if (info != null) {
            holder.username.setText(info.getName() + ": ");
        } else {
            holder.username.setText(msg.getSenderUserId() + ": ");
        }

//        if (msg.getMessageDirection() == Message.MessageDirection.SEND) {
//            holder.username.setTextColor(parent.getContext().getResources().getColor(R.color.live_me));
//        } else if (msg.getMessageDirection() == Message.MessageDirection.RECEIVE) {
//            holder.username.setTextColor(parent.getContext().getResources().getColor(R.color.live_other));
//        }

        TextMessage textMsg = (TextMessage) msg.getContent();
        CharSequence text = Emoji.ensure(parent.getContext(), textMsg.getContent());
        holder.content.setText(text);
        return convertView;
    }

    @Override
    public void onItemClick(View view, int position, UIMessage data) {

    }

    @Override
    public void onItemLongClick(View view, int position, UIMessage data) {

    }

    @Override
    public void destroyItem(ViewGroup group, Object template) {

    }

    private class ViewHolder {
        TextView username;
        TextView content;
    }
}
