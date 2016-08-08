package cn.rongcloud.live;

import android.app.Application;
import android.content.Context;

import cn.rongcloud.live.fakeserver.FakeServer;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        RongLiveApi.init(context, FakeServer.getAppKey());
    }

    public static Context getContext() {
        return context;
    }
}