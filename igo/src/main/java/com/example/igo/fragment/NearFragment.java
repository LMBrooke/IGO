package com.example.igo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.igo.R;
import com.example.igo.view.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class NearFragment extends Fragment implements CustomRefreshListView.OnRefreshListener{
    CustomRefreshListView customRefreshListView;
    private List<String> mList;
    ArrayAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_near, null, false);
        customRefreshListView = (CustomRefreshListView) view.findViewById(R.id.cr_list);
        mList = new ArrayList<>();
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,mList);
        customRefreshListView.setAdapter(mAdapter);
        customRefreshListView.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onPullRefresh() {
        mList.add("4");
        mList.add("5");
        mAdapter.notifyDataSetChanged();
        customRefreshListView.completeRefresh();
    }

    @Override
    public void onLoadingMore() {
        mList.add("6");
        mList.add("7");
        mAdapter.notifyDataSetChanged();


    }
}
