package com.example.igo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.igo.R;

/**
 * Created by Administrator on 2016/10/27.
 */
public class ShopDetailActivity extends Activity implements View.OnClickListener {

    TextView tv_back,tv_exit,tv_text;
    TextView tv_address,tv_phone;
    ImageView iv_shop;
    PoiInfo mPoiInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        init();
        initView();
        setListener();
    }

    private void setListener() {
        tv_back.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
    }

    private void init() {
        Intent intent = getIntent();
        mPoiInfo = (PoiInfo) intent.getParcelableExtra("poiInfo");
        Log.i("DetailActivity",""+(mPoiInfo==null));
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.title_back);
        tv_exit = (TextView) findViewById(R.id.title_exit);
        tv_text = (TextView) findViewById(R.id.title_text);
        tv_address = (TextView) findViewById(R.id.tv_detail_address);
        tv_phone = (TextView) findViewById(R.id.tv_detail_phone);
        iv_shop = (ImageView) findViewById(R.id.iv_shop);
        tv_exit.setVisibility(View.INVISIBLE);
        tv_text.setText(mPoiInfo.name);
        tv_address.setText(mPoiInfo.address);
        tv_phone.setText(mPoiInfo.phoneNum);
    }

    private void setView() {
        setContentView(R.layout.activity_shop);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.tv_detail_address:
                //进入导航
                break;
            case R.id.tv_detail_phone:
                //打电话，调用系统功能
                callPhone();
                break;
            default:
                break;
        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+tv_phone.getText().toString().trim()));
        ShopDetailActivity.this.startActivity(intent);//开始意图(及拨打电话)
    }
}
