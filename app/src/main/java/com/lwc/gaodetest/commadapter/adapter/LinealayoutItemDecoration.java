package com.lwc.gaodetest.commadapter.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自定义 listView样式的分割线
 *
 * Created by lingwancai on
 * 2018/9/17 13:47
 */
public class LinealayoutItemDecoration extends RecyclerView.ItemDecoration{

    private static final String TAG = "LinealayoutItemDecoration";
    private Drawable mDrawabler;

    public LinealayoutItemDecoration(Context context, int resDrawableId) {
        //获取drawable
        mDrawabler = ContextCompat.getDrawable(context,resDrawableId);
    }

    /**
     * 基本操作，留出分割线的位置
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //有两种思路：一个是recyclerview的item底部bottom绘制分割线，另一个是top绘制

        //1.绘制底部分割线--会有问题可以试试

        //2.另一个是top绘制
        //获取item的position
        int position = parent.getChildAdapterPosition(view);
//        Log.d(TAG, "position ->" + position + " itemCount ->" + parent.getChildCount());
        //保证第一条不需要绘制
        if (position != 0) {
            outRect.top = mDrawabler.getIntrinsicHeight();
        }
    }

    /**
     * 绘制分割线的区域
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //在每个item头部绘制
        int childCount = parent.getChildCount();

        //计算区域
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();

        //第一个头部不绘制 --- 如new GridLayoutManager(this,2) -根据每列来确定从哪里开始绘制 i=2
        for (int i = 1; i < childCount; i++) {
            //分割线的底部bottom就是itemView的头部
            rect.bottom = parent.getChildAt(i).getTop();
            rect.top = rect.bottom - mDrawabler.getIntrinsicHeight();
//            c.drawRect(rect, mPaint);
            mDrawabler.setBounds(rect);
            mDrawabler.draw(c);
        }

    }
    
    
}
