package com.example.igo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.igo.R;
import com.example.igo.adapter.FirstAdapter;
import com.example.igo.activity.MainActivity;
import com.example.igo.activity.ShopDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class FirstFragment extends Fragment implements AdapterView.OnItemClickListener {

    MainActivity mActivity;
    ListView mListView;
    FirstAdapter mAdapter;
    private List<PoiInfo> mList;

    public FirstFragment() {

        Log.i("Fragment","wuxiao");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Fragment","wuxiao1");
        mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_shop);
        mList = new ArrayList<>();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mList = mActivity.getmList();
        mAdapter = new FirstAdapter(mActivity,mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){
            return;
        }
        PoiInfo poiInfo = mList.get(position-1);
        Intent intent= new Intent(mActivity, ShopDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelable("poiInfo",poiInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
