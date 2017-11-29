package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.StringUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 应用详情页面 —— 基本信息部分
 */
public class DetailInfoHolder extends BaseHolder<ItemBean> {


    @Bind(R.id.app_detail_info_iv_icon)
    ImageView mAppDetailInfoIvIcon;
    @Bind(R.id.app_detail_info_tv_name)
    TextView mAppDetailInfoTvName;
    @Bind(R.id.app_detail_info_rb_star)
    RatingBar mAppDetailInfoRbStar;
    @Bind(R.id.app_detail_info_tv_downloadnum)
    TextView mAppDetailInfoTvDownloadnum;
    @Bind(R.id.app_detail_info_tv_version)
    TextView mAppDetailInfoTvVersion;
    @Bind(R.id.app_detail_info_tv_time)
    TextView mAppDetailInfoTvTime;
    @Bind(R.id.app_detail_info_tv_size)
    TextView mAppDetailInfoTvSize;

    /**
     * 决定成功视图长什么样子
     */
    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_info, null);
        ButterKnife.bind(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        /**
         * 看里面的String.xml，那是占位符，后面的日期，填到前面的位置去，然后就拼接成了一个String
         */
        String date = UIUtils.getResources().getString(R.string.detail_date, data.date);
        String downLoadNum = UIUtils.getResources().getString(R.string.detail_downloadnum, data.downloadNum);
        String size = UIUtils.getResources().getString(R.string.detail_size, StringUtils.formatFileSize(data.size));
        String version = UIUtils.getResources().getString(R.string.detail_version, data.version);

        mAppDetailInfoTvName.setText(data.name);
        mAppDetailInfoTvVersion.setText(version);
        mAppDetailInfoTvTime.setText(date);
        mAppDetailInfoTvSize.setText(size);
        mAppDetailInfoTvDownloadnum.setText(downLoadNum);

        //ratingbar
        mAppDetailInfoRbStar.setRating(data.stars);

        //图标
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.iconUrl).into(mAppDetailInfoIvIcon);
    }
}
