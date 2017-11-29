package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.MyApplication;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.utils.CommonUtils;
import com.itheima.googleplay.utils.FileUtils;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.PrintDownLoadInfo;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.ProgressBtn;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 应用详情页面 —— 应用下载部分
 */
public class DetailDownloadHolder extends BaseHolder<ItemBean> implements DownLoadManager.DownLoadInfoObserver {
                                                                //实现广播接收者
    @Bind(R.id.app_detail_download_btn_favo)
    Button mAppDetailDownloadBtnFavo;
    @Bind(R.id.app_detail_download_btn_share)
    Button mAppDetailDownloadBtnShare;
    @Bind(R.id.app_detail_download_btn_download)
    ProgressBtn mAppDetailDownloadBtnDownload;
    private ItemBean mItemBean;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_download, null);
        ButterKnife.bind(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        //保存数据到成员变量
        mItemBean = data;

        /*--------------- 2.根据不同的状态给用户提示 ---------------*/
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(data);

        refreshProgressBtnUI(downLoadInfo);
    }

    /**
     * 更新下载按钮的UI
     *
     * @param downLoadInfo
     */
    private void refreshProgressBtnUI(DownLoadInfo downLoadInfo) {

        int curState = downLoadInfo.curState;

        mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);

        /*
    public static final int STATE_UNDOWNLOAD = 0;//未下载
    public static final int STATE_DOWNLOADING = 1;//下载中
    public static final int STATE_PAUSEDOWNLOAD = 2;//暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;//等待下载
    public static final int STATE_DOWNLOADFAILED = 4;//下载失败
    public static final int STATE_DOWNLOADED = 5;//下载完成
    public static final int STATE_INSTALLED = 6;//已安装
         */

        /*
        状态(编程记录)  	|  给用户的提示(ui展现)
        ----------------|-----------------------
        未下载			|下载
        下载中			|显示进度条
        暂停下载			|继续下载
        等待下载			|等待中...
        下载失败 		    |重试
        下载完成 		    |安装
        已安装 			|打开
         */
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD:
                mAppDetailDownloadBtnDownload.setText("下载");
                break;
            case DownLoadManager.STATE_DOWNLOADING:

                mAppDetailDownloadBtnDownload.setIsProgressEnable(true);
                mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);

                int index = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);
                mAppDetailDownloadBtnDownload.setMax(downLoadInfo.max);
                mAppDetailDownloadBtnDownload.setProgress(downLoadInfo.progress);
                mAppDetailDownloadBtnDownload.setText(index+"%");
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:
                mAppDetailDownloadBtnDownload.setText("继续下载");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:
                mAppDetailDownloadBtnDownload.setText("等待中");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:
                mAppDetailDownloadBtnDownload.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED:
                mAppDetailDownloadBtnDownload.setIsProgressEnable(false);
                mAppDetailDownloadBtnDownload.setText("安装");
                break;
            case DownLoadManager.STATE_INSTALLED:
                mAppDetailDownloadBtnDownload.setText("打开");
                break;
            default:
                break;

        }
    }

    @OnClick(R.id.app_detail_download_btn_download)
    public void clickBtnDownLoad(View view) {
//        DownLoadInfo downloadInfo = new DownLoadInfo();
//
//        //给下载地址赋值
//        downloadInfo.downloadUrl = mItemBean.downloadUrl;
//        //给存储路径赋值
//        String dir = FileUtils.getDir("apk");
//        String fileName = mItemBean.packageName+".apk";
//        File saveApk = new File(dir,fileName);
//        downloadInfo.savePath = saveApk.getAbsolutePath();
//
//        DownLoadManager.getInstance().downLoad(downloadInfo);

        /*--------------- 3.根据不同的状态触发不同的操作 ---------------*/

        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mItemBean);
        int curState = downLoadInfo.curState;
        /*
         状态(编程记录)  | 用户行为(触发操作)
        ----------------| -----------------
        未下载			| 去下载
        下载中			| 暂停下载
        暂停下载			| 断点继续下载
        等待下载			| 取消下载
        下载失败 		    | 重试下载
        下载完成 		    | 安装应用
        已安装 			| 打开应用
         */
        switch (curState) {
            case DownLoadManager.STATE_UNDOWNLOAD:
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADING:
                doPause(downLoadInfo);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:
                doCancel(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADED:
                doInstall(downLoadInfo);
                break;
            case DownLoadManager.STATE_INSTALLED:
                doOpen(downLoadInfo);
                break;
            default:
                break;

        }
    }

    private void doDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().downLoad(downLoadInfo);
    }

    private void doPause(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().pause(downLoadInfo);
    }

    private void doCancel(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().cancel(downLoadInfo);
    }

    private void doInstall(DownLoadInfo downLoadInfo) {
        File apkFile = new File(downLoadInfo.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }

    private void doOpen(DownLoadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /*--------------- 接收到DownLoadInfo改变的通知 ---------------*/
    //被观察者通知消息所在的线程是什么线程,那接收消息所在的现在就是什么线程，所以下面刷新UI要用主线程
    @Override
    public void onDownLoadInfoChanged(final DownLoadInfo downLoadInfo) {
        //针对接收到的DownLoadInfo进行过滤，一个在下载，其他的进度条不要动
        if (!downLoadInfo.packageName.equals(mItemBean.packageName)) {
            return;
        }

        //日志输入downLoadInfo里面的curState
        PrintDownLoadInfo.printDownLoadInfo(downLoadInfo);

        //刷新ui
        MyApplication.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                refreshProgressBtnUI(downLoadInfo);
            }
        });

    }
}
