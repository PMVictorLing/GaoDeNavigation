package com.lwc.gaodetest.location;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lwc.gaodetest.R;
import com.lwc.gaodetest.commadapter.adapter.ItemOnClickLisener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 地图定位
 * @Author: lingwancai
 * @Time: 2018/11/6 9:23
 */

public class MapLocationActivity extends AppCompatActivity {

    private static final String TAG = "MapLocationActivity";
    private MapView mMapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private RecyclerView recyclerView;
    private ArrayList<locationAdressBean> locationAddressList;
    private LoactionAddressAdapter loactionAddressAdapter;
    private Marker marker;
    private PoiSearch.Query poiquery;
    private GeocodeSearch mGeocodeSearch;
    private EditText etInput;
    //默认全国
    private String mCityCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        mMapView = (MapView) this.findViewById(R.id.mapView);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理 
        mMapView.onCreate(savedInstanceState);
        initView();
        initListener();

        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void initListener() {
    }

    private void initView() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        aMap.setTrafficEnabled(true);
        // 显示实时交通状况 //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        recyclerView = this.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationAddressList = new ArrayList<>();
        loactionAddressAdapter = new LoactionAddressAdapter(this, locationAddressList, R.layout.item_address_position_view);
        recyclerView.setAdapter(loactionAddressAdapter);
        //item点击
        loactionAddressAdapter.setItemOnClickLisener(new ItemOnClickLisener() {
            @Override
            public void onClickLisener(int position) {
                Toast.makeText(MapLocationActivity.this, "点击", Toast.LENGTH_LONG).show();
                if (locationAddressList != null)
                    for (locationAdressBean item : locationAddressList) {
                        //已经选择的置为false
                        if (item.getIsSelect()) {
                            item.setIsSelect(false);
                        } else {
                            //没有选择的置为true
                            if (item.equals(locationAddressList.get(position)))
                                item.setIsSelect(true);
                        }
                    }
                loactionAddressAdapter.notifyDataSetChanged();
            }
        });

        //定位
        location();

        //拖动地图
        onDragMap();

        //搜索poi
        etInput = (EditText)this.findViewById(R.id.et_input);
        hideInput(MapLocationActivity.this,etInput);
        this.findViewById(R.id.iv_seach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput(MapLocationActivity.this,etInput);
                if (etInput.getText() != null) {
                    searchList(etInput.getText().toString(), mCityCode);
                } else {
                    Toast.makeText(MapLocationActivity.this, "请输入地址", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    /**
     * 强制隐藏输入法键盘
     *
     * @param context Context
     * @param view    EditText
     */
    public void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void onDragMap() {
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChange ==>" + cameraPosition.target.latitude + "\n" + cameraPosition.target.longitude);

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChangeFinish ==>" + cameraPosition.target.latitude + "\n" + cameraPosition.target.longitude);
                searchList(cameraPosition.target.latitude, cameraPosition.target.longitude);
                //增加marke
                addmark(cameraPosition.target.latitude, cameraPosition.target.longitude);

            }
        });
    }

