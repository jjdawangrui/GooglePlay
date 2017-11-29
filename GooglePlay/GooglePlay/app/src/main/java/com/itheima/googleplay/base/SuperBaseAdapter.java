package com.itheima.googleplay.base;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itheima.googleplay.holder.LoadMoreHolder;
import com.itheima.googleplay.proxy.ThreadPoolProxyFactory;

import java.util.List;

/**
 * 针对MyBaseAdapter里面的getView方法，在getView方法中引入了BaseHolder这个类
 */
public abstract class SuperBaseAdapter<T> extends MyBaseAdapter implements AdapterView.OnItemClickListener {
    /**
     * 加载更多
     */
    public static final int VIEWTYPE_LOADMORE = 0;
    /**
     * 普通的Item
     */
    public static final int VIEWTYPE_NORMAL = 1;

    private AbsListView mAbsListView;//ListView和GridView的父类

    public SuperBaseAdapter(List<T> dataSets, AbsListView absListView) {
        super(dataSets);
        this.mAbsListView = absListView;
        mAbsListView.setOnItemClickListener(this);
    }

    /**
     * LIstView中显示几种ViewType怎么搞？
     1.复写两个方法
     复写Adapter中的getViewTypeCount
     复写adapter中的getItemViewType（position）
     2.在getView中分别处理
     */

    /**
     * get(得到)ViewType(ViewType)Count(总数),默认是1种类型
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;//1(普通类型)+1(加载更多) = 2
    }

    /**
     * get(得到)Item(指定条目)ViewType(ViewType类型)(int position),默认是0
     * 范围：0 ~ getViewTypeCount() - 1
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {//最后一个条目的时候，就是加载View
            return VIEWTYPE_LOADMORE;
        } else {
//            return VIEWTYPE_NORMAL;
            return getNormalItemViewType(position);
        }
    }

    /**
     * 得到普通条目的ViewType类型
     * 子类可以覆写该方法,返回更多的普通条目的viewType类型
     */
    public int getNormalItemViewType(int position) {
        return VIEWTYPE_NORMAL;//默认值是1
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;//其实就是加的加载更多的条目
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        /*------------决定itemView是啥----------*/
        BaseHolder holder = null;
        int curViewType = getItemViewType(i);
        if (view == null) {
            //根据当前条目的ViewTpye类型判断
            if (curViewType == VIEWTYPE_LOADMORE) {
                holder = getLoadMoreHolder();//创建加载更多的holder
            } else {
                //创建holder
                holder = getSpecialBaseHolder(i);//创建普通的holder
            }

        } else {
            //如果不为空，是回收的view，就直接从里面拿
            holder = (BaseHolder) view.getTag();
        }

        /*------------得到数据然后绑定数据----------*/
        if (curViewType == VIEWTYPE_LOADMORE) {
            if (hasLoadMore()) {
                //显示正在加载更多的视图
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_LOADING);
                triggerLoadMoreData();
            } else {
                //隐藏加载更多以及重试视图
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
            }
        } else {
            Object data = mDataSets.get(i);
            holder.setDataAndRefreshHolderView(data);
        }

        /**
         * 来一个很神奇的动画
         */
        View holderView = holder.mHolderView;
        holderView.setScaleX(0.6f);
        holderView.setScaleY(0.5f);
        ViewCompat.animate(holderView).scaleX(1).scaleY(1).setDuration(400).setInterpolator(new OvershootInterpolator(4)).start();

        return holder.mHolderView;//返回HomeHolder里面的view
    }

    LoadMoreTask mLoadMoreTask = null;

    /**
     * 触发加载更多的数据
     */
    private void triggerLoadMoreData() {
        if (mLoadMoreTask == null) {
            //加载之前显示正在加载更多
            mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_LOADING);
            //异步加载
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolProxyFactory.getmNormalThreadPoolProxy().submit(mLoadMoreTask);
        }
    }


    private int mState;

    private class LoadMoreTask implements Runnable {

        /*--------------- 定义刷新ui需要用到的两个值 ---------------*/
        private static final int PAGESIZE = 20;//每页请求的总数
        List loadMoreList = null;

        @Override
        public void run() {
            //真正的在子线程中加载更多的数据，得到数据
            try {
                loadMoreList = onLoadMore();
                //处理数据
                if (loadMoreList == null) {
                    mState = LoadMoreHolder.LOADMORE_NONE;
                } else {
                    if (loadMoreList.size() == PAGESIZE) {
                        mState = LoadMoreHolder.LOADMORE_LOADING;//mLoadMoreHolder显示就是正在加载更多-->用户下一次看到的就是正在加载更多
                    } else {
                        mState = LoadMoreHolder.LOADMORE_NONE;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mState = LoadMoreHolder.LOADMORE_ERROR;
            }
            /*--------------- 生成了两个临时变量 ---------------*/
            final List finalLoadMoreList = loadMoreList;
            final int finalState = mState;
            /*--------------- 具体刷新ui ---------------*/
            MyApplication.getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新长什么样-->mLoadMoreHolder-->mLoadMoreHolder.setDataAndRefreshHolder(curState);
                    mLoadMoreHolder.setDataAndRefreshHolderView(finalState);
                    //刷新数据-->ListView-->修改数据集(mDataSets.add(loadMoreList))-->adapter.notifyDataSetChanged();
                    if (finalLoadMoreList != null) {
                        mDataSets.addAll(finalLoadMoreList);
                        notifyDataSetChanged();
                    }
                }
            });
            //代表走到run方法体的最后了,任务已经执行完成了,置空任务，配合上面的判断
            mLoadMoreTask = null;
        }
    }

    /**
     * @return
     * @des 是否有加载更多, 默认没有加载更多
     * @des 子类可以覆写该方法, 可以决定有加载更多
     */
    public boolean hasLoadMore() {
        return false;//默认没有加载更多
    }

    /**
     * @return
     * @des 属于BaseHolder的子类对象
     * @des 加载更多的Holder的对象
     */
    LoadMoreHolder mLoadMoreHolder = null;

    private BaseHolder getLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    /**
     * @return
     * @des 得到BaseHolder具体的子类对象
     * @des 在SuperBaseAdapter中不知道如何创建BaseHolder的子类对象, 所以只能交给子类, 子类必须实现
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract BaseHolder getSpecialBaseHolder(int position);

    /**
     * @return
     * @throws Exception 加载更多过程中,出现了异常
     * @des 在子线中真正的加载更多的数据
     * @des 在SuperBaseAdapter中不知道如何加载更多的数据, 只能交给子类
     * @des 子类是选择性实现, 只有子类有加载更多的时候才覆写该方法, 完成具体加载更多
     */
    public List onLoadMore() throws Exception {
        return null;//默认是null
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mAbsListView instanceof ListView) {
            i = i - ((ListView) mAbsListView).getHeaderViewsCount();
        }
        int curViewType = getItemViewType(i);
        if (curViewType == VIEWTYPE_LOADMORE) {
            if (mState == LoadMoreHolder.LOADMORE_ERROR) {
                triggerLoadMoreData();
            }
        } else {
            onNormalitemClick(adapterView, view, i, l);
        }
    }

    /**
     * @des 普通条目的点击事件
     * @des 在SuperBaseAdapter中不知道如何处理普通条目的点击事件, 只能交给子类
     * @des 子类是选择性实现普通条目的点击事件
     */
    public void onNormalitemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
