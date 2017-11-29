package com.itheima.fragment_notifydatachanged;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ViewPager			mViewPager;	// view
	private List<Fragment>		mFragments;	// dataSets
	private MainFragmentAdapter	mAdapter;	// adapter

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void initData() {
		// 模拟数据集
		mFragments = new ArrayList<Fragment>();
		// 0-->HeimaFragmentButton
		mFragments.add(new HeimaFragmentButton());
		//
		for (int i = 0; i < 9; i++) {
			mFragments.add(new HeimaFragmentTextView(i + ""));
		}

		mAdapter = new MainFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
	}

	class MainFragmentAdapter extends FragmentPagerAdapter {

		public MainFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			return mFragments.get(i);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	/**
	 * 第一个Fragment中的按钮的点击事件
	 */
	public void notifyData() {
		// 移除fragmentMnager中之前返回过的Framgent
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = supportFragmentManager.beginTransaction();

		for (Fragment fragment : mFragments) {
			transaction.remove(fragment);
		}

		transaction.commit();
		transaction = null;

		// 清空数据集
		mFragments.clear();
		// 加入第一页
		mFragments.add(new HeimaFragmentButton());

		for (int i = 0; i < 9; i++) {
			// mFragments.add(new HeimaFragmentTextView(i + ""));
			mFragments.add(new HeimaFragmentTextView("我修改了内容" + i));
		}

		mAdapter.notifyDataSetChanged();

		Toast.makeText(getApplicationContext(), "notifyData", 0).show();
	}

}
