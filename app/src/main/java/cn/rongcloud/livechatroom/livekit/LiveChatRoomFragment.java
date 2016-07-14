package cn.rongcloud.livechatroom.livekit;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.rongcloud.livechatroom.R;
import cn.rongcloud.livechatroom.livekit.ksyplayer.LiveClient;
import cn.rongcloud.livechatroom.livekit.ksyplayer.StreamListener;
import cn.rongcloud.livechatroom.livekit.ksyplayer.VideoSurfaceView;
import cn.rongcloud.livechatroom.livekit.model.BusEvent;
import cn.rongcloud.livechatroom.livekit.template.GiftMessage;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.toolkit.IInputBoardClickListener;
import io.rong.toolkit.InputBar;
import io.rong.toolkit.RongInputBoard;

public class LiveChatRoomFragment extends Fragment implements RongIMClient.OnReceiveMessageListener, Handler.Callback {

    private static final String TAG = "LiveChatRoomFragment";

    private VideoSurfaceView liveSurfaceView;
    private ListView liveChatListView;
    private LiveChatListAdapter liveChatListAdapter;
    private NewMessageHint newMessage;
    private RongInputBoard rongInputBoard;

    private Conversation.ConversationType conversationType;
    private String targetId;
    private String liveUrl;
    private LiveClient liveClient = new LiveClient();

    private ImageView gift_flower;
    private ImageView gift_applaud;
    private boolean longClicked;
    private Handler handler = new Handler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongIM.getInstance().getEventBus().register(this);

        Uri uri = getActivity().getIntent().getData();
        String typeStr = uri.getLastPathSegment().toUpperCase();
        conversationType = Conversation.ConversationType.valueOf(typeStr);
        targetId = uri.getQueryParameter("targetId");
        liveUrl = uri.getQueryParameter("liveUrl");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.rc_fragment_live_chatroom, container, false);
        liveSurfaceView = (VideoSurfaceView) root.findViewById(R.id.live_surface_view);
        liveChatListView = (ListView) root.findViewById(R.id.live_chatlist);
        newMessage = new NewMessageHint(root, liveChatListView);
        rongInputBoard = (RongInputBoard) root.findViewById(R.id.input_board);
        rongInputBoard.setActivity(getActivity());
        gift_flower = (ImageView) root.findViewById(R.id.icon_gift);
        gift_applaud = (ImageView) root.findViewById(R.id.icon_gift);

        rongInputBoard.setInputBarStyle(InputBar.Style.STYLE_CONTAINER);
        liveChatListAdapter = new LiveChatListAdapter();
        liveChatListView.setAdapter(liveChatListAdapter);

        liveClient.joinLiveChatRoom(targetId, liveUrl, liveSurfaceView, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                UserInfo info = RongIM.getInstance().getCurrentUserInfo();
                String infoText = getResources().getString(R.string.live_join_chatroom);
                Log.d(TAG, "infoText = " + infoText);
                InformationNotificationMessage content = InformationNotificationMessage.obtain(infoText);
                Message msg = Message.obtain(targetId, conversationType, content);
                RongIM.getInstance().sendMessage(msg);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

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

        rongInputBoard.setInputBoardClickListener(new IInputBoardClickListener() {
            @Override
            public void onSendToggleClick(View v, String text) {
                Message message = Message.obtain(targetId, conversationType, TextMessage.obtain(text));
                UserInfo myInfo = RongIM.getInstance().getCurrentUserInfo();
                message.getContent().setUserInfo(myInfo);
                message.setMessageDirection(Message.MessageDirection.SEND);
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

        gift_flower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    longClicked = true;
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            while (longClicked) {
                                handler.sendEmptyMessage(0);
                                try {
                                    sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longClicked = false;
                }
                return true;
            }
        });

        gift_applaud.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    longClicked = true;
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            while (longClicked) {
                                handler.sendEmptyMessage(1);
                                try {
                                    sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    longClicked = false;
                }
                return true;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        liveClient.stopPlay();
        super.onDestroyView();
    }

    @Override
    public boolean onReceived(Message message, int left) {
        Log.d(TAG, "onReceived");
        if (message.getConversationType() == conversationType && message.getTargetId().equals(targetId)) {
            liveChatListAdapter.addMessage(message);
            liveChatListAdapter.notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        String type = Integer.toString(msg.what);
        Message message = Message.obtain(targetId, conversationType, new GiftMessage(type));
        message.getContent().setUserInfo(RongIM.getInstance().getCurrentUserInfo());
        RongIM.getInstance().sendMessage(message);
        return false;
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
                Toast toast = Toast.makeText(getActivity(), R.string.live_send_failed, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private class NewMessageHint {
        private TextView hint;
        private ListView listView;
        private boolean isActive;

        public NewMessageHint(View root, ListView bondView) {
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