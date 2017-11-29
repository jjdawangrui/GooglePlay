package com.itheima.googleplay.base;

import android.app.Application;
import android.content.Context;
import android.os.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 描述	      全局单例
 * 描述	      注意：需要在清单文件里面进行配置
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static int mMainThreadId;

    /**
     * 内存缓存的集合
     * 存储结构放到哪里？
        大家都可以取到的地方——>全局可访问
        单利
            1.自己创建一个单利
            2.继承Application创建一个自己的类MyApplication，原来是用来全局的意思
     */
    private Map<String, String> mMemProtocolCacheMap = new HashMap<>();
    public Map<String, String> getMemProtocolCacheMap() {
        return mMemProtocolCacheMap;
    }

//     * 得到上下文C
    public static Context getContext() {
        return mContext;
    }

//     * 得到主线程里面的创建的一个hanlder
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

//     * 得到主线程的线程id
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
