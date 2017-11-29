package com.itheima.progressbtn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBtn progressBtn = (ProgressBtn) findViewById(R.id.progressBtn);
        progressBtn.setMax(200);
        progressBtn.setProgress(50);
        progressBtn.setText("25%");
    }
}
