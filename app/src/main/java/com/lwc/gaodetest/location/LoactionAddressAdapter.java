package com.lwc.gaodetest.location;

import android.content.Context;
import android.widget.ImageView;

import com.amap.api.services.core.PoiItem;
import com.lwc.gaodetest.R;
import com.lwc.gaodetest.commadapter.adapter.CommonRecyclerViewAdaper;

import java.util.List;

/**
 * Created by lingwancai on
 * 2018/11/6 09:41
 */
public class LoactionAddressAdapter extends CommonRecyclerViewAdaper<locationAdressBean> {
    public LoactionAddressAdapter(Context context, List<locationAdressBean> mDataList, int mLayoutId) {
        super(context, mDataList, mLayoutId);
    }

    @Override
    public void convert(ViewHolder holder, locationAdressBean item) {
        PoiItem mPoiItem = item.getPoiItem();
        holder.setTextView(R.id.tv_title, mPoiItem.getTitle() + "")
                .setTextView(R.id.tv_title_details, mPoiItem.getProvinceName() + mPoiItem.getCityName() +
                        mPoiItem.getAdName() + mPoiItem.getSnippet() + "");
        ImageView ivIsSelect = (ImageView) holder.getView(R.id.iv_select);
        //是否选择
        if (item.getIsSelect()){
            ivIsSelect.setImageResource(R.drawable.cart_selected_icon);
        } else {
            ivIsSelect.setImageResource(R.drawable.cart_unselected_icon);
        }

    }
}
