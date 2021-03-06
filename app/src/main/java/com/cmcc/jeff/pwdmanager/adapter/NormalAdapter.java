package com.cmcc.jeff.pwdmanager.adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cmcc.jeff.pwdmanager.MainActivity;
import com.cmcc.jeff.pwdmanager.R;
import com.cmcc.jeff.pwdmanager.UserInfo;
import com.cmcc.jeff.pwdmanager.UserManager;
import com.cmcc.jeff.pwdmanager.finger.FingerprintAuthenticationDialogFragment;
import java.util.Collections;
import java.util.List;

/**
 * Created by jeff on 2017/3/22.
 */

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH> implements IItemTouchHelperAdapter {
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
    public void onBindViewHolder(final VH holder, final int position) {
        holder.mTextView.setText(dataList.get(position).getTag());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                fragment.show(((MainActivity)mContext).getFragmentManager(), "tag");

                Bundle bundle = new Bundle();
                bundle.putString(UserInfo.TAG, dataList.get(holder.getLayoutPosition()).getTag());
                bundle.putString(UserInfo.USERNAME, dataList.get(holder.getLayoutPosition()).getUserName());
                bundle.putString(UserInfo.PASSWORD, dataList.get(holder.getLayoutPosition()).getPassword());
                fragment.setArguments(bundle);
//                DialogUtil.showOKDialog(mContext, dataList.get(holder.getLayoutPosition()).getTag(),
//                        "username ： " + dataList.get(holder.getLayoutPosition()).getUserName() +／
//                        "\npassword ： " + dataList.get(holder.getLayoutPosition()).getPassword(),
//                        "知道了");
            }
        });
    }

    @Override
    public int getItemCount() {
        return  (this.dataList != null) ? this.dataList.size() : 0;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(dataList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //删除制定item
        UserManager.removeUserInfo(mContext, dataList.get(position));
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public static class VH extends RecyclerView.ViewHolder implements IItemTouchHelperViewHolder {

        CardView mCardView;
        TextView mTextView;

        public VH(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#FFFF4081"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
