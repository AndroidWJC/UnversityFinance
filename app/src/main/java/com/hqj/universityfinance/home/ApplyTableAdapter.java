package com.hqj.universityfinance.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqj.universityfinance.R;

/**
 * Created by wang on 17-10-17.
 */

public class ApplyTableAdapter extends BaseAdapter {

    private String[] mTitles;
    private Context mContext;

    public ApplyTableAdapter(Context context, String[] title) {
        mContext = context;
        mTitles = title;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int i) {
        return mTitles[i];
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
            view = LayoutInflater.from(mContext).inflate(R.layout.left_right_layout_arrow_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTv.setText((String) getItem(i));

        return view;
    }

     class ViewHolder {
        TextView titleTv;
        TextView contentTv;

        public ViewHolder(View parent) {
            titleTv = (TextView) parent.findViewById(R.id.title);
            contentTv = (TextView) parent.findViewById(R.id.content);
        }
    }
}
