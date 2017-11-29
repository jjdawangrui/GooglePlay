package com.itheima_zphuan.tablayoutdemo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author: 钟佩桓
 * date: 2017/2/27
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"tab1","tab2","tab3"};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    private int[] imageResId = {
            R.drawable.ic_pause,
            R.drawable.ic_redownload,
            R.drawable.ic_resume
    };

    @Override
    public CharSequence getPageTitle(int position) {
        //第一种方式纯文本显示
//        return tabTitles[position];

        //第二种方式只显示图片
//        Drawable image = context.getResources().getDrawable(imageResId[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;

        //第三种方式既显示图片又显示文字
//        Drawable image = context.getResources().getDrawable(imageResId[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        // Replace blank spaces with image icon
//        SpannableString sb = new SpannableString("   " + tabTitles[position]);
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;

            return null;
    }

    //第四种方式-自定义view
    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        tv.setTextSize(18);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(imageResId[position]);
        return view;
    }
}
