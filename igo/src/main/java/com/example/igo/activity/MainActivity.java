package com.example.igo.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.igo.R;
import com.example.igo.fragment.FirstFragment;
import com.example.igo.fragment.NearFragment;
import com.example.igo.fragment.OtherFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    TextView tv_first;
    TextView tv_near;
    TextView tv_other;
    ImageView iv_check;
    EditText et_check;
    NavigationView mNavigationView;

    private FragmentManager fragmentManager;
    private FirstFragment firstFragment;
    private NearFragment nearFragment;
    private OtherFragment otherFragment;
    private List<PoiInfo> mList;

    public List<PoiInfo> getmList() {
        return mList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setView();
        initView();
        setListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, BaseMapActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this,PoiSearchActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        selectedShow();
        switch (v.getId()){
            case R.id.tv_first:
                selectedFirst();
                break;
            case R.id.tv_near:
                selectedNear();
                break;
            case R.id.tv_other:
                selectedOther();
                break;
            case R.id.iv_check:
                //跳转
                break;
            case R.id.et_check:
                //跳转
                break;
            default:
                break;
        }
    }

    private void init() {
        fragmentManager = getFragmentManager();
        Intent intent = getIntent();
        mList= (List<PoiInfo>) intent.getSerializableExtra("list");
    }
    private void setView() {
        setContentView(R.layout.activity_main);
    }
    private void setListener() {
        mNavigationView.setNavigationItemSelectedListener(this);
        tv_first.setOnClickListener(this);
        tv_near.setOnClickListener(this);
        tv_other.setOnClickListener(this);
        iv_check.setOnClickListener(this);
        et_check.setOnClickListener(this);

    }

    private void initView() {
        //title设置
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(Color.parseColor("#FF17AF30"));
        setSupportActionBar(mToolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //侧边栏的设置
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        iv_check = (ImageView) mToolbar.findViewById(R.id.iv_check);
        et_check = (EditText) mToolbar.findViewById(R.id.et_check);
        tv_first = (TextView) findViewById(R.id.tv_first);
        tv_near = (TextView) findViewById(R.id.tv_near);
        tv_other = (TextView) findViewById(R.id.tv_other);

        selectedFirst();
    }

    private void selectedShow(){
        tv_first.setSelected(false);
        tv_near.setSelected(false);
        tv_other.setSelected(false);
    }
    private void selectedFirst() {
        tv_first.setSelected(true);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(firstFragment == null){
            firstFragment = new FirstFragment();
            transaction.add(R.id.fragment_layout, firstFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(firstFragment);
        //提交事务
        transaction.commit();

    }
    private void selectedNear() {
        tv_near.setSelected(true);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(nearFragment == null){
            nearFragment = new NearFragment();
            transaction.add(R.id.fragment_layout, nearFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(nearFragment);
        //提交事务
        transaction.commit();
    }
    private void selectedOther() {
        tv_other.setSelected(true);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(otherFragment == null){
            otherFragment = new OtherFragment();
            transaction.add(R.id.fragment_layout, otherFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(otherFragment);
        //提交事务
        transaction.commit();
    }
    private void hideFragment(FragmentTransaction transaction){
        if(firstFragment != null){
            transaction.hide(firstFragment);
        }
        if(nearFragment != null){
            transaction.hide(nearFragment);
        }
        if(otherFragment != null){
            transaction.hide(otherFragment);
        }
    }


}
