package cn.rongcloud.livechatroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.rongcloud.livechatroom.livekit.LiveChatRoomFragment;
import cn.rongcloud.livechatroom.livekit.RongIM;
import io.rong.imlib.model.UserInfo;

public class LiveChatActivity extends AppCompatActivity {

    private LiveChatRoomFragment liveChatRoomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livechat);
        liveChatRoomFragment = (LiveChatRoomFragment) getSupportFragmentManager().findFragmentById(R.id.live_chatroom_fragment);

        RongIM.getInstance().setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                return App.getCurrentUserInfo();
            }
        });
    }

    @Override
    public void onBackPressed() {
        RongIM.getInstance().logout();
        super.onBackPressed();
    }
}
