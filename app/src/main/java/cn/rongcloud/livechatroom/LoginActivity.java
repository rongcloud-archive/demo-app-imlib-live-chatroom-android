package cn.rongcloud.livechatroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.rongcloud.livechatroom.livekit.RongIM;
import cn.rongcloud.livechatroom.util.HttpUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private boolean requesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.rongcloud.livechatroom.R.layout.activity_login);
        initView();
    }

    private void initView() {
        View btn_tv = findViewById(R.id.btn_tv);
        if (btn_tv != null) {
            btn_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requesting) {
                        return;
                    }

                    requesting = true;
                    UserInfo userInfo = UserList.getInstance().getRandomUserInfo();
                    RongIM.getInstance().setCurrentUserInfo(userInfo);
                    String content = "userId=" + userInfo.getUserId() + "&name=" + userInfo.getName() + "&portraitUri=" + userInfo.getPortraitUri();
                    try {
                        HttpUtil.post(HttpUtil.REQUEST_GET_TOKEN, content, new HttpUtil.OnResponse() {
                            @Override
                            public void onResponse(int code, String body) {
                                Log.d(TAG, "code = " + code + "; body = " + body);
                                try {
                                    JSONObject jsonObj = new JSONObject(body);
                                    connectServer(jsonObj.getString("token"), "rtmp://live.hkstv.hk.lxdns.com/live/hks");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        View btn_live = findViewById(R.id.btn_live);
        if (btn_live != null) {
            btn_live.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requesting) {
                        return;
                    }

                    requesting = true;
                    UserInfo userInfo = UserList.getInstance().getRandomUserInfo();
                    RongIM.getInstance().setCurrentUserInfo(userInfo);
                    String content = "userId=" + userInfo.getUserId() + "&name=" + userInfo.getName() + "&portraitUri=" + userInfo.getPortraitUri();
                    try {
                        HttpUtil.post(HttpUtil.REQUEST_GET_TOKEN, content, new HttpUtil.OnResponse() {
                            @Override
                            public void onResponse(int code, String body) {
                                Log.d(TAG, "code = " + code + "; body = " + body);
                                try {
                                    JSONObject jsonObj = new JSONObject(body);
                                    connectServer(jsonObj.getString("token"), "rtmp://vlive3.rtmp.cdn.ucloud.com.cn/ucloud/8969");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void connectServer(String token, final String url) {
        RongIM.getInstance().connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d(TAG, "——onTokenIncorrect—-");
                requesting = false;
            }

            @Override
            public void onSuccess(String userId) {
                Log.d(TAG, "——onSuccess—-" + userId);
                RongIM.getInstance().startConversation(LoginActivity.this, Conversation.ConversationType.CHATROOM,
                        "ChatRoom01", url);
                requesting = false;
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "——onError—-" + errorCode);
                requesting = false;
            }
        });
    }
}
