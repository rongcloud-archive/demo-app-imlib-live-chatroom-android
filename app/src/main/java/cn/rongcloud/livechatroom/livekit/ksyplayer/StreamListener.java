package cn.rongcloud.livechatroom.livekit.ksyplayer;

public interface StreamListener {

    enum StreamError {
        ERROR_UNKNOWN,
        ERROR_SERVER_DIED
    }

    void onError(StreamError error);

    void onPrepared();

    void onCompletion();
}
