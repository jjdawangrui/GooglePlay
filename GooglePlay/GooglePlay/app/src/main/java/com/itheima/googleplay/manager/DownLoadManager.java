package com.itheima.googleplay.manager;

import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.proxy.ThreadPoolProxyFactory;
import com.itheima.googleplay.utils.CommonUtils;
import com.itheima.googleplay.utils.FileUtils;
import com.itheima.googleplay.utils.HttpUtils;
import com.itheima.googleplay.utils.IOUtils;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raynwang on 2017/7/25.
 * 下载管理器，负责和下载相关的逻辑
 * 做成单利
 */
/**
 * 描述	      1.定义常量,而且需要`时刻记录`当前的状态
 * 描述	      2.根据ItemBean返回一个对应的DownLoadInfo
 */

public class DownLoadManager {

    public static final int STATE_UNDOWNLOAD = 0;//未下载
    public static final int STATE_DOWNLOADING = 1;//下载中
    public static final int STATE_PAUSEDOWNLOAD = 2;//暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;//等待下载
    public static final int STATE_DOWNLOADFAILED = 4;//下载失败
    public static final int STATE_DOWNLOADED = 5;//下载完成
    public static final int STATE_INSTALLED = 6;//已安装

    private static DownLoadManager instance;

    private Map<String ,DownLoadInfo> mCacheDownLoadInfoMap = new HashMap<>();



    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * 异步下载
     * 用户点击了下载按钮的时候
     * 传进来一个下载信息
     */
    public void downLoad(DownLoadInfo downLoadInfo) {
        mCacheDownLoadInfoMap.put(downLoadInfo.packageName,downLoadInfo);//进行过下载的，放进集合中

        /*############### 当前状态:未下载 ###############*/
        /************* 1.在还没有创建下载任务时，下载状态应该是未下载 *************/
        downLoadInfo.curState = STATE_UNDOWNLOAD;
        notifyObservers(downLoadInfo);
        /*#######################################*/

        /*
        任务提交给线程池之后,状态有两种情况
            1.任务如果直接交给了工作线程-->下载中-->状态会切换为下载中
            2.任务如果交给了任务队列-->等待中-->正好保持
         */
        /************* 2.由于不清楚任务会被放到工作线程还是等待队列，
         *   所以我们无法确认状态是否是下载中，所以我们需要将下载状态预设为等待下载 *************/
        /*############### 当前状态:等待中 ###############*/
        downLoadInfo.curState = STATE_WAITINGDOWNLOAD;
        notifyObservers(downLoadInfo);
        /*#######################################*/

        DownLoadTask downLoadTask = new DownLoadTask(downLoadInfo);
        ThreadPoolProxyFactory.getmDownloadThreadPoolProxy().submit(downLoadTask);

        downLoadInfo.downLoadTask = downLoadTask;//这一手给 下载bean 里面的任务赋值，再后面移除的时候要用
    }

    private class DownLoadTask implements Runnable {
        private DownLoadInfo mDownLoadInfo;

        //通过构造方法，让外部给 下载bean 赋值
        private DownLoadTask(DownLoadInfo downLoadInfo) {
            mDownLoadInfo = downLoadInfo;
        }

