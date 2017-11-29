package com.itheima.googleplay.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.protocol.RecommendProtocol;
import com.itheima.googleplay.utils.UIUtils;
import com.itheima.googleplay.views.flyinout.ShakeListener;
import com.itheima.googleplay.views.flyinout.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class RecommendFragment extends BaseFragment {

    private List<String> mDatas;
    /**  摇一摇切换*/
    private ShakeListener mShakeListener;

    @Override
    protected LoadingPager.LoadDataResult initData() {
        RecommendProtocol protocol = new RecommendProtocol();
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
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        final RecommendAdapter adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);

        //1.首页未展示
        stellarMap.setGroup(0, true);

        //2.每一页展示的条目数目不统一
        stellarMap.setRegularity(15, 20);

        /**
         * 这个摇一摇监听的类，封装的非常好，以后用，另外3个是字体飞入飞出，用到的可以拿过来
         */
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //切换
                int currentGroup = stellarMap.getCurrentGroup();
                if (currentGroup == adapter.getGroupCount() - 1) {//最后一页
                    currentGroup = 0;
                } else {
                    currentGroup++;
                }
                //设置group切换
                stellarMap.setGroup(currentGroup, true);
            }
        });
        return stellarMap;
    }

    /**
     * 摇一摇监听里面的方法，当界面可见的时候，才开启监听，不可见就停止监听，节约资源
     */
    @Override
    public void onResume() {
        if (mShakeListener != null) {
            mShakeListener.resume();
        }
        super.onResume();
    }
    @Override
    public void onPause() {
        if (mShakeListener != null) {
            mShakeListener.pause();
        }
        super.onPause();
    }

    private class RecommendAdapter implements StellarMap.Adapter {

        public static final int PAGESIZE = 15;

        @Override
        public int getGroupCount() {//一共多少组，如果32个  15 15 2，就需要3组
            if (mDatas.size() % PAGESIZE == 0) {
                return mDatas.size() / PAGESIZE;
            } else {
                return mDatas.size() / PAGESIZE + 1;
            }
        }

        @Override
        public int getCount(int group) {//传进来组序号，获取该组多少个
            if (mDatas.size() % PAGESIZE == 0) {
                return PAGESIZE;
            } else {
                if (group == getGroupCount() - 1) {
                    return mDatas.size() % PAGESIZE;
                } else {
                    return PAGESIZE;
                }
            }
        }

        @Override
        public View getView(int group, int position, View convertView) {//返回具体的孩子
            TextView tv = new TextView(UIUtils.getContext());

            //随机大小
            Random random = new Random();
            tv.setTextSize(random.nextInt(5) + 12);//[12,16]
            //随机颜色
            int alpha = 255;
            int red = random.nextInt(170) + 30;//30-200
            int green = random.nextInt(170) + 30;//30-200
            int blue = random.nextInt(170) + 30;//30-200
            int color = Color.argb(alpha, red, green, blue);
            tv.setTextColor(color);

            int index = group*PAGESIZE+position;
            tv.setText(mDatas.get(index));
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}
