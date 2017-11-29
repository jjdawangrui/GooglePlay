package com.itheima_zphuan.tablayoutdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //绑定viewpager和tablayout
        tabLayout.setupWithViewPager(viewPager);
        //设置viewpaer的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);





        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View tabView = pagerAdapter.getTabView(i);
            if(i==0){
                TextView tv = (TextView) tabView.findViewById(R.id.textView);
                tv.setTextColor(Color.RED);
                tv.setTextSize(25);
            }
            tab.setCustomView(tabView);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                        TextView tv = (TextView) tab.getCustomView().findViewById(R.id.textView);
                    if(i==position){
                        tv.setTextColor(Color.RED);
                        tv.setTextSize(25);
                    }else{
                        tv.setTextColor(Color.GRAY);
                        tv.setTextSize(18);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
