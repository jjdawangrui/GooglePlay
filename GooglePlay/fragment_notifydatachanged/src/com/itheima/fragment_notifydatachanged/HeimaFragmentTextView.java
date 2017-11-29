package com.itheima.fragment_notifydatachanged;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @创建者	 Administrator
 * @创时间 	 2015-10-19 下午9:25:00
 * @描述	     TODO
 *
 * @版本       $Rev$
 * @更新者     $Author$
 * @更新时间    $Date$
 * @更新描述    TODO
 */
public class HeimaFragmentTextView extends Fragment {

	private View		mRootView;
	private String		mStr;
	private TextView	mTitle;

	public HeimaFragmentTextView(String str) {
		mStr = str;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = View.inflate(getActivity(), R.layout.fragment_heima_textview, null);
		mTitle = (TextView) mRootView.findViewById(R.id.title);
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}

	private void initData() {
		mTitle.setText("HeimaFragment-" + mStr);
	}
}
