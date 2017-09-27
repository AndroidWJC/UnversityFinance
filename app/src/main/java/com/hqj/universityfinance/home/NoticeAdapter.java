package com.hqj.universityfinance.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.R;

import java.util.List;

/**
 * Created by wang on 17-9-22.
 */

public class NoticeAdapter extends BaseAdapter{

    private List<BannerBean> mList;
    private Context mContext;

    public NoticeAdapter(List<BannerBean> data, Context context) {
        mList = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BannerBean getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.notice_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(getItem(i).getTitle());

        return view;
    }

    class ViewHolder {

        TextView textView;

        ViewHolder(View parent) {
            textView = (TextView) parent.findViewById(R.id.notice_title);
        }
    }
}

