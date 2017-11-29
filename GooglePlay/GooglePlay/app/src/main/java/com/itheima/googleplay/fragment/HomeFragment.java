package com.itheima.googleplay.fragment;

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
import com.itheima.googleplay.bean.HomeBean;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.HomePicturesHolder;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.protocol.HomeProtocol;
import com.itheima.googleplay.utils.UIUtils;

import java.util.List;

public class HomeFragment extends BaseFragment {

    private List<String> mPicture;
    private List<ItemBean> mItemBeen;
    private HomeProtocol mHomeProtocol;
    private HomeAdapter mAdapter;

    @Override//返回那个枚举类
    protected LoadingPager.LoadDataResult initData() {
        mHomeProtocol = new HomeProtocol();
        try {
            HomeBean homeBean = mHomeProtocol.loadData(0);

            LoadingPager.LoadDataResult state = checkResult(homeBean);//握草，继承了就可以直接用麽！方法

            if (state != LoadingPager.LoadDataResult.SUCCESS) {//说明homeBean有问题,homeBean==null
                return state;
            }
            state = checkResult(homeBean.list);
            if (state != LoadingPager.LoadDataResult.SUCCESS) {//说明list有问题,list.size==0
                return state;
            }
            //走到这里来说明是成功的,保存数据到成员变量
            mPicture = homeBean.picture;
            mItemBeen = homeBean.list;
            return state;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadDataResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();

        /*---------轮播图挂载---------*/
        HomePicturesHolder homePicturesHolder = new HomePicturesHolder();
        //取出HomePicturesHolder所提供的视图
        View headerView = homePicturesHolder.mHolderView;
        //让HomePicturesHolder接收数据，然后就进行数据和视图的绑定
        homePicturesHolder.setDataAndRefreshHolderView(mPicture);
        //添加一个轮播图
        listView.addHeaderView(headerView);


        mAdapter = new HomeAdapter(mItemBeen, listView);
        listView.setAdapter(mAdapter);
        return listView;
    }


    class HomeAdapter extends ItemAdapter {

        public HomeAdapter(List dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }

        /**
         * 复用view，减少view被打气出来的次数
         * 用ViewHolder，减少findViewById的次数
         */


        @Override
        public List onLoadMore() throws Exception {
            SystemClock.sleep(1000);
            HomeBean homeBean = mHomeProtocol.loadData(mItemBeen.size());
            if (homeBean != null) {
                return homeBean.list;
            }
            return super.onLoadMore();
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
