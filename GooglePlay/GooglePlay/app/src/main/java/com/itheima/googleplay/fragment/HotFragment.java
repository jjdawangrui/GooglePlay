package com.itheima.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.protocol.HotProtocol;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class HotFragment extends BaseFragment {

    private List<String> mDatas;

    @Override
    protected LoadingPager.LoadDataResult initData() {
        HotProtocol protocol = new HotProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkResult(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadDataResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {

        //外层套一个ScrollView，滚动效果
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());

        for (int i = 0; i < mDatas.size(); i++) {
            //data
            final String data = mDatas.get(i);
            //view
            TextView tv = new TextView(UIUtils.getContext());
            //data+view
            tv.setText(data);

            //属性设置
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            int padding = UIUtils.dip2Px(5);
            tv.setPadding(padding, padding, padding, padding);

            /*-----------创建了一个默认情况的背景------------*/
            GradientDrawable normalBg = new GradientDrawable();
            normalBg.setCornerRadius(10);//圆角矩形的半径

            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(170) + 30;//30-200
            int green = random.nextInt(170) + 30;//30-200
            int blue = random.nextInt(170) + 30;//30-200
            int argb = Color.argb(alpha, red, green, blue);
            normalBg.setColor(argb);

            /*-----------创建一个按下去的背景------------*/
            GradientDrawable pressedBg = new GradientDrawable();
            pressedBg.setCornerRadius(10);
            pressedBg.setColor(Color.DKGRAY);

            /*-----------创建一个带有状态的背景------------*/
            StateListDrawable selectorBg = new StateListDrawable();
            selectorBg.addState(new int[]{-android.R.attr.state_pressed}, normalBg);
            selectorBg.addState(new int[]{android.R.attr.state_pressed}, pressedBg);

            /**
             * 或者下面这么写，把按下的放在上面，否则的话，就要加一个false，也就是没有按下的状态，前面加一个负号
             */
//            selectorBg.addState(new int[]{android.R.attr.state_pressed}, pressedBg);
//            selectorBg.addState(new int[]{}, normalBg);

            tv.setBackgroundDrawable(selectorBg);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });
            flowLayout.addView(tv);
        }
        scrollView.addView(flowLayout);
        return scrollView;
    }
}
