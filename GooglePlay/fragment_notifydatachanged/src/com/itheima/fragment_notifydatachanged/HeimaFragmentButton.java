package com.itheima.fragment_notifydatachanged;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class HeimaFragmentButton extends Fragment implements OnClickListener {

	private View	mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = View.inflate(getActivity(), R.layout.fragment_heima_button, null);
		mRootView.findViewById(R.id.btn).setOnClickListener(this);
		return mRootView;
	}

	@Override
	public void onClick(View arg0) {
		((MainActivity)getActivity()).notifyData();
	}

	 

}
