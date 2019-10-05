package com.lwb.framelibrary.adapter.loadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 创建时间：2017/10/23
 * 作者：李伟斌
 * 功能描述:
 */

public abstract class RootLoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //上拉加载默认状态--默认为-1
    private int load_more_status = -1;
    private RvOnScrollListener mRvOnScrollListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private ScollYDistanceListener mScollYDistanceListener;
    private FooterNoDataClickListener mFooterNoDataClickListener;

    /**
     * 加载更多item更新时，默认更新所有数据。
     * @return
     */
    public boolean changeMoreStatusOnlyOneNotify(){
        return false;
    }

    /**
     * 加载失败
     */
    public void setLoadFail() {
        mRvOnScrollListener.isLoading = false;
        mRvOnScrollListener.loadingEnable = false;
        changeMoreStatus(LoadMoreMode.LOADING_FAIL);
    }

    /**
     * 加载完成
     *
     * @param enable
     */
    public void setLoadMoreFinish(boolean enable) {
        if (mRvOnScrollListener != null) {
            mRvOnScrollListener.isLoading = false;
            mRvOnScrollListener.loadingEnable = enable;
            changeMoreStatus(enable ? LoadMoreMode.PULLUP_LOAD_MORE : LoadMoreMode.LOADING_FINISH);
        }
    }

    /**
     * 重置滑动偏移量
     */
    public void resetScroll(){
        if (mRvOnScrollListener != null) {
            mRvOnScrollListener.resetScroll();
        }
    }

    /**
     * 加载更多监听
     *
     * @param recyclerView
     * @param listener
     */
    public void setOnLoadMoreListener(RecyclerView recyclerView, final OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
        recyclerView.addOnScrollListener(mRvOnScrollListener = new RvOnScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore() {
                changeMoreStatus(LoadMoreMode.LOADING_MORE);
                mOnLoadMoreListener.onLoadMore();
            }

            @Override
            public void onScrolled(int dx,int dy) {
                if (mScollYDistanceListener != null) {
                    mScollYDistanceListener.onScrolledY(dy);
                }
            }
        });
    }

    /**
     * 垂直偏移量监听 must setOnLoadMoreListener();
     *
     * @param mScollYDistanceListener
     */
    public void setScollYDistanceListener(ScollYDistanceListener mScollYDistanceListener) {
        this.mScollYDistanceListener = mScollYDistanceListener;
    }

    /**
     * 尾部无更多数据时布局点击事件监听
     *
     * @param mFooterNoDataClickListener
     */
    public void setFooterNoDataClickListener(FooterNoDataClickListener mFooterNoDataClickListener) {
        this.mFooterNoDataClickListener = mFooterNoDataClickListener;
    }

    /**
     * 绑定上拉加载footer（整个RecycerView的footer）数据
     *
     * @param holder
     */
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.itemView.setEnabled(false);
            footerHolder.setDefaultTextColor();
            switch (load_more_status) {
                case LoadMoreMode.PULLUP_LOAD_MORE:
                    footerHolder.tvFooter.setVisibility(View.VISIBLE);
                    footerHolder.progressBar.setVisibility(View.GONE);
                    footerHolder.tvFooter.setText("上拉加载更多...");
                    break;
                case LoadMoreMode.LOADING_MORE:
                    footerHolder.tvFooter.setVisibility(View.VISIBLE);
                    footerHolder.progressBar.setVisibility(View.VISIBLE);
                    footerHolder.tvFooter.setText("正在加载数据...");
                    break;
                case LoadMoreMode.LOADING_FINISH:
                    footerHolder.tvFooter.setVisibility(View.VISIBLE);
                    footerHolder.progressBar.setVisibility(View.GONE);
                    footerHolder.tvFooter.setText("没有更多数据");
                    //这里无更多数据可以自定义点击事件处理
                    if (mFooterNoDataClickListener != null) {
                        //可点击
                        footerHolder.itemView.setEnabled(true);
                        footerHolder.tvFooter.setText(mFooterNoDataClickListener.getFooterContent());
                        footerHolder.tvFooter.setTextColor(mFooterNoDataClickListener.getFooterTextColor());
                        footerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFooterNoDataClickListener.onFooterClick();
                            }
                        });
                    }
                    break;
                case LoadMoreMode.LOADING_FAIL:
                    footerHolder.itemView.setEnabled(true);
                    footerHolder.progressBar.setVisibility(View.GONE);
                    footerHolder.tvFooter.setVisibility(View.VISIBLE);
                    footerHolder.tvFooter.setText("加载失败,点击重新加载");
                    footerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeMoreStatus(LoadMoreMode.LOADING_MORE);
                            mOnLoadMoreListener.onClickLoadMore();
                        }
                    });
                    break;
                default:
                    footerHolder.tvFooter.setVisibility(View.GONE);
                    footerHolder.progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        if(!changeMoreStatusOnlyOneNotify())
            notifyDataSetChanged();
        else {
            //只更新最后一个
            notifyItemChanged(getItemCount() - 1);
        }
    }

}
