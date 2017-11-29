package com.itheima.progressview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressView progressView = (ProgressView) findViewById(R.id.progressView);
        progressView.setIcon(R.drawable.ic_pause);

        //一个自定义控件,直接或者间接继承ViewGroup,需要设置背景才可以触发onDraw方法
//        progressView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressView.setMax(200);
        progressView.setProgress(100);
        progressView.setNote("50%");

    }
}
