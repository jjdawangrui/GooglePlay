package com.itheima.googleplay.base;

import android.app.Application;
import android.content.Context;
import android.os.*;


/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      全局单例
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static int mMainThreadId;

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程里面的创建的一个hanlder
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 得到主线程的线程id
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {//程序的入口方法
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mMainThreadHandler = new Handler();

        //主线程的线程id
        mMainThreadId = android.os.Process.myTid();
        /**
         myTid:Thread
         myPid:Process
         myUid:User
         */
        super.onCreate();
    }
}
