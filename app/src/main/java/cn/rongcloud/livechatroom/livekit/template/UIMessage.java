package cn.rongcloud.livechatroom.livekit.template;

import io.rong.imlib.model.Message;

public class UIMessage {
    private int progress;
    private boolean playing;
    private Message message;

    public UIMessage(Message message) {
        this.message = message;
        this.progress = 100;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public Message getMessage() {
        return message;
    }

    public static UIMessage obtain(Message message) {
        return new UIMessage(message);
    }
}
