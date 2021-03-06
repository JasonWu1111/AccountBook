package com.rightteam.accountbook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseRvAdapter;
import com.rightteam.accountbook.bean.WalletBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.ui.WalletActivity;
import com.rightteam.accountbook.utils.CommonUtils;
import com.rightteam.accountbook.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/21/2018
 */
public class WalletListAdapter extends BaseRvAdapter<WalletBean> {
    private final static int VIEW_TYPE_WALLET = 0;
    private final static int VIEW_TYPE_ADD = 1;

    @SuppressLint("UseSparseArrays")
    private HashMap<Long, Boolean> map = new HashMap<>();
    private long mCurWalletId = -1;

    public WalletListAdapter(Context context) {
        super(context);

    }

    public void setCurWalletId(long walletId) {
        mCurWalletId = walletId;
    }

    @Override
    public void setData(List<WalletBean> data) {
        for (WalletBean bean : data) {
            map.put(bean.getId(), false);
        }
        map.put(mCurWalletId, true);
        super.setData(data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_WALLET) {
            return new WalletViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_wallet, parent, false));
        } else {
            return new WalletAddViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_wallet_add, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            ((WalletAddViewHolder) holder).bind(position);
        } else {
            ((WalletViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_ADD;
        } else {
            return VIEW_TYPE_WALLET;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    class WalletViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_cover)
        ImageView imageCover;
        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.text_date)
        TextView textDate;
        @BindView(R.id.state_chosen)
        RelativeLayout stateChosen;
        @BindView(R.id.state_change)
        RelativeLayout stateChange;

        private int mPosition;
        private WalletBean mWallet;

        WalletViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            mPosition = position;
            mWallet = getData().get(position);
            imageCover.setImageResource(ResDef.WALLET_COVER[mWallet.getImageId()]);
            textTitle.setText(mWallet.getTitle());
            textDate.setText(CommonUtils.formatTimestamp(mWallet.getStartTime(), CommonUtils.DEFAULT_DAY_PATTERN));
            stateChosen.setVisibility(map.get(mWallet.getId()) ? View.VISIBLE : View.INVISIBLE);
            itemView.setOnClickListener(v -> {
                if (mCurWalletId != mWallet.getId()) {
                    map.put(mCurWalletId, false);
                    map.put(mWallet.getId(), true);
                    mCurWalletId = mWallet.getId();
                    notifyDataSetChanged();
                    SharedPreferencesUtil.getInstance().putLong(KeyDef.CURRENT_WALLET_ID, mCurWalletId);
                    EventBus.getDefault().post(new UpdateBillListEvent(mCurWalletId));
                }
            });
            itemView.setOnLongClickListener(v -> {
                stateChange.setVisibility(View.VISIBLE);
                return true;
            });
        }

        @OnClick({R.id.btn_modify, R.id.btn_delete, R.id.state_change})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.btn_modify:
                    onItemClickListener.onClick(mPosition, KeyDef.ACTION_MODIFY, mWallet.getId());
                    break;
                case R.id.btn_delete:
                    onItemClickListener.onClick(mPosition, KeyDef.ACTION_DELETE, mWallet.getId());
                    break;
                case R.id.state_change:
                    stateChange.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    class WalletAddViewHolder extends RecyclerView.ViewHolder {

        WalletAddViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int position) {
            itemView.setOnClickListener(v -> {
                mContext.startActivity(new Intent(mContext, WalletActivity.class));
            });
        }
    }
}
