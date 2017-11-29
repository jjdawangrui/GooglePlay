package com.itheima.googleplay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStripExtends;
import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.factory.FragmentFactory;
import com.itheima.googleplay.holder.LeftMenuHolder;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述
 */
public class MainActivity extends AppCompatActivity {


    @Bind(R.id.main_tabs)
    PagerSlidingTabStripExtends mMainTabs;
    @Bind(R.id.main_viewpager)
    ViewPager mMainViewpager;
    @Bind(R.id.main_drawerLayout)
    DrawerLayout mMainDrawerLayout;
    @Bind(R.id.main_left_menu)
    FrameLayout mFlLeftMenu;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private String[] mMainTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initActionBar();
        initView();
        initActionBarDrawerToggle();

        /**
         * initData创建ViewPager的Adapter，设置Adapter，创建HomeFragment和APPFragment，并创建两个LoadingPager，需要消耗时间
         * initListener是跟上面同时执行的，会取出HomeFragment，而可能上面还没创建，所以会空指针，所以要来一个onGlobalLayout监听
         */
        initData();
        initListener();
    }

    private void initListener() {
        final MyOnpageChangeListener myOnpageChangeListener = new MyOnpageChangeListener();

        //这里不能用mMainViewpager去设置监听，易错点
        mMainTabs.setOnPageChangeListener(myOnpageChangeListener);

        mMainViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //ViewPager已经展示给用户看-->说明HomeFragment和AppFragment已经创建好了
                //手动选中第一页，触发加载数据的方法，这是在处理首页未加载的问题
                myOnpageChangeListener.onPageSelected(0);
                mMainViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);//再把这个监听移除
            }
        });

    }

    class MyOnpageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 如果根据position找到对应页面的Fragment
            BaseFragment baseFragment = FragmentFactory.mCacheFragments.get(position);
            // 拿到Fragment里面的LoadingPager
            LoadingPager loadingPager = baseFragment.getLoadingPager();
            // 触发加载数据
            loadingPager.triggerLoadData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
    }

    private void initActionBar() {
        //得到actionBar的实例
//        getActionBar();//android sdk actionbar
        ActionBar supportActionBar = getSupportActionBar();//v4包中

        //设置标题
        supportActionBar.setTitle("GooglePlay");
//        supportActionBar.setSubtitle("副标题");

        //设置图标
        supportActionBar.setIcon(R.drawable.ic_launcher);
        supportActionBar.setLogo(R.mipmap.ic_action_call);

        //显示logo/icon(图标)
        supportActionBar.setDisplayShowHomeEnabled(false);//默认是false,默认是隐藏图标

        //修改icon和logo显示的优先级
        supportActionBar.setDisplayUseLogoEnabled(true);//默认是false,默认是没用logo,用的icon

        //显示回退部分，里面有个home看见没
        supportActionBar.setDisplayHomeAsUpEnabled(true);//默认是false,默认隐藏了回退部分
    }

    private void initActionBarDrawerToggle() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        //同步状态方法-->替换默认回退部分的UI效果
        mToggle.syncState();

        //设置drawerLayout的监听-->DrawerLayout拖动的时候,toggle可以跟着改变ui
        mDrawerLayout.setDrawerListener(mToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://这就是点击了回退按钮，home
//                Toast.makeText(getApplicationContext(), "点击了回退部分", Toast.LENGTH_SHORT).show();
                //点击toggle可以控制drawerlayout的打开和关闭
                mToggle.onOptionsItemSelected(item);

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initData() {
        //填充左菜单具体内容
        LeftMenuHolder leftMenuHolder = new LeftMenuHolder();
        mFlLeftMenu.addView(leftMenuHolder.mHolderView);

        //模拟数据集
        mMainTitles = UIUtils.getStrings(R.array.main_titles);

        //为viewPager设置适配器
//        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        MainFragmentStatePagerAdapter adapter = new MainFragmentStatePagerAdapter(getSupportFragmentManager());
        mMainViewpager.setAdapter(adapter);

//        mMainViewpager.setOffscreenPageLimit(0);//这里就是让它不要预加载

        // Bind the tabs to the ViewPager
        mMainTabs.setViewPager(mMainViewpager);
    }

    /*
      PagerAdapter-->View
      FragmentStatePagerAdapter-->Fragment
      FragmentPagerAdapter-->Fragment
     */
    class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {//指定Position所对应的页面的Fragment内容
            LogUtils.s("初始化->" + mMainTitles[position]);
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {//决定ViewPager页数的总和
            if (mMainTitles != null) {
                return mMainTitles.length;
            }
            return 0;
        }

        /**
         * 必须覆写一个方法:getPageTitle
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitles[position];
        }
    }

    class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public MainFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {//指定Position所对应的页面的Fragment内容
            LogUtils.s("初始化->" + mMainTitles[position]);
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {//决定ViewPager页数的总和
            if (mMainTitles != null) {
                return mMainTitles.length;
            }
            return 0;
        }

        /**
         * 必须覆写一个方法:getPageTitle
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitles[position];
        }
    }
}
