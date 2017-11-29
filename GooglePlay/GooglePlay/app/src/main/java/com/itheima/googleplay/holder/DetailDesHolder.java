package com.itheima.googleplay.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itheima.googleplay.R;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.utils.LogUtils;
import com.itheima.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 应用详情页面 —— 应用描述信息部分
 */
public class DetailDesHolder extends BaseHolder<ItemBean> implements View.OnClickListener {

    @Bind(R.id.app_detail_des_tv_des)
    TextView mAppDetailDesTvDes;
    @Bind(R.id.app_detail_des_tv_author)
    TextView mAppDetailDesTvAuthor;
    @Bind(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;
    /**
     * 当前View是否是展开的状态
     */
    private boolean isOpen = true;

    /**
     * 如果文字全部显示，View的总高度
     */
    private int mAppDetailDesTvDesHeight;

    /**
     * 当前View要展示的数据JavaBean
     */
    private ItemBean mItemBean;

    @Override
    public View initHolderView() {
        View holderView = View.inflate(UIUtils.getContext(), R.layout.item_detail_des, null);
        ButterKnife.bind(this, holderView);
        holderView.setOnClickListener(this);
        return holderView;
    }

    @Override
    public void refreshHolderView(ItemBean data) {
        //保存数据为成员变量
        mItemBean = data;
        mAppDetailDesTvAuthor.setText(data.author);
        mAppDetailDesTvDes.setText(data.des);
//        changeDetailDesHeight(false);
        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //默认折叠mAppDetailDesTvDes
                changeDetailDesHeight(false);
                mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);//用完之后记得移除
            }
        });
    }

    @Override
    public void onClick(View v) {
        changeDetailDesHeight(true);
    }

    /**
     * @param isAnimation true:有动画效果 false:没有动画效果
     * @des 修改mAppDetailDesTvDes高度
     */
    private void changeDetailDesHeight(boolean isAnimation) {
        if (mAppDetailDesTvDesHeight == 0) {
            mAppDetailDesTvDesHeight = mAppDetailDesTvDes.getMeasuredHeight();
            LogUtils.e("mAppDetailDesTvDesHeight", "" + mAppDetailDesTvDesHeight);
        }

        if (isOpen) {
            //折叠 mAppDetailDesTvDes高度 应有的高度-->7行的高度
            int start = mAppDetailDesTvDesHeight;
            int end = getShortLineHeight(7, mItemBean.des);//7行的高度
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        } else {
            //展开 mAppDetailDesTvDes高度 7行的高度-->应有的高度
            int start = getShortLineHeight(7, mItemBean.des);//7行的高度
            int end = mAppDetailDesTvDesHeight;
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        }
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mAppDetailDesTvDes, "height", start, end);
        animator.start();
        //箭头跟着旋转
        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 180, 0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 0, 180).start();
        }

        //监听动画执行完成
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //动画结束-->找到外层scrollView-->完成自动滚动
                ViewParent parent = mAppDetailDesTvDes.getParent();//父容器
                while (true) {
                    parent = parent.getParent();//父容器-->父容器
                    if (parent instanceof ScrollView) {
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);//滑到底部
                        break;
                    }
                    if (parent == null) {
                        break;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * 得到指定行高,指定内容的textview的高度
     *
     * @param lineHeight TextView显示几行
     * @param content    TextView要显示的文本内容
     * @return 指定行数的TextView的高度
     */
    private int getShortLineHeight(int lineHeight, String content) {
        TextView tempTextView = new TextView(UIUtils.getContext());
        tempTextView.setLines(lineHeight);
        tempTextView.setText(content);
        tempTextView.measure(0, 0);//spc:mode（0）+size（0）=0-->布局文件里面定义的多少高就得出多高
        //wrap-content---实际高度
        return tempTextView.getMeasuredHeight();
    }
}
