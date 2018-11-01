package com.lwc.gaodetest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwc.gaodetest.Adapter.ViewPagerCommonAdapter;
import com.lwc.gaodetest.fragment.PagerItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 万能的指示器
 * @Author: lingwancai
 * @Time: 2018/11/1 9:43
 */

public class IndicationAdapterActivity extends AppCompatActivity {

    private TrackIndicatorView mIndicatorView;

    private ArrayList<String> mDataList = new ArrayList<>();
    private ViewPager mViewPager;
    private ViewPagerCommonAdapter mViewPagerCommonAdapter;

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    //初始化item适配器
    class MyIndicatorAdapter extends IndicatorBaseAdapter {

        private List<String> mList;
        private Context mContext;

        public MyIndicatorAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(int position, ViewGroup viewGroup) {
            //加载item布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_view, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTitle.setText(mList.get(position) + "");
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indication_adapter);
        mIndicatorView = (TrackIndicatorView) this.findViewById(R.id.trackIndicatorView);

        //初始化viewpager
        mViewPager = (ViewPager) this.findViewById(R.id.viewPager);

        for (int i = 0; i < 15; i++) {
            if (0 == i % 2) {
                mDataList.add("  ++现货++  " + i);
            }
            mDataList.add("  现货  " + i);
            mFragmentList.add(PagerItemFragment.newInstance(""+i,"现货"+i));
        }
        MyIndicatorAdapter myIndicatorAdapter = new MyIndicatorAdapter(this, mDataList);
        mIndicatorView.setAdapter(myIndicatorAdapter, mViewPager);


        mViewPagerCommonAdapter = new ViewPagerCommonAdapter(this.getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mViewPagerCommonAdapter);

    }
}
