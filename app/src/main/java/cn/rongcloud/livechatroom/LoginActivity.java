package cn.rongcloud.livechatroom;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.rongcloud.livechatroom.livekit.RongIM;
import cn.rongcloud.livechatroom.util.HttpUtil;
import cn.rongcloud.livechatroom.widget.LoginEdit;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "LoginActivity";

    private LoginEdit user_id;
    private LoginEdit nickname;
    private LoginEdit chatroom_id;
    private LoginEdit live_url;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.rongcloud.livechatroom.R.layout.activity_login);
        initView();
    }

    private void initView() {
        user_id = (LoginEdit) findViewById(R.id.user_id);
        user_id.setHint("用户的登录名");
        user_id.setTextChangedListener(this);

        nickname = (LoginEdit) findViewById(R.id.nickname);
        nickname.setHint("昵称");
        nickname.setTextChangedListener(this);

        chatroom_id = (LoginEdit) findViewById(R.id.chatroom_id);
        chatroom_id.setHint("设置你要加入的聊天室ID");
        chatroom_id.setText("ChatRoom01");
        chatroom_id.setTextChangedListener(this);

        live_url = (LoginEdit) findViewById(R.id.live_url);
        live_url.setHint("直播流地址");
        live_url.setText("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        live_url.setTextChangedListener(this);

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "userId=" + user_id.getText() + "&name=" + nickname.getText() + "&portraitUri=";
                try {
                    HttpUtil.post(HttpUtil.REQUEST_GET_TOKEN, content, new HttpUtil.OnResponse() {
                        @Override
                        public void onResponse(int code, String body) {
                            Log.d(TAG, "code = " + code + "; body = " + body);
                            try {
                                JSONObject jsonObj = new JSONObject(body);
                                connectServer(jsonObj.getString("token"));
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean notEmpty = !user_id.getText().isEmpty() && !nickname.getText().isEmpty()
                && !chatroom_id.getText().isEmpty() && !live_url.getText().isEmpty();
        loginBtn.setEnabled(notEmpty);
    }

    private void connectServer(String token) {
        RongIM.getInstance().connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d(TAG, "——onTokenIncorrect—-");
            }

            @Override
            public void onSuccess(String userId) {
                Log.d(TAG, "——onSuccess—-" + userId);
                App.setCurrentUserInfo(new UserInfo(user_id.getText(), nickname.getText(), Uri.parse("")));
                RongIM.getInstance().startConversation(LoginActivity.this, Conversation.ConversationType.CHATROOM,
                        chatroom_id.getText(), live_url.getText());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "——onError—-" + errorCode);
            }
        });
    }
}
