package com.itheima.googleplay.manager;

/**
 * 描述	      组合和下载相关的参数
 */
public class DownLoadInfo {

    public String downloadUrl;//下载地址，问号后面的
    public String savePath;//apk的存储路径




    public String packageName;

    public int curState = DownLoadManager.STATE_UNDOWNLOAD;//默认是未下载
    public long max;
    public long progress;

    public Runnable downLoadTask;
}
