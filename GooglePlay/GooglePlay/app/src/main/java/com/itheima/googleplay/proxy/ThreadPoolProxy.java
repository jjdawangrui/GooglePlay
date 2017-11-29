package com.itheima.googleplay.proxy;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池代理类,替线程池完成相关操作
 * 1.代理模式就是多一个代理类出来，替原对象进行一些操作
 * 2.只需提供使用原对象时候真正关心的方法(提交任务,执行任务,移除任务)
 * 3.可以对原对象方法进行增强
 * <p>
 * 提交任务和执行任务的区别?有返回值
 * submit-->
 * execute-->没有返回值
 * <p>
 * Future对象是干嘛的?
 * 1.可以得到异步任务执行完成之后的结果
 * 2.校验任务是否执行完成,等待完成,接收结果
 * 3.其中get方法可以接收结果,get方法还是一个阻塞方法,还可以得到任务执行过程中抛出的异常信息
 */

//是ThreadPoolExecutor的代理，相当于买房子的时候，找中介
public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;//核心池的大小
    private int mMaxPoolSize;//最大线程数

    //外部通过构造方法传进来值
    public ThreadPoolProxy(int mMaxPoolSize, int mCorePoolSize) {
        this.mCorePoolSize = mCorePoolSize;
        this.mMaxPoolSize = mMaxPoolSize;
    }

    /**
     * 提交和执行的区别？
     *      submit有返回值
     * Future是干嘛的？
     *      1.可以得到异步任务执行完后的结果
     *      2.校验任务是否完成，等待完成，接收结果
     *      3.其中get方法可以接收结果，还是阻塞方法，还可以得到任务执行过程中抛出的异常信息
     */
    public Future submit(Runnable task){
        initThreadPoolExecutor();
        Future<?> future = mExecutor.submit(task);
        return future;
    }
    public void execute(Runnable task){
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }
    public void remove(Runnable task){
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }

    public void initThreadPoolExecutor() {
        //双重检查加锁 : 只有第一次实例化的时候才启用同步机制,提高了性能
        /**
         * 极端的时候，多线程很多，同时进来了4个线程，都为空，走到加锁，然后排队
         第一个线程走完，创建了实例，另外3个就不走了，return
         后面再进来的线程，连第一个判断都不走了，直接return
         */
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    long keepAliveTime = 0;//
                    TimeUnit unit = TimeUnit.MILLISECONDS;//单位
                    BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque();//无界队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();//
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();//异常不做处理

                    mExecutor = new ThreadPoolExecutor(
                            mCorePoolSize,mMaxPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);
                }
            }
        }
    }
}
