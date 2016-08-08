package cn.rongcloud.live;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

import cn.rongcloud.live.controller.EmojiManager;
import cn.rongcloud.live.ui.message.BaseMsgView;
import cn.rongcloud.live.ui.message.GiftMessage;
import cn.rongcloud.live.ui.message.GiftMsgView;
import cn.rongcloud.live.ui.message.InfoMsgView;
import cn.rongcloud.live.ui.message.TextMsgView;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;

public class RongLiveApi implements RongIMClient.OnReceiveMessageListener {

    private static RongLiveApi instance;
    private static HashMap<Class<? extends MessageContent>, Class<? extends BaseMsgView>> msgViewMap = new HashMap<>();

    public static final int MESSAGE_SEND_ERROR = -1;

    public static final int MESSAGE_ARRIVED = 0;
    public static final int MESSAGE_SENT = 1;

    private static ArrayList<Handler> eventHandlerList = new ArrayList<>();
    private static UserInfo currentUser;
    private static String currentRoomId;

    public static void init(Context context, String appKey) {
        EmojiManager.init(context);
        RongIMClient.init(context, appKey);
    }

    public static RongLiveApi getInstance() {
        if (instance == null) {
            instance = new RongLiveApi();
        }
        return instance;
    }

    private RongLiveApi() {
        RongIMClient.setOnReceiveMessageListener(this);

        registerMessageType(GiftMessage.class);
        registerMessageView(TextMessage.class, TextMsgView.class);
        registerMessageView(InformationNotificationMessage.class, InfoMsgView.class);
        registerMessageView(GiftMessage.class, GiftMsgView.class);
    }

    /**
     * 设置当前登录用户, 通常由注册生成, 通过应用服务器来返回.
     *
     * @param user 当前用户
     */
    public static void setCurrentUser(UserInfo user) {
        currentUser = user;
    }

    /**
     * 获得当前登录用户
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    /**
     * 注册自定义消息
     *
     * @param msgType 自定义消息类型
     */
    public static void registerMessageType(Class<? extends MessageContent> msgType) {
        try {
            RongIMClient.registerMessageType(msgType);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册消息展示界面类
     *
     * @param msgContent 消息类型
     * @param msgView    对应的界面展示类
     */
    public static void registerMessageView(Class<? extends MessageContent> msgContent, Class<? extends BaseMsgView> msgView) {
        msgViewMap.put(msgContent, msgView);
    }

    /**
     * 获取注册的消息展示类
     * @param msgContent
     * @return
     */
    public static Class<? extends BaseMsgView> getRegisterMessageView(Class<? extends MessageContent> msgContent) {
        return msgViewMap.get(msgContent);
    }

    public static void connect(String token, final RongIMClient.ConnectCallback callback) {
        RongIMClient.connect(token, callback);
    }

    public static void disconnect() {
        RongIMClient.getInstance().disconnect();
    }

    public static void joinChatRoom(String roomId, final RongIMClient.OperationCallback callback) {
        currentRoomId = roomId;
        RongIMClient.getInstance().joinChatRoom(currentRoomId, 2, callback);
    }

    public static void quitChatRoom(final RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().quitChatRoom(currentRoomId, callback);
    }

    public static void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener) {

    }

    public static void sendMessage(final MessageContent msgContent) {
        msgContent.setUserInfo(currentUser);
        Message msg = Message.obtain(currentRoomId, Conversation.ConversationType.CHATROOM, msgContent);
        RongIMClient.getInstance().sendMessage(msg, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                handleEvent(MESSAGE_SENT, msgContent);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                handleEvent(MESSAGE_SEND_ERROR, errorCode.getValue(), 0, msgContent);
            }
        }, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
            }
        });
    }

    public static void addEventHandler(Handler handler) {
        if (!eventHandlerList.contains(handler)) {
            eventHandlerList.add(handler);
        }
    }

    public static void removeEventHandler(Handler handler) {
        eventHandlerList.remove(handler);
    }

    @Override
    public boolean onReceived(Message message, int i) {
        handleEvent(MESSAGE_ARRIVED, message.getContent());
        return false;
    }

    private static void handleEvent(int what) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            handler.sendMessage(m);
        }
    }

    private static void handleEvent(int what, Object obj) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            m.obj = obj;
            handler.sendMessage(m);
        }
    }

    private static void handleEvent(int what, int arg1, int arg2, Object obj) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            m.arg1 = arg1;
            m.arg2 = arg2;
            m.obj = obj;
            handler.sendMessage(m);
        }
    }
}
