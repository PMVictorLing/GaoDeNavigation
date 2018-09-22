package com.lwc.gaodetest.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 辅助处理类
 * Created by lingwancai on
 * 2018/9/21 20:33
 */
public class DialogViewHelper {

    private View mContentView = null;


    public DialogViewHelper(Context context, int viewLayoutResId) {
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public DialogViewHelper() {

    }

    /**
     *
     * 设置Dialog布局
     * @param contentView
     */
    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {

    }

    /**
     * 设置监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickLisener(int viewId, View.OnClickListener listener) {

    }

    /**
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }
}
