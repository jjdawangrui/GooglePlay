package com.itheima.googleplay.holder;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by raynwang on 2017/7/16.
 */

public class LoadMoreHolder extends BaseHolder<Integer> {
    public static final int LOADMORE_LOADING = 0;//正在加载更多
    public static final int LOADMORE_ERROR = 1;//加载更多失败,点击重试
    public static final int LOADMORE_NONE = 2;//没有加载更多

    @Bind(R.id.item_loadmore_container_loading)
    LinearLayout mItemLoadmoreContainerLoading;
    @Bind(R.id.item_loadmore_tv_retry)
    TextView mItemLoadmoreTvRetry;
    @Bind(R.id.item_loadmore_container_retry)
    LinearLayout mItemLoadmoreContainerRetry;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(Integer curState) {//SuperBaseAdapter里面，setDataAndRefreshHolderView方法传进来
        //首先隐藏所有的视图
        mItemLoadmoreContainerLoading.setVisibility(View.GONE);
        mItemLoadmoreContainerRetry.setVisibility(View.GONE);
        switch (curState) {
            case LOADMORE_LOADING:
                mItemLoadmoreContainerLoading.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_ERROR:
                mItemLoadmoreContainerRetry.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_NONE:
                break;
            default:
                break;
        }
    }
}
