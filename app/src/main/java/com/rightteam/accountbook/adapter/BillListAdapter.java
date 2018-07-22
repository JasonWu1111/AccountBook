package com.rightteam.accountbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseRvAdapter;
import com.rightteam.accountbook.bean.BillBean;

/**
 * Created by JasonWu on 7/21/2018
 */
public class BillListAdapter extends BaseRvAdapter<BillBean> {
    public final static int VIEW_TYPE_DATE = 0;
    public final static int VIEW_TYPE_BILL = 1;

    public BillListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BillListViewHolder)holder).bind(position);
    }

    class BillListViewHolder extends RecyclerView.ViewHolder{

        BillListViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int position){

        }
    }
}
