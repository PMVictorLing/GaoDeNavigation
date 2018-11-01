package com.lwc.gaodetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;


import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;



/**
 * @Description:  根据经纬定位
 * @Author:  lingwancai
 * @Time:  2018/10/31 17:09
 */

public class LatlngActivity extends Activity implements LocationSource,AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private CameraUpdate mUpdata;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latlng);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setCompassEnabled(true);
            mUpdata = CameraUpdateFactory.newCameraPosition(
//15是缩放比例，0是倾斜度，30显示比例
                    new CameraPosition(new LatLng(40.043212, 116.299728), 15, 0, 30));//这是地理位置，就是经纬度。
            aMap.moveCamera(mUpdata);//定位的方法
            drawMarkers();
        }
    }

    public void drawMarkers() {

        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.043212, 116.299728))
                .title("八维")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
// TODO Auto-generated method stub

    }

    @Override
    public void activate(OnLocationChangedListener arg0) {
// TODO Auto-generated method stub

    }

    @Override
    public void deactivate() {
// TODO Auto-generated method stub

    }

}
