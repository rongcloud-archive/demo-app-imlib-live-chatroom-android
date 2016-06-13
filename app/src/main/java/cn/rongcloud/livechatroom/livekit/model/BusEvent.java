package cn.rongcloud.livechatroom.livekit.model;

import io.rong.imlib.model.Message;

public class BusEvent {

    public static class MessageReceived {
        public Message message;
        public int left;

        public MessageReceived(Message msg, int left) {
            message = msg;
            this.left = left;
        }
    }

    public static class MessageSent {
        public Message message;
        public int code;

        public MessageSent(Message msg, int code) {
            message = msg;
            this.code = code;
        }
    }
}
