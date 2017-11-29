package com.itheima.googleplay.utils;

import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;

/**
 * @author Administrator
 * @version $Rev: 70 $
 * @time 2015-7-13 下午11:12:12
 * @des TODO
 * @updateAuthor $Author: admin $
 * @updateDate $Date: 2016-08-01 20:05:08 +0800 (星期一, 01 八月 2016) $
 * @updateDes TODO
 */
public class PrintDownLoadInfo {

    public static void printDownLoadInfo(DownLoadInfo info) {
        String result = "";
        switch (info.curState) {
            case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                result = "未下载";
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 下载中
                result = "下载中";
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                result = "暂停下载";
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                result = "等待下载";
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                result = "等待下载";
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 下载完成
                result = "下载完成";
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                result = "已安装";
                break;

            default:
                break;
        }
        LogUtils.sf(result);
    }
}
