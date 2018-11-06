package com.lwc.gaodetest.commadapter.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自定义 GridView样式的分割线
 * <p>
 * Created by lingwancai on
 * 2018/9/17 13:47
 */
public class GridlayoutItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "GridlayoutItemDecoration";
    private Drawable mDrawabler;

    public GridlayoutItemDecoration(Context context, int resDrawableId) {
        //获取drawable
        mDrawabler = ContextCompat.getDrawable(context, resDrawableId);
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
        //绘制的区域，bottom和right
        outRect.bottom = mDrawabler.getIntrinsicHeight();
        outRect.right = mDrawabler.getIntrinsicWidth();
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
        //绘制水平分割线
        drawHeri(c, parent);
        //绘制垂直
        drawVert(c, parent);

    }

    private void drawVert(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();

            int left = childView.getLeft() - params.leftMargin;
            int right = childView.getRight() + mDrawabler.getIntrinsicWidth()+params.rightMargin;
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + mDrawabler.getIntrinsicHeight();

            //设置水平分割线的位置
            mDrawabler.setBounds(left,top,right,bottom);
            mDrawabler.draw(c);

        }
    }

    private void drawHeri(Canvas c, RecyclerView parent) {
    }


}
