package cn.rongcloud.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.rongcloud.live.R;
import cn.rongcloud.live.RongLiveApi;
import cn.rongcloud.live.controller.RcLog;
import cn.rongcloud.live.fakeserver.FakeServer;
import cn.rongcloud.live.fakeserver.HttpUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private RongLiveApi rongApi = RongLiveApi.getInstance();
    private Button btnTV;
    private Button btnLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        btnTV = (Button) findViewById(R.id.btn_tv);
        btnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeLogin("张三", "123456", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
            }
        });

        btnLive = (Button) findViewById(R.id.btn_live);
        btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeLogin("张三", "123456", "rtmp://vlive3.rtmp.cdn.ucloud.com.cn/ucloud/8969");
            }
        });
    }

    private void fakeLogin(String id, String password, final String url) {
        final UserInfo user = FakeServer.getLoginUser(id, password);
        FakeServer.getToken(user, new HttpUtil.OnResponse() {
            @Override
            public void onResponse(int code, String body) {
                if (code != 200) {
                    Toast.makeText(LoginActivity.this, body, Toast.LENGTH_SHORT).show();
                    return;
                }

                String token;
                try {
                    JSONObject jsonObj = new JSONObject(body);
                    token = jsonObj.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Token 解析失败!", Toast.LENGTH_SHORT).show();
                    return;
                }

                rongApi.connect(token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        RcLog.d(TAG, "connect onTokenIncorrect");
                        // 检查appKey 与token是否匹配.
                    }

                    @Override
                    public void onSuccess(String userId) {
                        RcLog.d(TAG, "connect onSuccess");
                        rongApi.setCurrentUser(user);
                        Intent intent = new Intent(LoginActivity.this, LiveShowActivity.class);
                        intent.putExtra(LiveShowActivity.LIVE_URL, url);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        RcLog.d(TAG, "connect onError = " + errorCode);
                        // 根据errorCode 检查原因.
                    }
                });
            }
        });
    }
}
