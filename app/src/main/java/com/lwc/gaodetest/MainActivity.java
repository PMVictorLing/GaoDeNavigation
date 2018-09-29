package com.lwc.gaodetest;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.lwc.gaodetest.dialog.MyAlertDialog;
import com.lwc.gaodetest.http.HttpUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends CheckPermissionsActivity implements LocationSource {

    private static final String TAG = "MainActivity";
    private TextView locationText;

    //定位
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    //地图
    private MapView mMapView;
    //初始化地图控制器对象
    AMap aMap;
    //测试用
    private Marker changshamarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.tv_start_nva).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SingleRouteCalculateActivity.class));
            }
        });

        this.findViewById(R.id.bt_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //.setStartBottom(true)
                final MyAlertDialog dialog = new MyAlertDialog.Builder(MainActivity.this)
                        .setContentView(R.layout.dialog_layout)
                        .setText(R.id.tv_start_nva, "重新定位")
                        .setFullWidth().adddefaultAnimation().show();

                //获取到的值
                final TextView textView = dialog.getView(R.id.tv_start_nva);
                dialog.setOnClickLisener(R.id.tv_start_nva, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        locationOption = getDefaultOption();
                        //设置定位参数
                        locationClient.setLocationOption(locationOption);
                        startLocation();
                        Toast.makeText(MainActivity.this, "获取到的值>>>>" + textView.getText(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });

                dialog.setOnClickLisener(R.id.bt_cancle, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "获取到的值>>>> 取消", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        //定位
        locationText = (TextView) this.findViewById(R.id.tv_string_size);
//        initLocation();
//        startLocation();

        //初始化地图
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //显示室内地图
        aMap.showIndoorMap(true);


        //绘制覆盖物 marker
        changshamarker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(28.198439, 112.970308))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.poi_marker_pressed)))
                //设置marker平贴地图效果 setFlat
                .title("长沙 ¥ 4850元/吨").setFlat(true).visible(true)
                .draggable(true));

        //.addMarkers(markerOptionlst, true);添加多个点
//        aMap.addMarkers(markerOptionlst, true);
        Marker zhuzhoumarker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(27.833333, 113.166666))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.poi_marker_pressed)))
                //设置marker平贴地图效果 setFlat
                .title("株洲 ¥ 4600元/吨").setFlat(true).visible(true)
                .draggable(true));

        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);

        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(mInfoWindowListener);

        //自定义infoWindow
        aMap.setInfoWindowAdapter(new MyInfoWindowAdapter());//AMap类中

        //当前地图的缩放级别为
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                Toast.makeText(MainActivity.this, "缩放" + "当前地图的缩放级别为: " + cameraPosition.zoom, +Toast.LENGTH_LONG).show();
                if (cameraPosition.zoom > 5) {
                    //如果是不可见
                    if (!changshamarker.isInfoWindowShown()) {
                        changshamarker.showInfoWindow();
                        changshamarker.setVisible(true);

                        Log.e(TAG, "!changshamarker.isVisible");
                    }
                }

                if (cameraPosition.zoom <= 5) {
                    //如果是显示
                    if (changshamarker.isInfoWindowShown()) {
                        changshamarker.hideInfoWindow();
                        changshamarker.setVisible(false);
                        Log.e(TAG, "changshamarker.isVisible");
                    }
                }
            }
        });

        //地图的加减 监听缩放
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                Toast.makeText(MainActivity.this, "加减" + "当前地图的缩放级别为: " + aMap.getCameraPosition().zoom, +Toast.LENGTH_LONG).show();
            }
        });

//        addCircleOptions().setVisible(true);

        //请求测试
        HttpUtils.with(this).Url("").addParam("","").get().execute(null);

    }

    /**
     * 添加一个圆
     */
    private Circle addCircleOptions() {
        Circle circle = aMap.addCircle(new CircleOptions().center(new LatLng(28.198439, 112.970308)).radius(10000)
                .strokeWidth(2f).strokeColor(R.color.colorPrimary).fillColor(R.color.colorPrimary));
        return circle;
    }

    /**
     * 自定义infoWindow样式
     */
    class MyInfoWindowAdapter implements AMap.InfoWindowAdapter {

        private View mInfoWindow;

        /**
         * 监听自定义infowindow窗口的infowindow事件回调
         *
         * @param marker
         * @return
         */
        @Override
        public View getInfoWindow(Marker marker) {
            if (mInfoWindow == null) {
                mInfoWindow = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.map_location_info_window, null);
            }
            render(marker, mInfoWindow);
            return mInfoWindow;
        }

        private void render(Marker marker, View mInfoWindow) {
            TextView tvTitle = (TextView) mInfoWindow.findViewById(R.id.tv_title);
            TextView tvPrices = (TextView) mInfoWindow.findViewById(R.id.tv_prices);
            tvPrices.setText(marker.getTitle() + "");
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    /**
     * infoWindow监听回调
     */
    AMap.OnInfoWindowClickListener mInfoWindowListener = new AMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker arg0) {
            //修改值
//            arg0.setTitle("infowindow clicked");
            Toast.makeText(MainActivity.this, "" + arg0.getTitle(), +Toast.LENGTH_LONG).show();
        }
    };

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(MainActivity.this, "" + marker.getTitle(), +Toast.LENGTH_LONG).show();
//            marker.showInfoWindow();
            return false;
        }
    };

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
//        resetOption();
        // 设置定位参数
//        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {

            if (mListener != null && location != null) {
                if (location != null
                        && location.getErrorCode() == 0) {
                    mListener.onLocationChanged(location);// 显示系统小蓝点
                    Log.e("AmapErr", "经    度    : " + location.getLongitude() + "\n"
                            + "纬    度    : " + location.getLatitude() + "\n");
                } else {
                    String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                    Log.e("AmapErr", errText);
                }
            }

            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
                sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                locationText.setText(result);
            } else {
                locationText.setText("定位失败，loc is null");
            }
        }
    };

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    private SimpleDateFormat sdf = null;

    public String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {

        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(locationListener);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
