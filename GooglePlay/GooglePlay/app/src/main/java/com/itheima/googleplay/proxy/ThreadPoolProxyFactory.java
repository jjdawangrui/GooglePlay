package com.itheima.googleplay.proxy;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by raynwang on 2017/7/11.
 */

    //ThreadPoolProxy工厂类,封装对ThreadPoolProxy创建
    //两个方法用来获取线程池代理的实例
public class ThreadPoolProxyFactory {
    //普通线程代理
    static ThreadPoolProxy mNormalThreadPoolProxy;
    //下载线程代理
    static ThreadPoolProxy mDownloadThreadPoolProxy;

    public static ThreadPoolProxy getmNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy==null){
            synchronized (ThreadPoolProxyFactory.class){
                if (mNormalThreadPoolProxy==null){
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5,5);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

    public static ThreadPoolProxy getmDownloadThreadPoolProxy() {
        if (mDownloadThreadPoolProxy==null){
            synchronized (ThreadPoolProxyFactory.class){
                if (mDownloadThreadPoolProxy==null){
                    mDownloadThreadPoolProxy = new ThreadPoolProxy(3,3);
                }
            }
        }
        return mDownloadThreadPoolProxy;
    }
}
