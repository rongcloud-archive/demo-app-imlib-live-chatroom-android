package cn.rongcloud.livechatroom.livekit.template;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.rongcloud.livechatroom.R;
import io.rong.common.RLog;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

@TemplateTag(messageContent = GiftMessage.class)
public class GiftMessageTemplate implements BaseMessageTemplate {
    private final static String TAG = "GiftMessageTemplate";

    @Override
    public View getView(View convertView, int position, ViewGroup parent, UIMessage data) {
        RLog.e(TAG, "getView " + position + " " + convertView);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_customize_message, null);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message msg = data.getMessage();
        UserInfo info = msg.getContent().getUserInfo();
        if (info != null) {
            holder.username.setText(info.getName() + " ");
        } else {
            holder.username.setText(msg.getSenderUserId() + " ");
        }

        GiftMessage giftMsg = (GiftMessage) msg.getContent();
        String content;
        if (giftMsg.getType().equals("1")) {
            content = "为主播点赞";
        } else{
            content = "送了一个钻石";
        }
        holder.content.setText(content);
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
