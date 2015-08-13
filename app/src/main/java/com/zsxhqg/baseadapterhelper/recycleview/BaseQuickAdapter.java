package com.zsxhqg.baseadapterhelper.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author LinZheng
 */
public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<ViewHolderHelper> {

    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<T> mData;

    public BaseQuickAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolderHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        return new ViewHolderHelper(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderHelper holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        onBindView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public abstract int getLayoutId();

    public abstract void onBindView(ViewHolderHelper holder, int position);

}
