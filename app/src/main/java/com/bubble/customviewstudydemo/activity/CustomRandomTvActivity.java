package com.bubble.customviewstudydemo.activity;

import android.os.Bundle;

import com.bubble.customviewstudydemo.R;

/**
 * description: 点击切换4位随机数
 * version:
 *
 * @author bubble
 * @date 2017/5/18
 */
public class CustomRandomTvActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(R.layout.activity_custom_random_tv);
    }

    @Override
    public void initData() {
        setTitle("CustomRandomTvActivity");

        setBlogInfo("Android 自定义View (一)",
                "http://blog.csdn.net/lmj623565791/article/details/24252901");
    }
}