    /**
     * 定位
     */
    private void location() {
        GooleMapUtils.getInstence().init(this, new GooleMapUtils.GetGooleMapListener() {
            @Override
            public void onMapListener(String cityName, AMapLocation aMapLocation, boolean location) {
                if (true) {
                    if (!TextUtils.isEmpty(aMapLocation.getCityCode()) && !TextUtils.isEmpty(aMapLocation.getRoad())) {
                        Log.e(TAG, "city => " + aMapLocation.getCityCode() + " \n" +
                                "aMapLocation.getLatitude()=>" + aMapLocation.getLatitude() + " aMapLocation.getLongitude()=>" + aMapLocation.getLongitude());
                        mCityCode = aMapLocation.getCityCode();
                        searchList(aMapLocation.getCityCode(), aMapLocation.getRoad());
                        //把地图移动到定位地点
                        moveMapCamera(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        addmark(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                        //获取GeocodeSearch
                        initGeocodeSearch(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    }
                } else {
                    Toast.makeText(MapLocationActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 根据经纬度得到地址
     */
    public void getAddress(double latitude, double longitude) {
    }

    /**
     * 经纬度转地址
     *
     * @param latitude
     * @param longitude
     */
    private void initGeocodeSearch(double latitude, double longitude) {

        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                //解析result获取地址描述信息
                if (i == 1000) {
                    Log.e(TAG, "regeocodeResult==>" + regeocodeResult.getRegeocodeAddress().getCity() + "\n address==>" +
                            regeocodeResult.getRegeocodeAddress().getFormatAddress());
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);

        mGeocodeSearch.getFromLocationAsyn(query);
    }

    //把地图画面移动到定位地点
    private void moveMapCamera(double latitude, double longitude) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
    }

    private void addmark(double latitude, double longitude) {
        if (marker == null) {
            marker = aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))
                    .draggable(true));
        } else {
            marker.setPosition(new LatLng(latitude, longitude));
//            aMap.invalidate();
        }
    }

    //根据城市code poi搜索
    private void searchList(String keyword, String cityCode) {
        Log.e(TAG, "keyword ==>" + keyword + "\n cityCode" +cityCode);
        if (TextUtils.isEmpty(keyword)) {
            locationAddressList.clear();
            loactionAddressAdapter.notifyDataSetChanged();
        }
        poiquery = new PoiSearch.Query(keyword, "", cityCode);
        poiquery.setPageSize(15);
        poiquery.setPageNum(2);
        PoiSearch poiSearch = new PoiSearch(this, poiquery);
        poiSearch.setOnPoiSearchListener(onPoiSearchListener);
        poiSearch.searchPOIAsyn();
    }

    //根据经纬度 poi搜索
    private void searchList(double latitude, double longitude) {
        if (latitude == 0 || longitude == 0) {
            locationAddressList.clear();
            loactionAddressAdapter.notifyDataSetChanged();
        }
        poiquery = new PoiSearch.Query("", "", "");
        poiquery.setPageSize(15);
        poiquery.setPageNum(2);
        PoiSearch poiSearch = new PoiSearch(this, poiquery);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 500, true));
        poiSearch.setOnPoiSearchListener(onPoiSearchListener);
        poiSearch.searchPOIAsyn();
    }

    //索引搜索
    PoiSearch.OnPoiSearchListener onPoiSearchListener = new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult result, int rCode) {
            if (rCode == 1000) {
                if (result != null && result.getQuery() != null) {
                    // 搜索poi的结果
                    if (result.getQuery().equals(poiquery)) {
                        Log.e(TAG,"poi搜索 poiquery=》"+poiquery);
                        // 是否是同一条 // poiResult = result;
                        // 取得搜索到的poiitems有多少页
                        ArrayList<com.amap.api.services.core.PoiItem> poiItems = result.getPois();
                        // 取得第一页的poiitem数据，页数从数字0开始
                        List<SuggestionCity> suggestionCities = result.getSearchSuggestionCitys();
                        // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                        locationAddressList.clear();
                        if (poiItems != null && poiItems.size() > 0) {
                            for (int i = 0; i < poiItems.size(); i++) {
                                Log.e(TAG, "poitiems snippet ==>" + poiItems.get(i).getSnippet() + " address title =>" +
                                        poiItems.get(i).getTitle() + " \n城市=>" + poiItems.get(i).getCityName() + " \n省份=》" + poiItems.get(i).getProvinceName() + "\n"
                                        + " 区域=》 " + poiItems.get(i).getAdName());
                                locationAdressBean locationAddress = new locationAdressBean();
                                locationAddress.setPoiItem(poiItems.get(i));
                                locationAddressList.add(locationAddress);
                            }
                            /* if (isSearch){ moveMapCamera(poiItems.get(0).getLatLonPoint().getLatitude(),poiItems.get(0).getLatLonPoint().getLongitude()); }*/
                        }
                        loactionAddressAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                Log.e(TAG,"poi搜索 失败");
            }
        }

        @Override
        public void onPoiItemSearched(com.amap.api.services.core.PoiItem poiItem, int i) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause(); //在activity执行onPause时执行
        //mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行
        // mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }
}


