package com.lwb.framelibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 创建时间：2017/11/02
 * @author ：李伟斌
 * 功能描述:
 * Description
 * 普通的万能Adapter
 * 支持onItemClick
 * 支持onLongItemClick
 * <p/>
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;
    protected int mLayoutId;
    private OnItemClickListener mItemClickListener;
    private onLongItemClickListener mLongItemClickListener;
    //存储监听回调
    private SparseArray<ItemViewClickListener> onClickListeners;

    public BaseRecycleAdapter(Context mContext, List<T> mDatas, int mLayoutId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mLayoutId = mLayoutId;
        onClickListeners=new SparseArray<>();
    }

    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        convert(mContext, holder, mDatas.get(position),position);
        //设置根据控件id点击监听
        for (int i = 0; i < onClickListeners.size(); ++i) {
            int id = onClickListeners.keyAt(i);
            View view = holder.getView(id);
            if (view == null){
                continue;
            }
            final ItemViewClickListener listener = onClickListeners.get(id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClicked(v, position);
                    }
                }
            });
        }
        if (mItemClickListener != null) {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, position);
                }
            });
        }
        if (mLongItemClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongItemClickListener.onLongItemClick(v, position);
                    return true;
                }
            });
        }
    }

    protected abstract void convert(Context mContext, BaseViewHolder holder, T t,int position);

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface onLongItemClickListener {
        void onLongItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setonLongItemClickListener(onLongItemClickListener listener) {
        this.mLongItemClickListener = listener;
    }


    //根据控件id的监听回调接口
    public interface ItemViewClickListener{
        void onItemClicked(View view, int position);
    }

    protected boolean isScrolling = false;

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    /**
     * 存储viewId对应的回调监听实例listener
     * @param viewId
     * @param listener
     */
    public void setOnItemViewClickListener(int viewId, ItemViewClickListener listener) {
        ItemViewClickListener listener_ = onClickListeners.get(viewId);
        if (listener_ == null) {
            onClickListeners.put(viewId, listener);
        }
    }

}
