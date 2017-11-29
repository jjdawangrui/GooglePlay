package com.itheima.googleplay.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.MyApplication;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.ChildViewPager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomePicturesHolder extends BaseHolder<List<String>> implements ViewPager.OnPageChangeListener {//String集合是轮播图的地址
    @Bind(R.id.item_home_picture_pager)
    ChildViewPager mItemHomePicturePager;
    @Bind(R.id.item_home_picture_container_indicator)
    LinearLayout mItemHomePictureContainerIndicator;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_pictures, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private List<String> mPictureUrls;

    @Override
    public void refreshHolderView(List<String> pictureUrls) {
        mPictureUrls = pictureUrls;
        /*--------------- mItemHomePicturePager绑定 ---------------*/
        mItemHomePicturePager.setAdapter(new HomePictureAdapter());

        /*--------------- mItemHomePictureContainerIndicator绑定 ---------------*/
        for (int i = 0; i < mPictureUrls.size(); i++) {
            ImageView ivIndicator = new ImageView(UIUtils.getContext());
            ivIndicator.setImageResource(R.drawable.indicator_normal);
            if (i == 0) {
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }

            //这里是给int值附一个特定的单位dp，这么长一串，厉害了
            int sixDp = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, UIUtils.getResources().getDisplayMetrics()) + .5f);
            LogUtils.s(sixDp+"----------------6dp是多少？");

            int width = UIUtils.dip2Px(6);
            int height = UIUtils.dip2Px(6);
            //点容器是线性的，所以选择LinerLayout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
            params.leftMargin = UIUtils.dip2Px(6);
            params.bottomMargin = UIUtils.dip2Px(6);
            mItemHomePictureContainerIndicator.addView(ivIndicator,params);
        }


        //监听ViewPager的页面切换操作
        mItemHomePicturePager.setOnPageChangeListener(this);

        //设置viewPager页面的初始位置
        int curItem = mPictureUrls.size()*500000;
        mItemHomePicturePager.setCurrentItem(curItem);

        //实现自动轮播
        if (mAutoScrollTask==null){
            mAutoScrollTask = new AutoScrollTask();
            mAutoScrollTask.start();
        }

        //按下去的时候停止轮播
        mItemHomePicturePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                        mAutoScrollTask.start();
                        break;
                }
                return false;
            }
        });


    }

    AutoScrollTask mAutoScrollTask = null;
    class AutoScrollTask implements Runnable{
        public void start(){
            stop();
            MyApplication.getMainThreadHandler().postDelayed(this,3000);
        }

        public void stop(){
            MyApplication.getMainThreadHandler().removeCallbacks(this);
        }


        @Override
        public void run() {
            int curItem = mItemHomePicturePager.getCurrentItem();
            curItem++;
            mItemHomePicturePager.setCurrentItem(curItem);
            start();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position%mPictureUrls.size();
        for (int i = 0; i < mPictureUrls.size(); i++) {
            ImageView ivIndicator = (ImageView) mItemHomePictureContainerIndicator.getChildAt(i);
            ivIndicator.setImageResource(R.drawable.indicator_normal);
            if (i==position){
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class HomePictureAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mPictureUrls != null) {
//                return mPictureUrls.size();
                return mPictureUrls.size()*1000000;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//模板代码？？？
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position%mPictureUrls.size();
            //view
            ImageView iv = new ImageView(UIUtils.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //data
            String url = mPictureUrls.get(position);
            //view+data
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);
            //把view加到容器中
            container.addView(iv);
            //返回具体的view
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//模板代码？？？
        }
    }
}
