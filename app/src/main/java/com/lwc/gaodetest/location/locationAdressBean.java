package com.lwc.gaodetest.location;

import com.amap.api.services.core.PoiItem;

/**
 * Created by lingwancai on
 * 2018/11/6 09:40
 */
public class locationAdressBean {

    //是否选择
    private boolean isSelect;

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    private String addressName;
    private String addressDetails;
    private PoiItem poiItem;

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }
}
