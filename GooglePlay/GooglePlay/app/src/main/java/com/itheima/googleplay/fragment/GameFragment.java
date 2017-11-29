package com.itheima.googleplay.fragment;

import android.content.ClipData;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.ItemAdapter;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.base.SuperBaseAdapter;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.protocol.AppProtocol;
import com.itheima.googleplay.protocol.GameProtocol;
import com.itheima.googleplay.utils.UIUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class GameFragment extends BaseFragment {

    private GameProtocol mProtocol;
    private List<ItemBean> mDatas;
    private GameAdapter mAdapter;

    @Override//返回那个枚举类
    protected LoadingPager.LoadDataResult initData() {
        mProtocol = new GameProtocol();
        try {
            mDatas = mProtocol.loadData(0);
            LoadingPager.LoadDataResult loadDataResult = checkResult(mDatas);
            return loadDataResult;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadDataResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        mAdapter = new GameAdapter(mDatas,listView);
        listView.setAdapter(mAdapter);
        return listView;
    }

    class GameAdapter extends ItemAdapter {

        public GameAdapter(List dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }


        @Override
        public List onLoadMore() throws Exception {
            SystemClock.sleep(1000);
            List<ItemBean> itemBeanList = mProtocol.loadData(mDatas.size());
            return itemBeanList;
        }
    }

    @Override
    public void onResume() {
        if (mAdapter!=null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            if (itemHolders != null && itemHolders.size() != 0) {
                for (ItemHolder o : itemHolders) {
                    //添加观察者到观察者集合中
                    DownLoadManager.getInstance().addObserver(o);

                    /**
                     * 当安装完，点完成，回到页面，会出现页面没有刷新，所以需要手动
                     */
                    //手动发布最新的状态
                    DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(o.mData);
                    DownLoadManager.getInstance().notifyObservers(downLoadInfo);
                }
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAdapter!=null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            if (itemHolders != null && itemHolders.size() != 0) {
                for (ItemHolder o : itemHolders) {
                    //从观察者集合中移除观察者
                    DownLoadManager.getInstance().deleteObserver(o);
                }
            }
        }
        super.onPause();
    }
}
