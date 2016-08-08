package cn.rongcloud.live.fakeserver;

import android.net.Uri;
import android.support.v4.util.ArrayMap;

import java.util.Random;

import io.rong.imlib.model.UserInfo;

public class FakeServer {

    /**
     * 集成融云应用的唯一标识, 前端后台都需要用到.
     */
    public static final String APP_KEY = "e5t4ouvptkm2a";

    /**
     * 务必妥善保存, 不能放在应用端, 否则可以被反编译获得.
     */
    public static final String APP_SECRET = "vB4FakXm8f68";

    private static ArrayMap<String, String> userList;

    static {
        userList = new ArrayMap<>();
        userList.put("安陵容", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/anlingrong.jpg");
        userList.put("果郡王", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/guojunwang.jpg");
        userList.put("华妃", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/huafei.jpg");
        userList.put("皇后", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/huanghou.jpg");
        userList.put("皇上", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/huangshang.jpg");
        userList.put("沈眉庄", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/shenmeizhuang.jpg");
        userList.put("甄嬛", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/zhenhuan.jpg");
    }

    public static String getAppKey() {
        return APP_KEY;
    }

    /**
     * 获取融云Token, 通过调用融云ServerApi获得.
     *
     * @param user
     * @param callback
     */
    public static void getToken(UserInfo user, HttpUtil.OnResponse callback) {
        final String HTTP_GET_TOKEN = "https://api.cn.ronghub.com/user/getToken.json";
        HttpUtil.Header header = HttpUtil.getRcHeader(APP_KEY, APP_SECRET);
        String body = "userId=" + user.getUserId() + "&name=" + user.getName() + "&portraitUri=" + user.getPortraitUri();
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.post(HTTP_GET_TOKEN, header, body, callback);
    }

    /**
     * 模拟用户登录, 返回的用户信息可以被扩展.
     *
     * @param id
     * @param password
     * @return 返回当前用户信息对象.
     */
    public static UserInfo getLoginUser(String id, String password) {
        long time = System.currentTimeMillis();
        Random ran = new Random(time);
        int n = ran.nextInt(userList.size());
        return new UserInfo(Long.toString(time), userList.keyAt(n), Uri.parse(userList.valueAt(n)));
    }
}