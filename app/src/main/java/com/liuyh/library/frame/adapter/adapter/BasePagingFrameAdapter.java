package com.liuyh.library.frame.adapter.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liuyh.library.R;

import java.util.List;

/**
 * 二次封装带分页的adapter
 *
 * @author liuyuhang
 */
public abstract class BasePagingFrameAdapter<T> extends BaseFrameAdapter<T> implements AdapterPageChangedListener {
    private final int LOADING_DELAY_TIME = 0;// 每次获取数据，加一个delay延迟再返回给调用者(用于优化用户体验)

    protected final int STATUS_LOADING = 1;// 正在加载
    protected final int STATUS_LOADING_NEED_TAP = STATUS_LOADING + 1;// 点击加载下一页
    protected final int STATUS_LOADING_END = STATUS_LOADING_NEED_TAP + 1;// 加载完毕

    private int mLoadingStatus = STATUS_LOADING;
    private boolean isLoadingNextPage = false;// 当前是否在做下一次请求
    // private int proLoadPosition = 4;// 预加载，距离底部还剩几条数据的时候开始加载，默认是0
    private int startPage = 0;// 初始化页码
    private int page = 0;

    private PagingListener<T> pagingListener;// 分页的listener

    public interface PagingListener<T> {
        /**
         * 分页的监听，返回当前adapter和当前请求的页数(页数从0开始)
         *
         * @param adapter
         * @param page    下一页，page会自动计数
         */
        void onNextPageRequest(BasePagingFrameAdapter<T> adapter, int page);
    }

    public BasePagingFrameAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        // 通过判断pagingListener是否为空来定义有没有开启分页加载的开关，如果是分页加载，count+1，最后一个view为loadingview
        if (super.getCount() != 0 && pagingListener != null) {
            return super.getCount() + 1;
        }
        return super.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getCount() - 1 || pagingListener == null) {
                return BaseAdapterViewType.VIEW_TYPE_CONTENT;
        } else {
            switch (mLoadingStatus) {
                case STATUS_LOADING:
                    return BaseAdapterViewType.VIEW_TYPE_LOADING;
                case STATUS_LOADING_NEED_TAP:
                    return BaseAdapterViewType.VIEW_TYPE_LOADING_TAP_NEXT;
                case STATUS_LOADING_END:
                    return BaseAdapterViewType.VIEW_TYPE_LOADING_COMPLETE;
                default:
                    return BaseAdapterViewType.VIEW_TYPE_LOADING;
            }
        }
    }

    @Override
    public boolean isEnabled(int position) {
        if (getItemViewType(position) != BaseAdapterViewType.VIEW_TYPE_CONTENT) {
            return false;
        }
        return true;
    }

    /**
     * 创建loadingview的样式
     *
     * @param viewType VIEW_TYPE_LOADING, VIEW_TYPE_LOADING_COMPLETE,
     *                 VIEW_TYPE_LOADING_TAP_NEXT
     * @return
     */
    protected View onLoadingViewCrate(int viewType) {
        switch (viewType) {
            case BaseAdapterViewType.VIEW_TYPE_LOADING:
                return getInflater().inflate(R.layout.adapter_loading_layout, null);
            case BaseAdapterViewType.VIEW_TYPE_LOADING_COMPLETE:
                return getInflater().inflate(R.layout.adapter_loading_complate_layout, null);
            case BaseAdapterViewType.VIEW_TYPE_LOADING_TAP_NEXT:
                return getInflater().inflate(R.layout.adapter_loading_tap_next_layout, null);
            default:
                return getInflater().inflate(R.layout.adapter_loading_layout, null);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case BaseAdapterViewType.VIEW_TYPE_CONTENT:
                return super.getView(position, convertView, parent);
            case BaseAdapterViewType.VIEW_TYPE_LOADING:
                if (convertView == null) {
                    convertView = onLoadingViewCrate(type);
                }
                adapterLoadNextPage(page);
                return convertView;
            case BaseAdapterViewType.VIEW_TYPE_LOADING_TAP_NEXT:
                if (convertView == null) {
                    convertView = onLoadingViewCrate(type);
                }
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mLoadingStatus = STATUS_LOADING;
                        notifyDataSetChanged();
                    }
                });
                return convertView;
            case BaseAdapterViewType.VIEW_TYPE_LOADING_COMPLETE:
                if (convertView == null) {
                    if (getData().size() > 5) {// 总数据如果小于5条，就不显示已加载全部的view
                        convertView = onLoadingViewCrate(type);
                    } else {
                        convertView = new LinearLayout(getContext());
                    }
                }
                return convertView;
            default:
                return super.getView(position, convertView, parent);
        }
    }

    /**
     * 本次加载分页请求完毕，可以加载下一次请求
     */
    @Override
    public void mayHaveNextPage() {
        page++;
        isLoadingNextPage = false;
        mLoadingStatus = STATUS_LOADING;
        notifyDataSetChanged();
    }

    /**
     * 点击按钮加载下一页代替之前滑动到底部就自动加载
     */
    public void tapNextPage() {
        isLoadingNextPage = false;
        mLoadingStatus = STATUS_LOADING_NEED_TAP;
        notifyDataSetChanged();
    }

    @Override
    public void noMorePage() {
        isLoadingNextPage = false;
        mLoadingStatus = STATUS_LOADING_END;
        notifyDataSetChanged();
    }

    @Override
    public void resetData(List<T> data) {
        super.resetData(data);
        this.page = startPage;
    }

    @Override
    public void addData(final List<T> data) {
        super.addData(data);
    }

    /**
     * 同步两个adapter的数据和page
     *
     * @param data
     * @param page
     */
    public void syncAdapter(List<T> data, int page) {
        if (data == null) {
            return;
        }
        super.resetData(data);
        this.page = page;// 更新page
        notifyDataSetChanged();
    }

    /**
     * 设置分页监听器
     *
     * @param listener
     */
    public void setOnPagingListener(PagingListener<T> listener) {
        this.pagingListener = listener;
        // page++;
        if (getCount() == 0) {
            adapterLoadNextPage(startPage);
        }
    }

    private synchronized void adapterLoadNextPage(final int page) {
        if (!isLoadingNextPage) {
            this.isLoadingNextPage = true;
            // 添加数据的时候增加300ms延迟是为了解决一个空指针的bug
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    BasePagingFrameAdapter.this.pagingListener.onNextPageRequest(BasePagingFrameAdapter.this, page);
                }

            }, LOADING_DELAY_TIME);
        }

    }

    /**
     * 设置初始加载页面的页码
     *
     * @param startPage
     */
    public void setStartPage(int startPage) {
        this.page = startPage;
        this.startPage = startPage;
    }

    public int getPage() {
        return page;
    }

}
