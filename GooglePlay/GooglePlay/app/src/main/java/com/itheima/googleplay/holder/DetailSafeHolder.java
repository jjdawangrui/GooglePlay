package com.itheima.googleplay.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.conf.Constants;
import com.itheima.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 应用详情页面 —— 安全描述信息部分
 */
public class DetailSafeHolder extends BaseHolder<ItemBean> implements View.OnClickListener {


    @Bind(R.id.app_detail_safe_iv_arrow)
    ImageView mAppDetailSafeIvArrow;
    @Bind(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;
    @Bind(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;
    /**
     * 标识当前状态是打开还是关闭
     */
    private boolean isOpen = true;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_safe, null);
        ButterKnife.bind(this, holderView);
        holderView.setOnClickListener(this);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        List<ItemBean.ItemSafeBean> itemSafeBeans = data.safe;
        for (int i = 0; i < itemSafeBeans.size(); i++) {
            ItemBean.ItemSafeBean itemSafeBean = itemSafeBeans.get(i);
            String safeDes = itemSafeBean.safeDes;
            int safeDesColor = itemSafeBean.safeDesColor;
            String safeDesUrl = itemSafeBean.safeDesUrl;
            String safeUrl = itemSafeBean.safeUrl;

            /*---------------  往mAppDetailSafePicContainer 容器动态加载孩子 ---------------*/
            ImageView ivIcon = new ImageView(UIUtils.getContext());
            //图片的加载
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeUrl).into(ivIcon);
            mAppDetailSafePicContainer.addView(ivIcon);

            /*---------------  往mAppDetailSafeDesContainer 容器动态加载孩子 ---------------*/
            LinearLayout line = new LinearLayout(UIUtils.getContext());

            // 构建描述文本
            TextView tvDesNote = new TextView(UIUtils.getContext());
            //设置单行效果
            tvDesNote.setSingleLine(true);
            //设置数据
            tvDesNote.setText(safeDes);
            // 设置文字颜色
            if (safeDesColor == 0) {
                tvDesNote.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
            } else {
                tvDesNote.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }

            // 构建描述图标
            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeDesUrl).into(ivDesIcon);

            line.addView(ivDesIcon);
            line.addView(tvDesNote);

            mAppDetailSafeDesContainer.addView(line);
        }

        //默认折叠mAppDetailSafeDesContainer
        changeSafeDesContainerHeight(false);
    }

    @Override
    public void onClick(View v) {
        changeSafeDesContainerHeight(true);
    }

    /**
     * 修改SafeDesContainer高度
     * @param isAnimation true:动画的方式修改高度 false:直接修改高度
     */
    private void changeSafeDesContainerHeight(boolean isAnimation) {
        if (isOpen) {
            //如果是打开的状态，就折叠  mAppDetailSafeDesContainer 高度  应有的高度-->0
            int start = mAppDetailSafeDesContainer.getMeasuredHeight();

            /**
             * 要用getMeasuredHeight，需要测绘完成才行，那么onCreat里面肯定不行，太早了
             * onDraw里面可以，onClick也可以，这里就是onClick
             *
             * 还可以先测量，measure，再getMeasuredHeight，那么在onCreat里面也可以这么搞了
             *
             * 第三种方法，view.getViewTreeObserver.addOnGlobalLayoutListener  监听全局的布局完成，那么肯定测绘完成了
             */

            int end = 0;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        } else {
            //展开 mAppDetailSafeDesContainer 高度  0-->应有的高度
            /**
             * 因为折叠了，所以这里直接getMeasureHeight，是0，需要先测量一下，系统重新分配给容器一个高度
             */
            mAppDetailSafeDesContainer.measure(0, 0);
            int end = mAppDetailSafeDesContainer.getMeasuredHeight();
            int start = 0;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        }
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        /**
         * 不能用ObjectAnimation，因为容器没有setHeight的方法，所以只能用ValueAnimation
         */
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.start();

        //得到动画执行过程中的渐变值，这里是配套ValueAnimation的，必须要有
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int tempHeight = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = tempHeight;

                //重新设置layoutParams
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        });
        //箭头跟着旋转
        if (isOpen) {
            /**
             * 崽耶，这个就可以用ObjectAnimation
             */
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0, 180).start();
        }
    }
}
