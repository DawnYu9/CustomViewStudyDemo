package com.bubble.customviewstudydemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bubble.customviewstudydemo.activity.BaseActivity;
import com.bubble.customviewstudydemo.activity.CustomImgActivity;
import com.bubble.customviewstudydemo.activity.CustomImgContainerActivity;
import com.bubble.customviewstudydemo.activity.CustomProgressBarActivity;
import com.bubble.customviewstudydemo.activity.CustomRandomTvActivity;
import com.bubble.customviewstudydemo.activity.CustomVolumControlActivity;

import java.util.ArrayList;

/**
 * description:
 * version:
 *
 * @author bubble
 * @date 2017/5/9
 */
public class MainActivity extends BaseActivity {
    private ArrayList<Button> btnList;
    private TextView tv_github;

    private static final String GITHUB_URL = "https://github.com/codingbubble/CustomViewStudyDemo";
    private static final String GITHUB_NAME = "github";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(R.layout.activity_main);
    }

    @Override
    public void initView() {
        tv_github = (TextView) findViewById(R.id.tv_github);

        Button btn_customtitleview, btn_customimageview, btn_customprogressbar, btn_customvolumcontrolbar, btn_customimgcontainer;
        btn_customtitleview = (Button) findViewById(R.id.btn_customtitleview);
        btn_customimageview = (Button) findViewById(R.id.btn_customimageview);
        btn_customprogressbar = (Button) findViewById(R.id.btn_customprogressbar);
        btn_customvolumcontrolbar = (Button) findViewById(R.id.btn_customvolumcontrolbar);
        btn_customimgcontainer = (Button) findViewById(R.id.btn_customimgcontainer);

        btnList = new ArrayList<>();
        btnList.add(btn_customtitleview);
        btnList.add(btn_customimageview);
        btnList.add(btn_customprogressbar);
        btnList.add(btn_customvolumcontrolbar);
        btnList.add(btn_customimgcontainer);
    }

    @Override
    public void initData() {
        super.initData();

        tv_github.setText(Html.fromHtml("源码：<a href='" + GITHUB_URL + "'>" + GITHUB_NAME + "</a>"));
        tv_github.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void registListener() {
        for (Button btn : btnList) {
            btn.setOnClickListener(clickListener);
        }
    }

    @Override
    public void onMyClick(View v) {
        super.onMyClick(v);

        switch (v.getId()) {
            case R.id.btn_customtitleview:
                startActivity(new Intent(mContext, CustomRandomTvActivity.class));
                break;
            case R.id.btn_customimageview:
                startActivity(new Intent(mContext, CustomImgActivity.class));
                break;
            case R.id.btn_customprogressbar:
                startActivity(new Intent(mContext, CustomProgressBarActivity.class));
                break;
            case R.id.btn_customvolumcontrolbar:
                startActivity(new Intent(mContext, CustomVolumControlActivity.class));
                break;
            case R.id.btn_customimgcontainer:
                startActivity(new Intent(mContext, CustomImgContainerActivity.class));
                break;
        }
    }
}
