package com.example.igo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;


import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.igo.R;
import com.example.igo.util.MyLocationListener;

/**
 * Created by Administrator on 2016/10/20.
 */
public class BaseMapActivity extends Activity implements View.OnClickListener {

    public MapView mMapView;
    public BaiduMap mMap;
    BitmapDescriptor mCurrentMarker;
    private LocationClient mClient;
    Button mButtonNext;
    Button mButtonCheck;
    EditText mEditText;

    private boolean isFirstLoc = true;
    private String TAG = "BaseMapActivity";
    private MyLocationListener mListener;
    private PoiSearch mPoiSearch;

    private int totalPage = 0;
    private double mLatitude;
    private double mLongitude;

    private int page = 0;
    private String mType;

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        initView();
        location();

    }

    private void initView() {
        mButtonCheck = (Button) findViewById(R.id.btn_check);
        mButtonNext = (Button) findViewById(R.id.btn_next);
        mEditText = (EditText) findViewById(R.id.et_base_type);
        mButtonCheck.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
    }

    public void setLocation(double lat,double longitude) {
        mLatitude = lat;
        mLongitude = longitude;
    }


    private void setView() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_base_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        //POI搜索权限
        mMapView.showZoomControls(false);
        mMap = mMapView.getMap();

        // 设置开启定位自己的位置
        mMap.setMyLocationEnabled(true);
        // 设置定位点图标，可以使用系统的也可以自定义
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_global_landmark_groupon_list);
        // 如果使用系统的让mCurrentMarker为空即可
        mMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
        //查找------------------------------------
        mPoiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
    }

    //-----------------------
    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(BaseMapActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                return;
            }

            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                mMap.clear();
                MyPoiOverlay poiOverlay = new MyPoiOverlay(mMap);
                poiOverlay.setData(poiResult);// 设置POI数据
                mMap.setOnMarkerClickListener(poiOverlay);
                poiOverlay.addToMap();// 将所有的overlay添加到地图上
                poiOverlay.zoomToSpan();
                //
                totalPage = poiResult.getTotalPageNum();// 获取总分页数
                Toast.makeText(BaseMapActivity.this, "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为" + totalPage + "页", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(BaseMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            } else {// 正常返回结果的时候，此处可以获得很多相关信息
                Toast.makeText(BaseMapActivity.this,
                        poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
                        Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri CONTENT_URI_BROWSERS = Uri.parse(poiDetailResult.getDetailUrl());
                intent.setData(CONTENT_URI_BROWSERS);
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intent);
            }
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

        private void location() {
            mClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
            mListener = new MyLocationListener(mMapView, mMap,BaseMapActivity.this);
            // 注册监听
            mClient.registerLocationListener(mListener);
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
            option.setOpenGps(true);// 打开gps
            // option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
            //option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向


            option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
            option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

            mClient.setLocOption(option);

//            mLatitude = ((MyLocationListener)mListener).getmLatitude();
//            mLongitude = ((MyLocationListener)mListener).getmLongitude();
//            Log.i("POILocation",""+mLatitude+mLongitude);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            mMapView.onDestroy();
        }

        @Override
        protected void onResume() {
            super.onResume();
            location();
            //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
            mMapView.onResume();
            mClient.start();
        }

        @Override
        protected void onPause() {
            super.onPause();
            //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
            mMapView.onPause();
            mClient.stop();

        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_check:
                check();
                break;
            case R.id.btn_next:
                goToNext();
                break;
            default:
                break;
        }
    }

    private void goToNext() {
        if (mEditText.getText().toString()==null||mEditText.getText().length()==0){
            Toast.makeText(BaseMapActivity.this,"搜索内容为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (totalPage==0||!mEditText.getText().toString().equals(mType)){
            Toast.makeText(BaseMapActivity.this,"请先进行搜索",Toast.LENGTH_SHORT).show();
            return;
        }
        String type = mEditText.getText().toString();
        Log.i("搜索",""+mEditText.getText().toString());
        page++;
        if (page>=totalPage){
            Toast.makeText(BaseMapActivity.this,"已经是最后一页",Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.clear();
        nearbySearch(page,type);
    }

    private void check() {

        Log.i("搜索",mEditText.getText().length()+""+mEditText.getText().toString());
        if (mEditText.getText().toString()==null||mEditText.getText().length()==0){
            Toast.makeText(BaseMapActivity.this,"搜索内容为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String type = mEditText.getText().toString();
        mType = mEditText.getText().toString();
        page=0;
        mMap.clear();
        nearbySearch(page,type);
    }

    //    BDLocationListener Mylisetener = new BDLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // map view 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null)
//                return;
//            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
//            mMap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                // 缩放的范围是3-20
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 17f);
//                mMap.animateMapStatus(u);
//
//            }
//        }
//
//    };
        class MyPoiOverlay extends PoiOverlay {

            public MyPoiOverlay(BaiduMap arg0) {
                super(arg0);
            }

            @Override
            public boolean onPoiClick(int arg0) {
                super.onPoiClick(arg0);
                PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
                mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                        .poiUid(poiInfo.uid));
                return true;
            }

        }
    private void nearbySearch(int page,String type) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(mLatitude, mLongitude));
        Log.i("POILocation",""+mLatitude+":nearby"+mLongitude);
        nearbySearchOption.keyword(type);
        nearbySearchOption.radius(1000);// 检索半径，单位是米
        nearbySearchOption.pageNum(page);
        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }

}




