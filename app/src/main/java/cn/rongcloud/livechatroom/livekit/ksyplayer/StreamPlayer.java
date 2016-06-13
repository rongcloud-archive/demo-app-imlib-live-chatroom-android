package cn.rongcloud.livechatroom.livekit.ksyplayer;

public interface StreamPlayer {

    void setStreamListener(StreamListener listener);

    void play();

    void pause();

    void stop();

    void setUrl(String url);

    void setView(VideoSurfaceView view);
}
