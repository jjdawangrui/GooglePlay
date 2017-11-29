package com.itheima.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.RatioLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 应用详情页面 —— 应用截图信息部分
 */
public class DetailPicHolder extends BaseHolder<ItemBean> {


    @Bind(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_pic, null);
        ButterKnife.bind(this, holderView);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        //往mAppDetailPicIvContainer容器添加内容
        List<String> screenUrls = data.screen;
        for (int i = 0; i < screenUrls.size(); i++) {
            String url = screenUrls.get(i);
            ImageView iv = new ImageView(UIUtils.getContext());

            //创建ratioLayout，为了让屏幕只有3个图片
            RatioLayout rl = new RatioLayout(UIUtils.getContext());
            //要设置谁，就相对于谁
            rl.setRelative(RatioLayout.RELATIVE_WIDTH);
            //图片的宽高比，不加float，就是0了
            rl.setPicRatio((float) 150 / 250);
            //添加图片到ratioLayout中
            rl.addView(iv);//然后下面要用到iv的，全都换成rl

            //宽度已知-->屏幕的1/3
            int screenWidth = UIUtils.getResources().getDisplayMetrics().widthPixels;
            screenWidth = screenWidth - UIUtils.dip2Px(18);
            int width = screenWidth / 3;

            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            if (i != 0) {
                params.leftMargin = UIUtils.dip2Px(4);
            }

            /**
             * 这个添加View，有2个参数的，因为容器是LL，所以上面的参数也是LL的LayoutParams
             */
            mAppDetailPicIvContainer.addView(rl, params);

            //图片的加载
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);
        }
    }
}
