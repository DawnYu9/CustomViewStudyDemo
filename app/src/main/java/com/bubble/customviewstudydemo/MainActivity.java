package com.bubble.customviewstudydemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bubble.customviewstudydemo.activity.CustomImgActivity;
import com.bubble.customviewstudydemo.activity.CustomImgContainerActivity;
import com.bubble.customviewstudydemo.activity.CustomProgressBarActivity;
import com.bubble.customviewstudydemo.activity.CustomRandomTvActivity;
import com.bubble.customviewstudydemo.activity.CustomVolumControlActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    ArrayList<Button> btnList;
    Button btn_customtitleview, btn_customimageview, btn_customprogressbar, btn_customvolumcontrolbar, btn_customimgcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
        registListener();
    }

    private void initView() {
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

    private void registListener() {
        for (Button btn : btnList) {
            btn.setOnClickListener(clickListener);
        }
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
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
    };
}
