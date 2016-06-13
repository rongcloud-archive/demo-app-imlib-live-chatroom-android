package cn.rongcloud.livechatroom.livekit.template;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.rongcloud.livechatroom.R;
import io.rong.common.RLog;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

@TemplateTag(messageContent = InformationNotificationMessage.class)
public class InformationNotificationMessageTemplate implements BaseMessageTemplate {
    private final static String TAG = "InformationNotificationMessageTemplate";

    @Override
    public View getView(View convertView, int position, ViewGroup parent, UIMessage data) {
        RLog.e( TAG, "getView " + position + " " + convertView);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_infor_message, null);
            holder.content = (TextView) convertView.findViewById(R.id.rc_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message msg = data.getMessage();
        InformationNotificationMessage infoMsg = (InformationNotificationMessage) msg.getContent();
        holder.content.setText(infoMsg.getMessage());
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