        @Override
        public void run() {

            /************* 3.当run方法被执行时，我们可以确认这个下载任务已经进入到了工作线程开始执行线程任务，
             此时可以将下载状态置为下载中 *************/
        /*############### 当前状态:下载中 ###############*/
            mDownLoadInfo.curState = STATE_DOWNLOADING;
            notifyObservers(mDownLoadInfo);
        /*#######################################*/

            //真正开始在子线程中下载apk
            OkHttpClient okHttpClient = new OkHttpClient();

            long initRange = 0;//已有数据的长度
            File saveApk = new File(mDownLoadInfo.savePath);
            if (saveApk.exists()) {//如果存在，就说明已经下载过
                initRange = saveApk.length();
            }
            //③需要初始化进度
            mDownLoadInfo.progress = initRange;

            //http://localhost:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk&range=0
            String url = Constants.URLS.BASEURL + "download";
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("name", mDownLoadInfo.downloadUrl);
            paramsMap.put("range", initRange + "");//① 发起请求的时候,初始化range参数(已有数据的长度)
            String urlParamsByMap = HttpUtils.getUrlParamsByMap(paramsMap);
            url = url + "?" + urlParamsByMap;//一定注意加上问号

            Request request = new Request.Builder().get().url(url).build();
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {//是成功的，系统Okhttp自带

                    inputStream = response.body().byteStream();//字节流

                    outputStream = new FileOutputStream(saveApk,true);//②,写文件的时候,以追加的方式去写，用另一个构造

                    boolean isPause = false;
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {

                        if (mDownLoadInfo.curState == STATE_PAUSEDOWNLOAD) {//什么时候暂停的？？？
                            isPause = true;
                            break;
                        }

                        outputStream.write(buffer, 0, len);

                        //处理进度
                        mDownLoadInfo.progress += len;

                        /************* 4.在下载过程中，我们需要通过一个while()循环去不停的读取服务器返回的数据，
                         * 每读取一次数据，下载状态依旧需要置为下载中，只不过此时下载进度需要更新 *************/
                         /*############### 当前状态:下载中 ###############*/
                        mDownLoadInfo.curState = STATE_DOWNLOADING;
                        notifyObservers(mDownLoadInfo);
                         /*#######################################*/
                        //下载完成,按照while循环的语法,就会再一次来到((len = in.read(buffer)) != -1),
                        //但是在okHttp里面,(只是在okhttp里面),看源码发现,如果这个时候再去读,读出来的结果是-1的时候会抛出异常

                        if (saveApk.length() == mDownLoadInfo.max) {//下载完成,提前跳出while循环
                            break;
                        }
                    }
                    if (isPause) {
                        //说明用户点击了暂停下载.来到这个地方
                    } else {
                        /************* 5.当下载完成时，将下载状态置为下载完成 ************/
                        //写完了,下载完成了
                    /*############### 当前状态:下载完成 ###############*/
                        mDownLoadInfo.curState = STATE_DOWNLOADED;
                        notifyObservers(mDownLoadInfo);
                    /*#######################################*/
                    }
                }else{
                    /************* 6.如果请求服务器失败没有返回成功code，那么我们的状态需要置为下载失败 *************/
                    /*############### 当前状态:下载失败 ###############*/
                    mDownLoadInfo.curState = STATE_DOWNLOADFAILED;
                    notifyObservers(mDownLoadInfo);
                    /*#######################################*/
                }
            } catch (IOException e) {
                e.printStackTrace();
                /************* 7.如果在下载的过程中出现了异常，那么我们的状态需要置为下载失败 *************/
                 /*############### 当前状态:下载失败 ###############*/
                mDownLoadInfo.curState = STATE_DOWNLOADFAILED;
                notifyObservers(mDownLoadInfo);
                /*#######################################*/
            }finally {
                IOUtils.close(inputStream);
                IOUtils.close(outputStream);
            }
        }
    }

    /**
     * 根据详情信息得到对应的 下载bean
     * @param data
     * @return
     */
    public DownLoadInfo getDownLoadInfo(ItemBean data){
        DownLoadInfo downLoadInfo = new DownLoadInfo();
        //常规赋值
        String dir = FileUtils.getDir("apk");//工具类，自动拼接成路径String
        String fileName = data.packageName + ".apk";
        File saveFile = new File(dir, fileName);
        downLoadInfo.savePath = saveFile.getAbsolutePath();

        downLoadInfo.packageName = data.packageName;
        downLoadInfo.downloadUrl = data.downloadUrl;
        downLoadInfo.max = data.size;
        downLoadInfo.progress = 0;

        //重点赋值
        /*
        未下载		-->默认情况

        下载中   -->用户点击了下载操作
        暂停下载 -->用户点击了下载操作
        等待下载 -->用户点击了下载操作
        下载失败 -->用户点击了下载操作
        下载完成 -->用户点击了下载操作

        下载完成 -->用户即使不点击下载操作,也有可能已经下载完成

        已安装  -->用户即使不点击下载操作,也有可能已经安装

         */

        //已安装  -->用户即使不点击下载操作,也有可能已经安装
        if (CommonUtils.isInstalled(UIUtils.getContext(), downLoadInfo.packageName)) {
            downLoadInfo.curState = STATE_INSTALLED;
            return downLoadInfo;
        }

        //下载完成 -->用户即使不点击下载操作,也有可能已经下载完成
        if (saveFile.exists() && saveFile.length() == data.size) {
            downLoadInfo.curState = STATE_DOWNLOADED;
            return downLoadInfo;
        }

        if (mCacheDownLoadInfoMap.containsKey(data.packageName)){
            //用户肯定点击了itemBean所对应详情界面里面的下载按钮
            return mCacheDownLoadInfoMap.get(data.packageName);//如果包含，就从集合里面取出来
        }

        //未下载，默认状态就是未下载，所以直接return
//        downLoadInfo.curState = STATE_UNDOWNLOAD;
        return downLoadInfo;
    }

    /*--------------- 自己实现观察者设计模式,通知DownLoadInfo ---------------*/
    //1.定义接口以及接口方法
    public interface DownLoadInfoObserver {
        void onDownLoadInfoChanged(DownLoadInfo downLoadInfo);
    }

    //2.定义集合保存接口对象
    public List<DownLoadInfoObserver> observers = new ArrayList<>();

    //3.常见方法-->添加观察者到观察者集合中
    public synchronized void addObserver(DownLoadInfoObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    //3.常见方法-->从观察者集合中移除观察者
    public synchronized void deleteObserver(DownLoadInfoObserver o) {
        observers.remove(o);
    }

    //3.常见方法-->通知所有的观察者消息已经发生改变
    public void notifyObservers(DownLoadInfo downLoadInfo) {
        for (DownLoadInfoObserver o : observers) {
            o.onDownLoadInfoChanged(downLoadInfo);
        }
    }

    /**
     * @param downLoadInfo
     * @des 暂停下载
     * @called 如果apk正在下载, 用户点击了下载按钮
     */
    public void pause(DownLoadInfo downLoadInfo) {
        /*############### 当前状态:暂停 ###############*/
        downLoadInfo.curState = STATE_PAUSEDOWNLOAD;
        notifyObservers(downLoadInfo);
        /*#######################################*/
    }

    /**
     * @param downLoadInfo
     * @des 取消下载
     * @called 如果有3个apk正在下载, 第4个进入等待中的时候, 再次被点击就是取消下载
     */
    public void cancel(DownLoadInfo downLoadInfo) {
         /*############### 当前状态:未下载 ###############*/
        downLoadInfo.curState = STATE_UNDOWNLOAD;
        notifyObservers(downLoadInfo);
        /*#######################################*/

        ThreadPoolProxyFactory.getmDownloadThreadPoolProxy().remove(downLoadInfo.downLoadTask);
    }
}
