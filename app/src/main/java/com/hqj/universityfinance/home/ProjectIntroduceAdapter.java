package com.hqj.universityfinance.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqj.universityfinance.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wang on 17-10-12.
 */

public class ProjectIntroduceAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, String>> mDataList;

    public ProjectIntroduceAdapter(Context context, List<Map<String, String>> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Map<String, String> getItem(int i) {
        return mDataList.get(i);
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
            if (i + 1 == getCount()) {
                view = LayoutInflater.from(mContext).inflate(R.layout.up_down_layout_item, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.left_right_layout_item, parent, false);
            }

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTv.setText(getItem(i).get("title"));
        viewHolder.contentTv.setText(getItem(i).get("content"));

        return view;
    }

    static class ViewHolder {
        TextView titleTv;
        TextView contentTv;

        public ViewHolder(View parent) {
            titleTv = (TextView) parent.findViewById(R.id.title);
            contentTv = (TextView) parent.findViewById(R.id.content);
        }
    }
}
