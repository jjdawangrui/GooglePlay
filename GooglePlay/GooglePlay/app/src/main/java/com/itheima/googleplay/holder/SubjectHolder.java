package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.SubjectBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by raynwang on 2017/7/17.
 */

public class SubjectHolder extends BaseHolder<SubjectBean> {
    ImageView mItemSubjectIvIcon;
    TextView mItemSubjectTvTitle;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
//        mItemSubjectIvIcon = view.findViewById(R.id.item_subject_iv_icon);
        mItemSubjectTvTitle = view.findViewById(R.id.item_subject_tv_title);
        return view;
    }

    @Override
    public void refreshHolderView(SubjectBean data) {
//        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL+data.url).into(mItemSubjectIvIcon);
        LogUtils.s(data.des+"------------------");
        mItemSubjectTvTitle.setText(data.des);
    }
}
