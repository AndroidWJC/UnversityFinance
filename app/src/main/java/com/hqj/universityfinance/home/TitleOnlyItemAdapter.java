package com.hqj.universityfinance.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqj.universityfinance.BannerBean;
import com.hqj.universityfinance.ProjectBean;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.NoticeData;

import java.util.List;

/**
 * Created by wang on 17-9-22.
 */

public class TitleOnlyItemAdapter extends BaseAdapter{

    private List<?> mList;
    private Context mContext;

    public TitleOnlyItemAdapter(List<?> data, Context context) {
        mList = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

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

        if (getItem(i) instanceof BannerBean) {
            viewHolder.textView.setText(((BannerBean) getItem(i)).getTitle());
        } else if (getItem(i) instanceof ProjectBean) {
            viewHolder.textView.setText(((ProjectBean)getItem(i)).getProjectName());
        } else if (getItem(i) instanceof NoticeData) {
            viewHolder.textView.setText(((NoticeData) getItem(i)).getTitle());
            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.setData(Uri.parse(((NoticeData) getItem(i)).getNotice_url()));
                    mContext.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder {

        TextView textView;

        ViewHolder(View parent) {
            textView = (TextView) parent.findViewById(R.id.notice_title);
        }
    }
}

