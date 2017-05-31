package com.bubble.customviewstudydemo.activity;

import android.os.Bundle;

import com.bubble.customviewstudydemo.R;

/**
 * @description
 * @date 17/5/18
 */

public class CustomImgContainerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(R.layout.custom_img_container);
    }

    @Override
    public void initData() {
        setTitle("CustomImgContainerActivity");

        setBlogInfo("Android 手把手教您自定义ViewGroup（一）",
                "http://blog.csdn.net/lmj623565791/article/details/38339817");
    }
}
