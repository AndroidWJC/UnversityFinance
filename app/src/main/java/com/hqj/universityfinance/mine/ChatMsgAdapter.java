package com.hqj.universityfinance.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.MessageData;
import com.hqj.universityfinance.utils.Utils;

import java.util.List;

/**
 * Created by wang on 17-11-26.
 */

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ViewHolder> {

    private List<MessageData> mMsgList;
    private Context mContext;
    private int mCurrentUserId;

    public ChatMsgAdapter(Context context, List<MessageData> list) {
        mContext = context;
        mMsgList = list;
        mCurrentUserId = Integer.valueOf(Utils.getStringFromSharedPreferences(mContext, "account"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.msg_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItem(position).getSend_id() == mCurrentUserId) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.msgBg.setLayoutParams(lp);
            holder.msgBg.setBackgroundResource(R.drawable.message_right);
            holder.msgContent.setText(getItem(position).getMsg_centent());
        } else {
            holder.msgContent.setText(getItem(position).getMsg_centent());
        }
    }

    private MessageData getItem(int position) {
        return mMsgList.get(position);
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout msgBg;
        TextView msgContent;

        public ViewHolder(View view) {
            super(view);
            msgBg = (LinearLayout) view.findViewById(R.id.msg_bg);
            msgContent = (TextView) view.findViewById(R.id.msg_content);
        }
    }
}
