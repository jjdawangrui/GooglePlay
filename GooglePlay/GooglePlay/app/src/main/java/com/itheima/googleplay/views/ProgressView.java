package com.itheima.googleplay.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.R;
import com.itheima.googleplay.utils.UIUtils;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class ProgressView extends LinearLayout {

    private boolean isProgressEnable = true;
    private long mMax = 100;
    private long mProgress;
    private Paint mPaint;
    private RectF mOval;

    /**
     * 设置是否允许进度
     *
     * @param isProgressEnable
     */
    public void setIsProgressEnable(boolean isProgressEnable) {
        this.isProgressEnable = isProgressEnable;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 设置进度的当前值
     *
     * @param progress
     */
    public void setProgress(long progress) {
        mProgress = progress;
        //重绘进度
        invalidate();
    }

    private ImageView mIvIcon;
    private TextView mTvNote;

    /**
     * 设置图标
     *
     * @param resId
     */
    public void setIcon(int resId) {
        mIvIcon.setImageResource(resId);
    }

    /**
     * 设置提示文本
     *
     * @param content
     */
    public void setNote(String content) {
        mTvNote.setText(content);
    }

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.inflate_progressview, this);
        mIvIcon = view.findViewById(R.id.ivIcon);
        mTvNote = view.findViewById(R.id.tvNote);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);//绘制背景
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);//绘制图标,文本
        if (isProgressEnable) {
            if (mPaint == null) {
                mPaint = new Paint();
                mPaint.setColor(Color.BLUE);
                mPaint.setStyle(Paint.Style.STROKE);//空心
                mPaint.setStrokeWidth(3);
                mPaint.setAntiAlias(true);//消除锯齿
            }

//        canvas.drawText("haha", 20, 20, paint);
            int left = mIvIcon.getLeft();
            int right = mIvIcon.getRight();
            int top = mIvIcon.getTop();
            int bottom = mIvIcon.getBottom();
            if (mOval == null) {
                mOval = new RectF(left, top, right, bottom);
            }
            float startAngle = -90;
            float sweepAngle = mProgress * 1.0f / mMax * 360;//动态计算
            boolean useCenter = false;//是否连接中心
            canvas.drawArc(mOval, startAngle, sweepAngle, useCenter, mPaint);
        }
    }
}
