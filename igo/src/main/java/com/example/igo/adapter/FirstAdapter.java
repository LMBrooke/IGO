package com.example.igo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.igo.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class FirstAdapter extends BaseAdapter implements View.OnClickListener {
    List<PoiInfo> mList;
    Context mContext;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private LayoutInflater inflater;

    public FirstAdapter(Context mContext, List<PoiInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p == 0)
            return TYPE_1;
        else
            return TYPE_2;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(position);
        inflater = LayoutInflater.from(mContext);
        //按当前所需的样式，确定new的布局
        if (type==TYPE_1){
            convertView = inflater.inflate(R.layout.fragment_first_item_1, null, false);
            holder1 = new ViewHolder1();
            holder1.tv_food = (TextView) convertView.findViewById(R.id.tv_food);
            holder1.tv_hotel = (TextView) convertView.findViewById(R.id.tv_hotel);
            holder1.tv_enjoy = (TextView) convertView.findViewById(R.id.tv_enjoy);

        }else if (type==TYPE_2&&convertView==null){
            convertView = inflater.inflate(R.layout.fragment_first_item_2, parent, false);
            holder2 = new ViewHolder2();
            holder2.imageView = (ImageView) convertView.findViewById(R.id.iv_shop);
            holder2.tv_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder2.tv_address = (TextView) convertView.findViewById(R.id.tv_shop_address);
            convertView.setTag(holder2);
        }else if (type ==TYPE_2&&convertView!=null){
            holder2 = (ViewHolder2) convertView.getTag();
        }

        switch (type) {
            case TYPE_1:
                holder1.tv_food.setOnClickListener(this);
                holder1.tv_hotel.setOnClickListener(this);
                holder1.tv_enjoy.setOnClickListener(this);
                break;
            case TYPE_2:
                PoiInfo poiInfo = mList.get(position-1);
                holder2.imageView.setImageResource(R.mipmap.takeout_img_logo_new);
                holder2.tv_name.setText(poiInfo.name);
                holder2.tv_address.setText(poiInfo.address);
                break;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_food:
                //搜索
                Toast.makeText(mContext,"FOOD",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_hotel:
                Toast.makeText(mContext,"HOTEL",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_enjoy:
                Toast.makeText(mContext,"PLAY",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private class ViewHolder1 {
        TextView tv_food;
        TextView tv_hotel;
        TextView tv_enjoy;

    }

    private class ViewHolder2 {
        ImageView imageView;
        TextView tv_name;
        TextView tv_address;
    }
}
