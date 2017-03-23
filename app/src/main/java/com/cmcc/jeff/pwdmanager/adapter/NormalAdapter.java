package com.cmcc.jeff.pwdmanager.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmcc.jeff.pwdmanager.R;
import com.cmcc.jeff.pwdmanager.UserInfo;

import java.util.List;

/**
 * Created by jeff on 2017/3/22.
 */

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH> {
    private List<UserInfo> dataList;
    private Context mContext;

    public NormalAdapter(Context context, List<UserInfo> dataList) {
        this.dataList = dataList;
        mContext = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(mContext).inflate(R.layout.item_content, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.mTextView.setText(dataList.get(position).getKey());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "item " + position + " card clicked."
                        + "\nusername : " + dataList.get(position).getUserName()
                        + "\npassword : " + dataList.get(position).getPassword()
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return  (this.dataList != null) ? this.dataList.size() : 0;
    }

    public static class VH extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTextView;

        public VH(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
