package com.hqj.universityfinance.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqj.universityfinance.R;
import com.hqj.universityfinance.javabean.MyApplyBean;

import java.util.List;

/**
 * Created by wang on 17-10-11.
 */

public class MyApplyAdapter extends RecyclerView.Adapter<MyApplyAdapter.ViewHolder> {

    private List<MyApplyBean> mApplyList;
    private Context mContext;

    private final static int STATUS_NOT_VERIFY = 0;
    private final static int STATUS_PASS = 1;
    private final static int STATUS_REFUSE = 2;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView projectTv;
        TextView statusTv;
        TextView timeTv;
        LinearLayout container;

        ViewHolder(View view) {
            super(view);
            projectTv = (TextView) view.findViewById(R.id.project_apply);
            statusTv = (TextView) view.findViewById(R.id.status_apply);
            timeTv = (TextView) view.findViewById(R.id.time_apply);
            container = (LinearLayout) view.findViewById(R.id.container);
        }
    }

    public MyApplyAdapter(Context context, List<MyApplyBean> data) {
        mContext = context;
        mApplyList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_apply, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(mContext, ApplyPreviewActivity.class);
                intent.putExtra("applyBean", mApplyList.get(position));
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String status = null;
        if (mApplyList.get(position).getVerifyResult() == STATUS_NOT_VERIFY) {
            holder.statusTv.setText(R.string.status_not_verify);
        } else if (mApplyList.get(position).getVerifyResult() == STATUS_PASS) {
            holder.statusTv.setText(R.string.status_pass);
            holder.statusTv.setTextColor(Color.RED);
        } else if (mApplyList.get(position).getVerifyResult() == STATUS_REFUSE) {
            holder.statusTv.setText(R.string.status_refuse);
            holder.statusTv.setTextColor(Color.RED);
        }

        holder.timeTv.setText(mApplyList.get(position).getTime());
        holder.projectTv.setText(mApplyList.get(position).getProjectId());
    }

    @Override
    public int getItemCount() {
        return mApplyList.size();
    }
}
