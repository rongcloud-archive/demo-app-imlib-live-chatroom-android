package demo.rong.livechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import demo.rong.livechat.util.HttpUtil;
import demo.rong.livechat.widget.LoginEdit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        String content = "userId=Ray&name=Ray&portraitUri=";
        try {
            HttpUtil.post(HttpUtil.REQUEST_GET_TOKEN, content, new HttpUtil.OnResponse() {
                @Override
                public void onResponse(int code, String body) {
                    Log.d("RYM", "code = " + code + "; body = " + body);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        LoginEdit user_id = (LoginEdit) findViewById(R.id.user_id);
        user_id.setHint("用户ID:");

        LoginEdit nickname = (LoginEdit) findViewById(R.id.nickname);
        nickname.setHint("昵称:");

        LoginEdit chatroom_id = (LoginEdit) findViewById(R.id.chatroom_id);
        chatroom_id.setHint("要加入的聊天室ID:");

        LoginEdit live_url = (LoginEdit) findViewById(R.id.live_url);
        live_url.setHint("金山直播流地址:");

        Button loginBtn = (Button) findViewById(R.id.login);
    }

}
