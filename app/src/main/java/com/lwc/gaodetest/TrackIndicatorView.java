package com.lwc.gaodetest;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * ViewPager指示器
 * <p>
 * Created by lingwancai on
 * 2018/11/1 09:24
 */
public class TrackIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private static final String TAG = "TrackIndicatorView";
    //每页个数
    private int mVisTabNum = 4;

    //每个item的宽度
    private int mItemWidth = 0;

    private LinearLayout mIndicatorLinearLayout;

    private IndicatorBaseAdapter mAdapter;
    private ViewPager mViewPager;

    public TrackIndicatorView(Context context) {
        this(context, null);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化容器
        mIndicatorLinearLayout = new LinearLayout(context);
        addView(mIndicatorLinearLayout);

        //获取自定义属性 每个屏幕个数
        TypedArray arrys = context.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView);
        mVisTabNum = arrys.getInt(R.styleable.TrackIndicatorView_content_num, mVisTabNum);

        //释放
        arrys.recycle();
    }

    /**
     * 指定item的宽度
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //指定item宽度
            mItemWidth = getItemWidth();

            //循环指定宽度
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                //指定item的宽度
                mIndicatorLinearLayout.getChildAt(i).getLayoutParams().width = mItemWidth;
            }
            Log.e(TAG, "mItemWidth =>>" + mItemWidth);

        }
    }

    /**
     * 获取每个item宽度
     *
     * @return
     */
    private int getItemWidth() {
        int itemWidth = 0;
        // 获取当前控件的宽度
        int width = getWidth();
        if (mVisTabNum != 0) {
            // 在布局文件中指定一屏幕显示多少个
            itemWidth = width / mVisTabNum;
            return itemWidth;
        } // 如果没有指定获取最宽的一个作为ItemWidth
        int maxItemWidth = 0;
        int mItemCounts = mAdapter.getCount();
        // 总的宽度
        int allWidth = 0;
        for (int i = 0; i < mItemCounts; i++) {
            View itemView = mIndicatorLinearLayout.getChildAt(i);
            int childWidth = itemView.getMeasuredWidth();
            maxItemWidth = Math.max(maxItemWidth, childWidth);
            allWidth += childWidth;
        }
        itemWidth = maxItemWidth;
        // 如果不足一个屏那么宽度就为 width/mItemCounts
        if (allWidth < width) {
            itemWidth = width / mItemCounts;
        }
        return itemWidth;
    }

    /**
     * 设置适配
     *
     * @param adapter
     */
    public void setAdapter(IndicatorBaseAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter cannot be null!");
        }
        this.mAdapter = adapter;

        //获取item个数
        int count = mAdapter.getCount();

        //动态添加布局
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i, mIndicatorLinearLayout);
            mIndicatorLinearLayout.addView(view);
        }

    }

    //重载setAdapter,监听viewpager
    public void setAdapter(IndicatorBaseAdapter adapter, ViewPager viewPager) {
        //直接调用重载的方法
        setAdapter(adapter);

        //为viewpager添加监听
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 在ViewPager滚动的时候会不断的调用该方法
        Log.e(TAG, "position --> " + position + " positionOffset --> " + positionOffset);
        // 在不断滚动的时候让头部的当前Item一直保持在最中心
        indicatorScrollTo(position, positionOffset);

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 不断滚动的头部
     *
     * @param position
     * @param positionOffset
     */
    private void indicatorScrollTo(int position, float positionOffset) {
        // 当前的偏移量
        int currentOffset = (int) ((position + positionOffset) * mItemWidth);
        // 原始的左边的偏移量
        int originLeftOffset = (getWidth() - mItemWidth) / 2;
        // 当前应该滚动的位置
        int scrollToOffset = currentOffset - originLeftOffset;
        // 调用ScrollView的scrollTo方法
        scrollTo(scrollToOffset, 0);
    }
}
