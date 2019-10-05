package com.lwb.framelibrary.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwb.framelibrary.R;
import com.lwb.framelibrary.adapter.loadmore.FooterHolder;
import com.lwb.framelibrary.adapter.loadmore.RootLoadMoreAdapter;

import java.util.List;


public abstract class BaseLoadMoreAdapter<T, H extends RecyclerView.ViewHolder,
        I extends RecyclerView.ViewHolder> extends RootLoadMoreAdapter {

    private static final int TYPE_HEADER = 0;  //顶部HeaderView
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;  //底部FooterView
    private static final int TYPE_OTHER = 3;  //自定义类型

    protected Context mContext;
    private OnItemClickListener mItemClickListener;
    private onLongItemClickListener mLongItemClickListener;
    public List<T> mDatas;

    public BaseLoadMoreAdapter(Context mContext, List<T> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    /**
     * 获取布局
     *
     * @param parent
     * @param resLayout
     * @return
     */
    protected View getItemView(@Nullable int resLayout, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(resLayout, parent, false);
    }

    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mDatas.addAll(data);
        notifyItemRangeInserted(mDatas.size() - 1, data.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_HEADER) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            viewHolder = onCreateFooterViewHolder(parent, viewType);
        } else if (viewType == TYPE_ITEM) {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        } else {
            return onCreateOtherHolder(parent, viewType);
        }
        return viewHolder;
    }

    /**
     * c创建自定义类型布局
     *
     * @param parent
     * @param viewType not TYPE_HEADER、TYPE_ITEM、TYPE_FOOTER
     */
    public RecyclerView.ViewHolder onCreateOtherHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader()) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position + 1 < getItemCount()) {
                //自定义类型
                if (isOtherItem(position - 1)) {
                    return getItemViewOtherType(position);
                } else {
                    return TYPE_ITEM;
                }
            } else {
                return TYPE_FOOTER;
            }
        } else {//无头部
            if (position + 1 < getItemCount()) {
                if (isOtherItem(position)) {
                    return getItemViewOtherType(position);
                } else {
                    return TYPE_ITEM;
                }
            } else {
                return TYPE_FOOTER;
            }
        }
    }

    /**
     * 是否为自定义类型布局，这个子类重写进行判断
     *
     * @param position 有头部的情况下标需要减一处理
     */
    public boolean isOtherItem(int position) {
        return false;
    }

    /**
     * 自定义类型,如果只是一种类型，无需重写。多种需要重写自己判断
     * 但是不能使用已经定义的int值（ 0、1、2）
     *
     * @param position
     * @return not 0、1、2
     */
    public int getItemViewOtherType(int position) {
        return TYPE_OTHER;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (hasHeader()) {//如果整个列表有header
            if (position == 0) {
                onBindHeaderViewHolder((H) holder);
            } else if (position + 1 < getItemCount()) {
                //自定义布局
                if (isOtherItem(position - 1)) {
                    onBindOtherViewHolder(holder, position - 1);
                } else {
                    onBindItemViewHolder((I) holder, position - 1);
                }
                if (mItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onItemClick(position - 1);
                        }
                    });
                }
                if (mLongItemClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            mLongItemClickListener.onLongItemClick(position - 1);
                            return true;
                        }
                    });
                }
            } else {//当前位置是整个列表的footer
                onBindFooterViewHolder(holder);
            }
        } else {//整个列表没有Header
            if (position + 1 < getItemCount()) {
                if (isOtherItem(position)) {
                    onBindOtherViewHolder(holder, position);
                } else {
                    onBindItemViewHolder((I) holder, position);
                }
                if (mItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onItemClick(position);
                        }
                    });
                }
                if (mLongItemClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            mLongItemClickListener.onLongItemClick(position);
                            return true;
                        }
                    });
                }
            } else {//当前位置是整个列表的footer
                onBindFooterViewHolder( holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hasHeader() ? mDatas.size() + 2 : mDatas.size() + 1;
    }

    /**
     * 整个列表是否有Header
     */
    public abstract boolean hasHeader();

    /**
     * 为分组内容创建一个类型为VH的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract I onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * 为整个列表创建一个类型为RH的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract H onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * 为整个列表创建一个类型为FO的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected FooterHolder onCreateFooterViewHolder(ViewGroup parent, int viewType){
        return new FooterHolder(getItemView(R.layout.layout_footer, parent));
    }

    /**
     * 绑定分组数据
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindItemViewHolder(I holder, int position);

    /**
     * 绑定Header数据
     *
     * @param holder
     */
    protected abstract void onBindHeaderViewHolder(H holder);

    /**
     * 绑定自定义数据
     *
     * @param holder
     */
    protected void onBindOtherViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface onLongItemClickListener {
        void onLongItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setonLongItemClickListener(onLongItemClickListener listener) {
        this.mLongItemClickListener = listener;
    }

}
