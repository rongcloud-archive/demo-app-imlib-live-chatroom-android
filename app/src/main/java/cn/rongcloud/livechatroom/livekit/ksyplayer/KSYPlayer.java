package cn.rongcloud.livechatroom.livekit.ksyplayer;

import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;

import java.io.IOException;

public class KSYPlayer implements StreamPlayer, IMediaPlayer.OnErrorListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = "KSYPlayer";

    private VideoSurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private KSYMediaPlayer mKSYMediaPlayer;
    private StreamListener mStreamListener;

    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    public KSYPlayer(Context context) {
        mKSYMediaPlayer = new KSYMediaPlayer.Builder(context).build();
        mKSYMediaPlayer.setScreenOnWhilePlaying(true);
        mKSYMediaPlayer.setBufferTimeMax(5);
    }

    @Override
    public void setStreamListener(StreamListener listener) {
        mStreamListener = listener;
        mKSYMediaPlayer.setOnErrorListener(this);
        mKSYMediaPlayer.setOnPreparedListener(this);
        mKSYMediaPlayer.setOnCompletionListener(this);
        mKSYMediaPlayer.setOnVideoSizeChangedListener(this);
    }

    @Override
    public void play() {
        mKSYMediaPlayer.start();
    }

    @Override
    public void pause() {
        mKSYMediaPlayer.pause();
    }

    @Override
    public void stop() {
        mKSYMediaPlayer.stop();
    }

    @Override
    public void setUrl(String url) {
        try {
            mKSYMediaPlayer.setDataSource(url);
            mKSYMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setView(VideoSurfaceView view) {
        mSurfaceView = view;
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);
    }

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {

        private Surface mSurface = null;

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mKSYMediaPlayer != null) {
                final Surface newSurface = holder.getSurface();
                mKSYMediaPlayer.setDisplay(holder);
                mKSYMediaPlayer.setScreenOnWhilePlaying(true);
                if (mSurface != newSurface) {
                    mSurface = newSurface;
                    mKSYMediaPlayer.setSurface(mSurface);
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mKSYMediaPlayer != null) {
                mSurface = null;
            }
        }
    };

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError code = " + what);
        StreamListener.StreamError code = StreamListener.StreamError.ERROR_UNKNOWN;
        switch (what) {
            case KSYMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                code = StreamListener.StreamError.ERROR_SERVER_DIED;
                break;
            default:
        }
        mStreamListener.onError(code);
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Log.e(TAG, "onPrepared");
        mVideoWidth = mKSYMediaPlayer.getVideoWidth();
        mVideoHeight = mKSYMediaPlayer.getVideoHeight();
        if(mSurfaceView != null)
        {
            Log.d(TAG, "onPrepared w * h = " + mKSYMediaPlayer.getVideoWidth() + " * " + mKSYMediaPlayer.getVideoHeight());
            mSurfaceView.setVideoDimension(mKSYMediaPlayer.getVideoWidth(), mKSYMediaPlayer.getVideoHeight());
            mSurfaceView.requestLayout();
        }
        mStreamListener.onPrepared();
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Log.e(TAG, "onCompletion");
        mStreamListener.onCompletion();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
        Log.d(TAG, "onVideoSizeChanged w * h = " + width + " * " + height);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (width != mVideoWidth || height != mVideoHeight) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();

                // maybe we could call scaleVideoView here.
                if (mSurfaceView != null) {
                    mSurfaceView.setVideoDimension(mVideoWidth, mVideoHeight);
                    mSurfaceView.requestLayout();
                }
            }
        }
    }
}
