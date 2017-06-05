package com.bubble.customviewstudydemo.activity;

import android.os.Bundle;

import com.bubble.customviewstudydemo.R;

/**
 * @description
 * @date 17/5/18
 */

public class CustomImgActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(R.layout.activity_custom_img);
    }

    @Override
    public void initData() {
        setTitle("CustomImgActivity");

        setBlogInfo("Android 自定义View (二) 进阶",
                "http://blog.csdn.net/lmj623565791/article/details/24300125");
    }
}
