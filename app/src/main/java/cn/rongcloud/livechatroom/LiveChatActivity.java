package cn.rongcloud.livechatroom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import cn.rongcloud.livechatroom.livekit.LiveChatListAdapter;
import cn.rongcloud.livechatroom.livekit.RongIM;
import cn.rongcloud.livechatroom.livekit.ksyplayer.LiveClient;
import cn.rongcloud.livechatroom.livekit.ksyplayer.StreamListener;
import cn.rongcloud.livechatroom.livekit.ksyplayer.VideoSurfaceView;
import cn.rongcloud.livechatroom.livekit.model.BusEvent;
import cn.rongcloud.livechatroom.livekit.template.GiftMessage;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.toolkit.IInputBoardClickListener;
import io.rong.toolkit.RongInputBoard;

public class LiveChatActivity extends AppCompatActivity {

    private static final String TAG = "LiveChatActivity";

    private LiveClient liveClient = new LiveClient();
    private LiveChatListAdapter liveChatListAdapter;
    private NewMessageHint newMessage;
    private View buttonBoard;
    private RongInputBoard inputBoard;

    private String targetId;
    private Conversation.ConversationType conversationType;
    private Drawable[] drawables;
    private Random random = new Random();
    private RelativeLayout backLayout;
    private RelativeLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livechat);
        RongIM.getInstance().getEventBus().register(this);

        Uri uri = getIntent().getData();
        String typeStr = uri.getLastPathSegment().toUpperCase();
        conversationType = Conversation.ConversationType.valueOf(typeStr);
        targetId = uri.getQueryParameter("targetId");
        String liveUrl = uri.getQueryParameter("liveUrl");
        VideoSurfaceView liveSurfaceView = (VideoSurfaceView) findViewById(R.id.live_surface_view);

        ListView liveChatListView = (ListView) findViewById(R.id.live_chatlist);
        buttonBoard = findViewById(R.id.button_board);
        inputBoard = (RongInputBoard) findViewById(R.id.input_board);
        backLayout = (RelativeLayout) findViewById(R.id.back_view);
        lp = new RelativeLayout.LayoutParams(100, 100);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        drawables = new Drawable[9];
        drawables[0] = getResources().getDrawable(R.drawable.heart0);
        drawables[1] = getResources().getDrawable(R.drawable.heart1);
        drawables[2] = getResources().getDrawable(R.drawable.heart2);
        drawables[3] = getResources().getDrawable(R.drawable.heart3);
        drawables[4] = getResources().getDrawable(R.drawable.heart4);
        drawables[5] = getResources().getDrawable(R.drawable.heart5);
        drawables[6] = getResources().getDrawable(R.drawable.heart6);
        drawables[7] = getResources().getDrawable(R.drawable.heart7);
        drawables[8] = getResources().getDrawable(R.drawable.heart8);

        if (backLayout != null) {
            backLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputBoard.collapseBoard();
                    inputBoard.setVisibility(View.INVISIBLE);
                    buttonBoard.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            });
        }

        liveChatListAdapter = new LiveChatListAdapter();
        newMessage = new NewMessageHint(this, liveChatListView);
        liveChatListView.setAdapter(liveChatListAdapter);
        liveClient.joinLiveChatRoom(targetId, liveUrl, liveSurfaceView, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                InformationNotificationMessage content = InformationNotificationMessage.
                        obtain(getResources().getString(R.string.live_join_chatroom));
                Message msg = Message.obtain(targetId, conversationType, content);
                RongIM.getInstance().sendMessage(msg);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d(TAG, "onError errorCode = " + errorCode);
            }
        });
        liveClient.setStreamListener(new StreamListener() {
            @Override
            public void onError(StreamError error) {
            }

            @Override
            public void onPrepared() {
            }

            @Override
            public void onCompletion() {
            }
        });

        View input = findViewById(R.id.icon_input);
        if (input != null) {
            input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonBoard.setVisibility(View.INVISIBLE);
                    inputBoard.setVisibility(View.VISIBLE);
                }
            });
        }

        View gift = findViewById(R.id.icon_gift);
        if (gift != null) {
            gift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftMessage content = new GiftMessage("2");
                    Message msg = Message.obtain(targetId, conversationType, content);
                    RongIM.getInstance().sendMessage(msg);
                }
            });
        }

        View heart = findViewById(R.id.icon_heart);
        if (heart != null) {
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addHeart(backLayout);
                    GiftMessage content = new GiftMessage("1");
                    Message msg = Message.obtain(targetId, conversationType, content);
                    RongIM.getInstance().sendMessage(msg);
                }
            });
        }

        inputBoard.setInputBoardClickListener(new IInputBoardClickListener() {
            @Override
            public void onSendToggleClick(View v, String text) {
                Message message = Message.obtain(targetId, conversationType, TextMessage.obtain(text));
                RongIM.getInstance().sendMessage(message);
            }

            @Override
            public void onEditTextClick(EditText editText) {
            }

            @Override
            public void onImageSendResult(List<Uri> selectedImages) {
            }

            @Override
            public void onLocationSendResult() {
            }

            @Override
            public boolean onSwitchToggleClick(View v, ViewGroup inputBoard) {
                return false;
            }

            @Override
            public void onAudioInputToggleTouch(View v, MotionEvent event) {
            }

            @Override
            public boolean onEmoticonToggleClick(View v, ViewGroup emotionBoard) {
                return false;
            }

            @Override
            public boolean onPluginToggleClick(View v, ViewGroup pluginBoard) {
                return false;
            }
        });
    }

    private void addHeart(RelativeLayout layout) {
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(drawables[random.nextInt(9)]);
        imageView.setLayoutParams(lp);

        layout.addView(imageView);

        Animator set = getEnterAnimator(imageView);
        set.addListener(new AnimEndListener(layout, imageView));
        set.start();
    }

    private AnimatorSet getEnterAnimator(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        ObjectAnimator tranY = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, 0f, -1200f);
        ObjectAnimator tranX = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, 0f, random.nextInt(400) * -1);

        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(5000);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY, tranX, tranY);
        enter.setTarget(target);
        return enter;
    }

    @Override
    public void onBackPressed() {
        RongIM.getInstance().logout();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        liveClient.stopPlay();
        super.onDestroy();
    }

    public void onEventMainThread(BusEvent.MessageReceived event) {
        Log.d(TAG, "BusEvent.MessageReceived left = " + event.left);
        Message msg = event.message;
        if (targetId.equals(msg.getTargetId()) && conversationType == Conversation.ConversationType.CHATROOM) {
            liveChatListAdapter.addMessage(msg);
            liveChatListAdapter.notifyDataSetChanged();
            newMessage.messageArrived();
        }
    }

    public void onEventMainThread(BusEvent.MessageSent event) {
        Log.d(TAG, "BusEvent.MessageSent");
        Message msg = event.message;
        if (targetId.equals(msg.getTargetId()) && conversationType == Conversation.ConversationType.CHATROOM) {
            int errorCode = event.code;
            if (errorCode == 0) {
                liveChatListAdapter.addMessage(msg);
                liveChatListAdapter.notifyDataSetChanged();
                newMessage.messageArrived();
            } else {
                Toast toast = Toast.makeText(this, R.string.live_send_failed, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;
        private RelativeLayout layout;

        public AnimEndListener(RelativeLayout layout, View target) {
            this.layout = layout;
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            layout.removeView((target));
        }
    }

    private class NewMessageHint {
        private TextView hint;
        private ListView listView;
        private boolean isActive;

        public NewMessageHint(AppCompatActivity root, ListView bondView) {
            listView = bondView;

            hint = (TextView) root.findViewById(R.id.new_message);
            hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.setSelection(listView.getBottom());
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        hint.setVisibility(View.GONE);
                        isActive = false;
                        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    } else {
                        isActive = true;
                        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                    }
                }
            });
        }

        public void messageArrived() {
            if (isActive) {
                hint.setVisibility(View.VISIBLE);
            }
        }
    }
}
