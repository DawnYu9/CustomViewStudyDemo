package com.bubble.customviewstudydemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bubble.customviewstudydemo.R;
import com.bubble.customviewstudydemo.utils.StringUtils;

/**
 * description:
 * version:
 *
 * @author DawnYu
 * @date 2017/5/24
 */
public class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    public Context mContext;

    /**
     * 博客的链接
     */
    public TextView tv_blog;

    /**
     * 保存按钮
     */
    public Button btn_save;

    /**
     * 自定义 View 的 LayoutParams
     */
    public ViewGroup.LayoutParams customLayoutParams;
    private LayoutInflater inflater;
    private View contentView;

    public BaseActivity() {
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置返回键
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 初始化封装，子类调用
     *
     * @param layoutResID
     * @param showBaseHeader
     * @param canCustom      是否可以设置自定义属性
     */
    public void init(int layoutResID, boolean showBaseHeader, boolean canCustom) {
        if (showBaseHeader) {
            LinearLayout rootView = new LinearLayout(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rootView.setLayoutParams(lp);
            rootView.setOrientation(LinearLayout.VERTICAL);

            //顶部博客部分
            tv_blog = new TextView(mContext);
            tv_blog.setTextSize(14);
            int padding = StringUtils.getDip(mContext, 5);
            tv_blog.setPadding(padding, padding, padding, padding);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rootView.addView(tv_blog, lp);

            //内容
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(layoutResID, null);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rootView.addView(contentView, lp);

            setContentView(rootView);
        } else {
            setContentView(layoutResID);
        }

        initBase(canCustom);
        initView();
        initData();
        registListener();
    }

    private void initBase(boolean canCustom) {
        if (canCustom) {
            btn_save = findView(R.id.btn_save);
            btn_save.setOnClickListener(clickListener);
        }
    }

    /**
     * 初始化控件
     */
    public void initView() {
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 注册监听事件
     */
    public void registListener() {
    }

    /**
     * 点击事件监听
     * 子类调用
     */
    public View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            onMyClick(v);
        }
    };

    /**
     * 子类复写
     */
    public void onMyClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                save();
                break;
        }
    }

    /**
     * 保存设置
     */
    public void save() {
    }

    /**
     * 设置点击跳转博客页面
     */
    public void setBlogInfo(String blogTitle, String blogUrl) {
        if (tv_blog == null) {
            return;
        }
        tv_blog.setText(Html.fromHtml("参考：<a href='" + blogUrl + "'>" + blogTitle + "</a>"));
        tv_blog.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 设置自定义正方形 View 的边长
     *
     * @param length
     */
    public void setSquareViewLength(String length) {
        if (customLayoutParams != null) {
            switch (length) {
                case "match_parent":
                    customLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    customLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    break;
                case "wrap_content":
                    customLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    customLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    break;
                default:
                    if (!StringUtils.isNullOrEmpty(length) && StringUtils.canParseDouble(length)) {
                        int len = StringUtils.getDip(mContext, length);
                        customLayoutParams.width = len;
                        customLayoutParams.height = len;
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://返回键
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected <T extends View> T findView(int resId) {
        return (T) findViewById(resId);
    }
}
