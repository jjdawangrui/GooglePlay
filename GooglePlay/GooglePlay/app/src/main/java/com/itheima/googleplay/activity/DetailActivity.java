package com.itheima.googleplay.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.holder.DetailDesHolder;
import com.itheima.googleplay.holder.DetailDownloadHolder;
import com.itheima.googleplay.holder.DetailInfoHolder;
import com.itheima.googleplay.holder.DetailPicHolder;
import com.itheima.googleplay.holder.DetailSafeHolder;
import com.itheima.googleplay.manager.DownLoadInfo;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.protocol.DetailProtocol;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.detail_fl_download)
    FrameLayout mDetailFlDownload;
    @Bind(R.id.detail_fl_info)
    FrameLayout mDetailFlInfo;
    @Bind(R.id.detail_fl_safe)
    FrameLayout mDetailFlSafe;
    @Bind(R.id.detail_fl_pic)
    FrameLayout mDetailFlPic;
    @Bind(R.id.detail_fl_des)
    FrameLayout mDetailFlDes;

    private LoadingPager mLoadingPager;
    private String mPackageName;
    private ItemBean mItemBean;
    private DetailDownloadHolder mDetailDownloadHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingPager = new LoadingPager(this) {
            @Override
            public View initSuccessView() {
                return DetailActivity.this.initSuccessView();
            }

            @Override
            public LoadDataResult initData() {
                return DetailActivity.this.initData();
            }
        };
        setContentView(mLoadingPager);
//        ButterKnife.bind(this);//这一行自动生成的，要干掉，因为在确定成功视图的时候，已经find孩子了
        init();
        triggerLoadData();
        initActionBar();
    }

    /**
     * 触发加载详情里面的数据
     */
    private void triggerLoadData() {
        mLoadingPager.triggerLoadData();
    }

    //左上角的回退按钮，学着写吧，看不懂
    public void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private LoadingPager.LoadDataResult initData() {
        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mItemBean = protocol.loadData(0);
            if (mItemBean != null) {
                return LoadingPager.LoadDataResult.SUCCESS;
            } else {
                return LoadingPager.LoadDataResult.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadDataResult.ERROR;
        }
    }

    private View initSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.layout_detail, null);
        ButterKnife.bind(this, view);

//        往应用的信息部分这个容器里面添加内容
        DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
        View holderView = detailInfoHolder.mHolderView;
        // 为基本信息部分添加一个翻转的属性动画
        ViewCompat.animate(holderView).rotationX(360)
                .setInterpolator(new OvershootInterpolator(4))
                .setDuration(1000)
                .start();
        mDetailFlInfo.addView(holderView);
        detailInfoHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的安全部分这个容器里面添加内容
        DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
        mDetailFlSafe.addView(detailSafeHolder.mHolderView);
        detailSafeHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的截图部分
        DetailPicHolder detailPicHolder = new DetailPicHolder();
        mDetailFlPic.addView(detailPicHolder.mHolderView);
        detailPicHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的描述部分
        DetailDesHolder detailDesHolder = new DetailDesHolder();
        mDetailFlDes.addView(detailDesHolder.mHolderView);
        detailDesHolder.setDataAndRefreshHolderView(mItemBean);

//        往应用的下载部分
        mDetailDownloadHolder = new DetailDownloadHolder();
        mDetailFlDownload.addView(mDetailDownloadHolder.mHolderView);
        mDetailDownloadHolder.setDataAndRefreshHolderView(mItemBean);

        //添加观察者到集合中
        DownLoadManager.getInstance().addObserver(mDetailDownloadHolder);

        return view;
    }


    private void init() {
        mPackageName = getIntent().getStringExtra("packageName");
        Toast.makeText(getApplicationContext(), mPackageName, Toast.LENGTH_SHORT).show();
    }

    /**
     * 从详情页面返回，里面的UI更新就是没必要的
     * 所以观察者要和生命周期绑定
     */
    @Override
    protected void onResume() {
        //动态的添加观察者到观察者集合中
        if (mDetailDownloadHolder != null) {
            DownLoadManager.getInstance().addObserver(mDetailDownloadHolder);

            //点击安装，然后安装，然后点击完成，就会回到详情页面，但是UI并没有刷新，还是显示 安装
            //因为跳转到系统安装页面的时候，移除了观察者，所以...
            //手动发布最新的DownLoadInfo
            DownLoadInfo downLoadInfo  = DownLoadManager.getInstance().getDownLoadInfo(mItemBean);
            DownLoadManager.getInstance().notifyObservers(downLoadInfo);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        //动态的从观察者集合中移除观察者
        if (mDetailDownloadHolder != null) {
            DownLoadManager.getInstance().deleteObserver(mDetailDownloadHolder);
        }
        super.onPause();
    }
}
