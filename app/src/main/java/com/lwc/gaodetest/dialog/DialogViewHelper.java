package com.lwc.gaodetest.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 辅助处理类
 * Created by lingwancai on
 * 2018/9/21 20:33
 */
public class DialogViewHelper {

    //存入引用
    HashMap<Integer, WeakReference<View>> mViews = new HashMap();
    private View mContentView = null;


    public DialogViewHelper(Context context, int viewLayoutResId) {
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public DialogViewHelper() {

    }

    /**
     * 设置Dialog布局
     *
     * @param contentView
     */
    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    //********************************************************************************************
    /**
     * //思考一个问题，就是Dialog中的控件拿不到想要的值以及相应的点击事件？
     //所以只能从把相应的方法放到Dialog中去
     */
    /**
     * 设置文本
     *
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setText(charSequence);
        }

    }

    public <T extends View> T getView(int viewId) {
        //防止内存泄漏
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }

        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    /**
     * 设置监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickLisener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

}
