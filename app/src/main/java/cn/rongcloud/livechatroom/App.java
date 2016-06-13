package cn.rongcloud.livechatroom;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import cn.rongcloud.livechatroom.livekit.RongIM;
import cn.rongcloud.livechatroom.livekit.template.GiftMessage;
import cn.rongcloud.livechatroom.livekit.template.GiftMessageTemplate;
import cn.rongcloud.livechatroom.util.HttpUtil;
import io.rong.imlib.model.UserInfo;

public class App extends Application {

    private static UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this, HttpUtil.APP_KEY);
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
                RongIM.getInstance().registerMessageType(GiftMessage.class);
                RongIM.getInstance().registerMessageTemplate(new GiftMessageTemplate());
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static UserInfo getCurrentUserInfo() {
        return userInfo;
    }

    public static void setCurrentUserInfo(UserInfo info) {
        userInfo = info;
    }
}