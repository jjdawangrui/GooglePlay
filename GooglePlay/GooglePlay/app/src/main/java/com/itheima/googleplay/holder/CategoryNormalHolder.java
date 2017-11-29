package com.itheima.googleplay.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.CategoryBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by raynwang on 2017/7/19.
 */

public class CategoryNormalHolder extends BaseHolder<CategoryBean> {
    @Bind(R.id.item_category_icon_1)
    ImageView mItemCategoryIcon1;
    @Bind(R.id.item_category_name_1)
    TextView mItemCategoryName1;
    @Bind(R.id.item_category_item_1)
    LinearLayout mItemCategoryItem1;
    @Bind(R.id.item_category_icon_2)
    ImageView mItemCategoryIcon2;
    @Bind(R.id.item_category_name_2)
    TextView mItemCategoryName2;
    @Bind(R.id.item_category_item_2)
    LinearLayout mItemCategoryItem2;
    @Bind(R.id.item_category_icon_3)
    ImageView mItemCategoryIcon3;
    @Bind(R.id.item_category_name_3)
    TextView mItemCategoryName3;
    @Bind(R.id.item_category_item_3)
    LinearLayout mItemCategoryItem3;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(CategoryBean data) {
        refreshUI(data.name1,data.url1,mItemCategoryName1,mItemCategoryIcon1);
        refreshUI(data.name2,data.url2,mItemCategoryName2,mItemCategoryIcon2);
        refreshUI(data.name3,data.url3,mItemCategoryName3,mItemCategoryIcon3);
    }

    public void refreshUI(final String name, String url, TextView tv, ImageView iv){
        if (TextUtils.isEmpty(name)&&TextUtils.isEmpty(url)) {
            ViewParent parent = tv.getParent();
            ((ViewGroup) parent).setVisibility(View.INVISIBLE);//学一手，父类控件需要转化成组，才能设置可见
        }else{
            tv.setText(name);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL+url).into(iv);
            ViewParent parent = tv.getParent();
            ((ViewGroup)parent).setVisibility(View.VISIBLE);
            ((ViewGroup)parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
