package com.bubble.customviewstudydemo.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.bubble.customviewstudydemo.R;
import com.bubble.customviewstudydemo.utils.StringUtils;
import com.bubble.customviewstudydemo.view.CustomProgressBar;

/**
 * description: 圆环进度条
 * version:
 *
 * @author DawnYu
 * @date 2017/5/18
 */
public class CustomProgressBarActivity extends BaseActivity {
    private EditText et_length, et_padding, et_speed;

    private CustomProgressBar customProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(R.layout.activity_custom_pgbar, true, true);
    }

    @Override
    public void initView() {
        et_length = findView(R.id.et_length);
        et_padding = findView(R.id.et_padding);
        et_speed = findView(R.id.et_speed);
        customProgressBar = findView(R.id.customProgressBar);
    }

    @Override
    public void initData() {
        setTitle("CustomProgressBarActivity");

        setBlogInfo("Android 自定义View (三) 圆环交替 等待效果",
                "http://blog.csdn.net/lmj623565791/article/details/24500107");
    }

    @Override
    public void save() {
        customLayoutParams = customProgressBar.getLayoutParams();

        //设置边长
        String length = et_length.getText().toString();
        if (!StringUtils.isNullOrEmpty(length) && customLayoutParams != null) {
            setSquareViewLength(length);
            customProgressBar.setLayoutParams(customLayoutParams);
        }

        //设置 padding
        String paddingString = et_padding.getText().toString();
        if (!StringUtils.isNullOrEmpty(paddingString)) {
            int padding = StringUtils.getDip(mContext, paddingString);
            if (padding >= 0) {
                customProgressBar.setPadding(padding, padding, padding, padding);
            } else {
                Toast.makeText(mContext, "padding 不能为负数", Toast.LENGTH_SHORT).show();
            }
        }

        //设置速度
        String speed = et_speed.getText().toString();
        if (!StringUtils.isNullOrEmpty(speed) && StringUtils.canParseDouble(speed)) {
            if (Integer.parseInt(speed) > 0) {
                customProgressBar.setSpeed(Integer.parseInt(speed));
            } else {
                Toast.makeText(mContext, "速度应大于 0 ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customProgressBar.stopThread(true);
    }
}
