package com.itheima.googleplay.holder;

import android.view.View;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.utils.UIUtils;

/**
 * Created by raynwang on 2017/7/31.
 */

public class LeftMenuHolder extends BaseHolder<Object>{
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
        return view;
    }

    @Override
    public void refreshHolderView(Object data) {

    }
}
