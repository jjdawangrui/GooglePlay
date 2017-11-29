package com.itheima.googleplay.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.itheima.googleplay.R;
import com.itheima.googleplay.proxy.ThreadPoolProxyFactory;
import com.itheima.googleplay.utils.UIUtils;

import java.util.concurrent.ThreadFactory;

/**
 * Created by raynwang on 2017/7/9.
 */

//将视图和数据绑定，C
public abstract class LoadingPager extends FrameLayout {
    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_EMPTY = 3;
    public int mCurState = STATE_LOADING;
    private View mLodingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;
    private LoadDataTask mLoadDataTask;


    //这个构造只能传上下文，不能让别人瞎用
    public LoadingPager(@NonNull Context context) {
        super(context);
        initCommonView();
    }


    /**
     * @des 初始化常规视图(加载中视图, 错误视图, 空视图3个静态视图)
     * @called LoadingPager创建的时候
     */
    private void initCommonView() {
        mLodingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mLodingView);
        this.addView(mErrorView);
        this.addView(mEmptyView);

        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击重新加触发加载数据
                triggerLoadData();
            }
        });

        //为什么在这里不去写成功视图-->loadingPager创建的时候无法决定成功视图
        refreshViewByState();
    }

    /**
     * @des 根据状态刷新ui(决定LoadingPager到底提供四种视图中的哪一种)
     * @called 1.LoadingPager创建的时候
     * @called 2.外界调用了triggerLoadData加载数据, 数据加载之前
     * @called 3.外界调用了triggerLoadData加载数据, 而且数据加载完成了
     */
    private void refreshViewByState() {
        //先判断三个普通视图，显示与否
        if (mCurState == STATE_LOADING) {
            mLodingView.setVisibility(View.VISIBLE);
        } else {
            mLodingView.setVisibility(View.GONE);
        }
        if (mCurState == STATE_ERROR) {
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mErrorView.setVisibility(View.GONE);
        }
        if (mCurState == STATE_EMPTY) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }

        //这里就可能有成功视图了.因为数据已经加载完成了.而且数据加载成功了
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            //初始化成功View的方法，返回来View
            mSuccessView = initSuccessView();//抽象方法，子类实现，返回View就行
            this.addView(mSuccessView);
        }
        if (mSuccessView!=null){
            //当有成功的View，再来显示与否
            if (mCurState==STATE_SUCCESS){
                mSuccessView.setVisibility(View.VISIBLE);
            }else{
                mSuccessView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @des 触发加载数据
     * @called 外界想让LoadingPager触发加载数据的时候调用
     */
    public void triggerLoadData(){
        // 若当前已经加载成功，则无需再次加载
        if (mCurState!=STATE_SUCCESS){
            if (mLoadDataTask==null){//任务是null的时候才会走，如果正在走，就不重新走了
                //控制数据加载之前显示加载中的视图
                mCurState = STATE_LOADING;
                refreshViewByState();

                // 2.异步加载
                mLoadDataTask = new LoadDataTask();
//                new Thread(mLoadDataTask).start();//这里是直接来了个线程，我们用下面的线程池代理，来开启任务
                ThreadPoolProxyFactory.getmNormalThreadPoolProxy().submit(mLoadDataTask);
            }
        }
    }

    class LoadDataTask implements Runnable{
        @Override
        public void run() {
            //真正的在子线程中加载具体的数据-->得到数据
            LoadDataResult loadDataResult = initData();//这个方法在Fragment中重写，返回数据的结果3种，空 错误 成功
            //处理数据
            mCurState = loadDataResult.getState();
            //在主线程中去更新ui
            MyApplication.getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    //刷新UI(决定到底提供4种视图中的哪一种视图)
                    refreshViewByState();//mCurState-->Int
                }
            });
            //置空任务，配合上面的if判断，任务是null的时候才走
            mLoadDataTask = null;
        }
    }


    //标识数据加载结果的枚举类
    public enum LoadDataResult{
        /**
         * STATE_ERROR = 1;//错误
         * STATE_SUCCESS = 2;//成功
         * STATE_EMPTY = 3;//空
         */
        ERROR(STATE_ERROR),SUCCESS(STATE_SUCCESS),EMPTY(STATE_EMPTY);

        private int state;
        //0 1 2和上面的1 2 3不对应，所以要用构造方法传入
        LoadDataResult(int state) {
            this.state = state;
        }
        public int getState(){
            return state;
        }
    }



    /**
     * @return
     * @des 决定成功视图长什么样子(需要定义成功视图)
     * @des 数据和视图的绑定过程
     * @called triggerLoadData()方法被调用, 而且数据加载完成了, 而且数据加载成功
     */
    public abstract View initSuccessView();

    /**
     * @des 在子线程中真正的加载具体的数据
     * @called triggerLoadData()方法被调用的时候
     */
    public abstract LoadDataResult initData();

}
