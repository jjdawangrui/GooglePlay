package com.itheima.googleplay.conf;

import com.itheima.googleplay.utils.LogUtils;

public class Constants {
    /*
    LogUtils.LEVEL_ALL:打开日志(显示所有的日志输出)
    LogUtils.LEVEL_OFF:关闭日志(屏蔽所有的日志输出)
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final long PROTOCOLTIMEOUT = 5 * 60 * 1000;//5分钟，每个公司或者每个协议，时间都不一样，这里模拟一下

    public static final class URLS{
        public static final String BASEURL = "http://10.0.2.2:8080/GooglePlayServer/";
        public static final String IMGBASEURL = BASEURL + "image?name=";
    }
}
