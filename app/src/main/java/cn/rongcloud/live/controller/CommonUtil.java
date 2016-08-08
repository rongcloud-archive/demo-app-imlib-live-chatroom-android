package cn.rongcloud.live.controller;

import cn.rongcloud.live.App;

public class CommonUtil {

    public static int dip2px(float dpValue) {
        float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
