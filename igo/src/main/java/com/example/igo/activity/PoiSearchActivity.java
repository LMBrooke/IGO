package com.example.igo.activity;


import com.baidu.mapapi.search.core.PoiInfo;
import com.example.igo.R;
import com.example.igo.service.LocationSvc;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * POI检索 1.周边检索 2. 范围检索 3. 城市检索 4.详细检索
 *
 * @author ys
 */
public class PoiSearchActivity extends Activity  {


    TextView tv_time;
//    private PoiSearch poiSearch;
//
//    // 城市检索，区域检索，周边检索，下一组数据 按钮
//    //private Button citySearchBtn, boundSearchBtn, nearbySearchBtn, nextDataBtn;
//
//    // 记录检索类型
//    private int type;
//    // 记录页标
//    private int page = 0;
//    private int totalPage = 0;
    int time = 3;
//
//    private double mLatitude ;
//    private double mLongitude ;
    private List<PoiInfo> poiResultDetail = new ArrayList<>();

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(PoiSearchActivity.this,MainActivity.class);
                    intent.putExtra("list",(Serializable) poiResultDetail);
                    if (poiResultDetail!=null&&poiResultDetail.size()!=0){
                        Log.i("list",""+poiResultDetail.size());
                    }
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    tv_time.setText((time--)+"");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        setListener();
    }

    private void setListener() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i= 3;i>=0;i--){


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(2);
                    if (i<1){
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }
        }.start();
    }

    private void init() {

        tv_time = (TextView) findViewById(R.id.tv_time);

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("locationAction");
        this.registerReceiver(new LocationBroadcastReceiver(), filter);

        // 启动服务
        Intent intent = new Intent();
        intent.setClass(this, LocationSvc.class);
        startService(intent);
    }


    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("POILocation","lai guangbo");
            if (!intent.getAction().equals("locationAction")) return;
            poiResultDetail = (List<PoiInfo>) intent.getSerializableExtra("list");
//            double latitude = intent.getDoubleExtra("latitude",0);
//            double longitude = intent.getDoubleExtra("longitude",0);
//            mLatitude = latitude;
//            mLongitude = longitude;
//            // 实例化PoiSearch对象
//            poiSearch = PoiSearch.newInstance();
//            // 设置检索监听器
//            poiSearch.setOnGetPoiSearchResultListener(poiSearchListener);
//            Log.i("POILocation","先搜索");
//            nearbySearch(page,"美食");
            context.unregisterReceiver(this);
//            PoiSearchActivity.this.unregisterReceiver(this);// 不需要时注销
        }
    }


//    /**
//     *  poi搜索接口
//     */
//    OnGetPoiSearchResultListener poiSearchListener = new OnGetPoiSearchResultListener() {
//
//        @Override
//        public void onGetPoiResult(PoiResult poiResult) {
//            if (poiResult == null
//                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
//                Toast.makeText(PoiSearchActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            Log.i("POILocation","先进行结果");
//            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//
//                totalPage = poiResult.getTotalPageNum();// 获取总分页数
//                Toast.makeText(PoiSearchActivity.this,
//                        "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为" + totalPage + "页",
//                        Toast.LENGTH_SHORT).show();
//                poiResultDetail = poiResult.getAllPoi();
//
//                Log.i("POILocation","多少个？"+poiResult.getAllPoi().size()+"多少个POI"+poiResult.getTotalPoiNum()+"名字"+poiResultDetail.get(0).name);
////                for (PoiInfo poiInfo : poiResultDetail) {
////                    string = "" + string + poiInfo.name + ":" + poiInfo.address + "\n";
////                }
//                Log.i("POILocation",""+poiResultDetail.get(0).uid);
//                //--------------------------------------------------------------------
//
//
//            }
//        }
//
//        @Override
//        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//            if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                Toast.makeText(PoiSearchActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
//            } else {// 正常返回结果的时候，此处可以获得很多相关信息
//                Toast.makeText(PoiSearchActivity.this,
//                        poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
//                        Toast.LENGTH_LONG).show();
//                Log.i("POILocation",poiDetailResult.getDetailUrl());
//            }
//        }
//
//        @Override
//        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//
//        }
//    };
//
//    /**
//     * 城市内搜索
//     */
//    private void citySearch(int page,String type) {
//        // 设置检索参数
//        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
//        citySearchOption.city("");// 城市
//        citySearchOption.keyword(type);// 关键字
//        citySearchOption.pageCapacity(15);// 默认每页10条
//        citySearchOption.pageNum(page);// 分页编号
//        // 发起检索请求
//        poiSearch.searchInCity(citySearchOption);
//    }
//
//    /**
//     * 范围检索
//     */
//    private void boundSearch(int page,String type) {
//        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
//        LatLng southwest = new LatLng(mLatitude - 0.01, mLongitude - 0.012);// 西南
//        LatLng northeast = new LatLng(mLatitude + 0.01, mLongitude + 0.012);// 东北
//        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest).include(northeast).build();// 得到一个地理范围对象
//        boundSearchOption.bound(bounds);// 设置poi检索范围
//        boundSearchOption.keyword(type);// 检索关键字
//        boundSearchOption.pageNum(page);
//        poiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
//    }
//
//    /**
//     * 附近检索
//     */
//    private void nearbySearch(int page,String type) {
//        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
//        nearbySearchOption.location(new LatLng(mLatitude, mLongitude));
//        nearbySearchOption.keyword(type);
//        nearbySearchOption.radius(1000);// 检索半径，单位是米
//        nearbySearchOption.pageNum(page);
//        poiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.city_search_btn:
//                type = 0;
//                page = 1;
//                citySearchBtn.setEnabled(false);
//                boundSearchBtn.setEnabled(true);
//                nearbySearchBtn.setEnabled(true);
//                nextDataBtn.setEnabled(true);
//                bdMap.clear();
//                citySearch(page);
//                break;
//            case R.id.bound_search_btn:
//                type = 1;
//                page = 1;
//                citySearchBtn.setEnabled(true);
//                boundSearchBtn.setEnabled(false);
//                nearbySearchBtn.setEnabled(true);
//                nextDataBtn.setEnabled(true);
//                bdMap.clear();
//                boundSearch(page);
//                break;
//            case R.id.nearby_search_btn:
//                type = 2;
//                page = 1;
//                citySearchBtn.setEnabled(true);
//                boundSearchBtn.setEnabled(true);
//                nearbySearchBtn.setEnabled(false);
//                nextDataBtn.setEnabled(true);
//                bdMap.clear();
//                nearbySearch(page);
//                break;
//            case R.id.next_data_btn:
//                switch (type) {
//                    case 0:
//                        if (++page <= totalPage) {
//                            citySearch(page);
//                        } else {
//                            Toast.makeText(PoiSearchActivity.this, "已经查到了最后一页~",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    case 1:
//                        if (++page <= totalPage) {
//                            boundSearch(page);
//                        } else {
//                            Toast.makeText(PoiSearchActivity.this, "已经查到了最后一页~",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                    case 2:
//                        if (++page <= totalPage) {
//                            nearbySearch(page);
//                        } else {
//                            Toast.makeText(PoiSearchActivity.this, "已经查到了最后一页~",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (poiSearch != null)
//        poiSearch.destroy();// 释放poi检索对象
    }

}
