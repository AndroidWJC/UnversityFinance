package com.hqj.universityfinance.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.ChatPeopleData;
import com.hqj.universityfinance.javabean.MessageData;
import com.hqj.universityfinance.utils.Utils;

import java.util.List;

/**
 * Created by wang on 17-11-26.
 */

public class ChatPeopleListAdapter extends RecyclerView.Adapter<ChatPeopleListAdapter.ViewHolder> {

    private List<ChatPeopleData> mList;
    private Context mContext;
    private int mCurrentUserId;

    public ChatPeopleListAdapter(Context context, List<ChatPeopleData> list) {
        mContext = context;
        mList = list;
        mCurrentUserId = Integer.valueOf(Utils.getStringFromSharedPreferences(mContext, "account"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.peopleNameTv.setText(mList.get(position).getName());
        holder.summaryTv.setText(mList.get(position).getSummary());
        holder.timeTv.setText(mList.get(position).getTime());
        Glide.with(mContext).load(mList.get(position).getPhoto_url()).into(holder.photoIv);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout container;
        ImageView photoIv;
        TextView peopleNameTv;
        TextView summaryTv;
        TextView timeTv;

        public ViewHolder(View view) {
            super(view);
            container = (RelativeLayout) view.findViewById(R.id.container);
            photoIv= (ImageView) view.findViewById(R.id.icon);
            peopleNameTv = (TextView) view.findViewById(R.id.chat_target);
            summaryTv = (TextView) view.findViewById(R.id.chat_summary);
            timeTv = (TextView) view.findViewById(R.id.time);
        }
    }
}
