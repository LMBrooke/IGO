package com.example.igo.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class LocationSvc extends Service implements LocationListener {
    private static final String TAG = "LocationSvc";
    private LocationManager locationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean stopService(Intent name) {
        if (poiSearch != null)
            poiSearch.destroy();// 释放poi检索对象
        return super.stopService(name);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Get the current position \n" + location);

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        // 实例化PoiSearch对象
        poiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        poiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
        Log.i("POILocation","先搜索");
        nearbySearch(page,"美食");

        // 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
        stopSelf();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    private PoiSearch poiSearch;

    // 城市检索，区域检索，周边检索，下一组数据 按钮
    //private Button citySearchBtn, boundSearchBtn, nearbySearchBtn, nextDataBtn;

    // 记录检索类型
    private int type;
    // 记录页标
    private int page = 0;
    private int totalPage = 0;
    int time = 3;

    private double mLatitude ;
    private double mLongitude ;
    private List<PoiInfo> poiResultDetail = new ArrayList<>();

    /**
     *  poi搜索接口
     */
    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(LocationSvc.this, "未找到结果", Toast.LENGTH_LONG).show();
                return;
            }

            Log.i("POILocation","先进行结果");
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回

                totalPage = poiResult.getTotalPageNum();// 获取总分页数
                Toast.makeText(LocationSvc.this,
                        "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为" + totalPage + "页",
                        Toast.LENGTH_SHORT).show();
                poiResultDetail = poiResult.getAllPoi();

                Log.i("POILocation","多少个？"+poiResult.getAllPoi().size()+"多少个POI"+poiResult.getTotalPoiNum()+"名字"+poiResultDetail.get(0).name);
//                for (PoiInfo poiInfo : poiResultDetail) {
//                    string = "" + string + poiInfo.name + ":" + poiInfo.address + "\n";
//                }
                Log.i("POILocation",""+poiResultDetail.get(0).uid);
                //--------------------------------------------------------------------
//通知Activity
                Intent intent = new Intent();
                intent.setAction("locationAction");
                intent.putExtra("list",(Serializable) poiResultDetail);
                sendBroadcast(intent);

            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(LocationSvc.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            } else {// 正常返回结果的时候，此处可以获得很多相关信息
                Toast.makeText(LocationSvc.this,
                        poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
                        Toast.LENGTH_LONG).show();
                Log.i("POILocation",poiDetailResult.getDetailUrl());
            }
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    /**
     * 城市内搜索
     */
    private void citySearch(int page,String type) {
        // 设置检索参数
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city("");// 城市
        citySearchOption.keyword(type);// 关键字
        citySearchOption.pageCapacity(15);// 默认每页10条
        citySearchOption.pageNum(page);// 分页编号
        // 发起检索请求
        poiSearch.searchInCity(citySearchOption);
    }

    /**
     * 范围检索
     */
    private void boundSearch(int page,String type) {
        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
        LatLng southwest = new LatLng(mLatitude - 0.01, mLongitude - 0.012);// 西南
        LatLng northeast = new LatLng(mLatitude + 0.01, mLongitude + 0.012);// 东北
        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest).include(northeast).build();// 得到一个地理范围对象
        boundSearchOption.bound(bounds);// 设置poi检索范围
        boundSearchOption.keyword(type);// 检索关键字
        boundSearchOption.pageNum(page);
        poiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
    }

    /**
     * 附近检索
     */
    private void nearbySearch(int page,String type) {
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(mLatitude, mLongitude));
        nearbySearchOption.keyword(type);
        nearbySearchOption.radius(1000);// 检索半径，单位是米
        nearbySearchOption.pageNum(page);
        poiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }

}
