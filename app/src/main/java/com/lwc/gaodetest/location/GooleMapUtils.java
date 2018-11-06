package com.lwc.gaodetest.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by lingwancai on
 * 2018/11/6 10:02
 */
public class GooleMapUtils {
    private static GooleMapUtils amap;
    private AMapLocationListener mLocationListener;
    private Context context;
    private GetGooleMapListener getGooleMapListener1;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    public static GooleMapUtils getInstence() {
        if (amap == null) {
            amap = new GooleMapUtils();
        }
        return amap;
    }

    public void init(final Context context, GetGooleMapListener getGooleMapListener) {
        this.context = context;
        this.getGooleMapListener1 = getGooleMapListener;
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                // Log.v("sssssss","sssssss这个是地区id 比如海珠区"+aMapLocation);
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    if (getGooleMapListener1 != null)
                        getGooleMapListener1.onMapListener(aMapLocation.getCity(), aMapLocation, true);
                } else {
                    if (getGooleMapListener1 != null)
                        getGooleMapListener1.onMapListener("定位失败", null, false);
                }
            }
        };
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(30 * 60 * 1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public interface GetGooleMapListener {

        void onMapListener(String cityName, AMapLocation aMapLocation, boolean location);
    }

}
