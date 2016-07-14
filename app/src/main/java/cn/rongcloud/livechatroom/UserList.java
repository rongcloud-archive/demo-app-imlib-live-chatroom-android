package cn.rongcloud.livechatroom;

import android.net.Uri;
import android.support.v4.util.ArrayMap;

import java.util.Random;

import io.rong.imlib.model.UserInfo;

public class UserList {

    private static UserList instance;
    private ArrayMap<String, String> userList = new ArrayMap<>();

    private UserList() {
        userList.put("安陵容", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/anlingrong.jpg");
        userList.put("果郡王", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/guojunwang.jpg");
        userList.put("华妃", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/huafei.jpg");
        userList.put("皇后", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/huanghou.jpg");
        userList.put("皇上", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/huangshang.jpg");
        userList.put("沈眉庄", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/shenmeizhuang.jpg");
        userList.put("甄嬛", "http://7xs9j5.com1.z0.glb.clouddn.com/liveapp/zhenhuan.jpg");
    }

    public static UserList getInstance() {
        if (instance == null) {
            instance = new UserList();
        }
        return instance;
    }

    public UserInfo getRandomUserInfo() {
        long time = System.currentTimeMillis();
        Random ran = new Random(time);
        int n = ran.nextInt(userList.size());
        return new UserInfo(Long.toString(time), userList.keyAt(n), Uri.parse(userList.valueAt(n)));
    }
}
