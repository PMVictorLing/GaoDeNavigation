package com.lwc.gaodetest.commadapter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * recyclerView通用Adapter
 * <p>
 * Created by lingwancai on
 * 2018/9/14 15:18
 */
public abstract class CommonRecyclerViewAdaper<T> extends RecyclerView.Adapter<CommonRecyclerViewAdaper.ViewHolder> {

    //多条目支持
    private MulitiTypeSupport mTpyeSupport;
    private LayoutInflater mInflater;
    public Context mContext;

    //数据使用泛型
    private List<T> mDataList;

    //布局使用构造函数传递
    private int mLayoutId;

    public CommonRecyclerViewAdaper(Context context, List<T> mDataList, int mLayoutId) {
        this.mContext = context;
        this.mDataList = mDataList;
        this.mLayoutId = mLayoutId;
        this.mInflater = LayoutInflater.from(context);
    }

    public CommonRecyclerViewAdaper(Context context, List<T> mDataList, MulitiTypeSupport typeSupport) {
        this(context, mDataList, -1);
        this.mTpyeSupport = typeSupport;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mTpyeSupport != null) {
            //需要多布局
            mLayoutId = viewType;
        }

        //加载布局，返回ViewHolder
        View view = mInflater.inflate(mLayoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 会在onCreateViewHolder()之前调用
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        //判断是否有多布局，并返回布局id
        if (mTpyeSupport != null) {
            return mTpyeSupport.getLayoutId(mDataList.get(position));
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonRecyclerViewAdaper.ViewHolder holder, final int position) {
        convert(holder, mDataList.get(position));

        //item点击事件 只能利用接口回调
        if (mLisener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLisener.onClickLisener(position);
                }
            });
        }


    }

    /**
     * 绑定数据，使用抽象方法回传，把必要的参数传出
     *
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //item点击事件 只能利用接口回调
    private ItemOnClickLisener mLisener;

    public void setItemOnClickLisener(ItemOnClickLisener lisener) {
        this.mLisener = lisener;
    }

    /**
     * 增加ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //存放view的集合，用缓存已找到的view界面
        private SparseArray<View> mViewArray;

        public ViewHolder(View itemView) {
            super(itemView);
            mViewArray = new SparseArray<>();
        }

        //通用的功能进行封装
        //设置textView文本 *******采用链式构造调用 一般builder 设计模式*********
        public ViewHolder setTextView(int textViewId, String string) {
            //根据id获取textView
            TextView textView = (TextView) getView(textViewId);
            textView.setText(string);
            //采用链式调用
            return this;
        }

        //设置imageview --- 本地资源
        public ViewHolder setImageResource(int imageViewId, int resourceId) {
            ImageView imageView = getView(imageViewId);
            imageView.setImageResource(resourceId);
            return this;
        }

        //设置imageview --- 网络资源
        //图片处理问题，路径问题 使用第三方的 imageloader glide
        public ViewHolder setImagePath(int imageViewId, HolderImagerLoader imagerLoader) {
            ImageView imageView = getView(imageViewId);
            imagerLoader.loadImage(imageView, imagerLoader.getPath());
            return this;
        }

        //设置view组件是否显示
        public ViewHolder setViewVisibility(int viewId, int visibiliby) {
            getView(viewId).setVisibility(visibiliby);
            return this;
        }

        //设置条目点击事件
        /*public ViewHolder setItemOnClicklistener(int viewId, View.OnClickListener listener) {
            getView(viewId).setOnClickListener(listener);
            return this;
        }*/

        /**
         * 根据id从itemview中获取View T
         *
         * @param viewId
         * @param <T>
         * @return
         */
        public  <T extends View> T getView(int viewId) {
            //从缓存中获取view
            View view = mViewArray.get(viewId);

            //使用缓存的方式减少了findviewbyid的次数
            if (view == null) {
                view = itemView.findViewById(viewId);
                //存入view
                mViewArray.put(viewId, view);
            }
            return (T) view;
        }

    }

    /**
     * 图片加载 --- 使用解耦的方式 抽象出来
     */
    public abstract static class HolderImagerLoader {
        private String mPath;

        public HolderImagerLoader(String path) {
            this.mPath = path;
        }

        /**
         * 需要重写该方法，加载图片
         */
        public abstract void loadImage(ImageView imageView, String path);


        public String getPath() {
            return mPath;
        }
    }


}
