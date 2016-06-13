package cn.rongcloud.livechatroom.livekit.template;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
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
            holder.content = (TextView) convertView.findViewById(R.id.rc_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message msg = data.getMessage();
        UserInfo info = msg.getContent().getUserInfo();
        String username;
        if (info != null) {
            username = info.getName();
        } else {
            username = msg.getSenderUserId();
        }

        int res = R.drawable.u1f339;
        GiftMessage giftMsg = (GiftMessage) msg.getContent();
        if (giftMsg.getType().equals("1")) {
            res = R.drawable.u1f44f;
        }

        GiftMessage giftMessage = (GiftMessage) msg.getContent();
        SpannableStringBuilder ssb = new SpannableStringBuilder(username + "送出一个X");
        Bitmap bitmap = BitmapFactory.decodeResource(parent.getContext().getResources(), res);
        ImageSpan imageSpan = new ImageSpan(parent.getContext(), bitmap);
        int start = ssb.toString().indexOf("X");
        ssb.setSpan(imageSpan, start, start + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(ssb);
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
        TextView content;
    }
}
