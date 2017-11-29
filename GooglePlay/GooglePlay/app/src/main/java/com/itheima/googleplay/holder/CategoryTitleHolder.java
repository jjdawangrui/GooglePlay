package com.itheima.googleplay.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.CategoryBean;
import com.itheima.googleplay.utils.UIUtils;

/**
 * Created by raynwang on 2017/7/19.
 */

public class CategoryTitleHolder extends BaseHolder<CategoryBean> {

    private TextView mTv;

    @Override
    public View initHolderView() {
        mTv = new TextView(UIUtils.getContext());
        //这一手padding也要学一下
        int padding = UIUtils.dip2Px(5);
        mTv.setPadding(padding,padding,padding,padding);
        mTv.setTextColor(Color.BLUE);
        return mTv;
    }

    @Override
    public void refreshHolderView(CategoryBean data) {
        mTv.setText(data.title);
    }
}
