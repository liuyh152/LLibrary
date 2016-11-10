//package com.liuyh.library.frame.adapter.adapter.paging;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import com.ichsy.core_library.R;
//import com.ichsy.libs.core.frame.adapter.AdapterPageChangedListener;
//import com.ichsy.libs.core.frame.adapter.BaseAdapterViewType;
//
///**
// * Created by liuyuhang on 16/5/16.
// */
//public abstract class AdapterPagingHelper<T, AD> implements AdapterPageChangedListener {
//    private final int LOADING_DELAY_TIME = 0;// 每次获取数据，加一个delay延迟再返回给调用者(用于优化用户体验)
//
//    protected final int STATUS_LOADING = 1;// 正在加载
//    protected final int STATUS_LOADING_NEED_TAP = STATUS_LOADING + 1;// 点击加载下一页
//    protected final int STATUS_LOADING_END = STATUS_LOADING_NEED_TAP + 1;// 加载完毕
//
//    private int mLoadingStatus = STATUS_LOADING;
//    private boolean isLoadingNextPage = false;// 当前是否在做下一次请求
//    // private int proLoadPosition = 4;// 预加载，距离底部还剩几条数据的时候开始加载，默认是0
//    private int startPage = 0;// 初始化页码
//    private int page = 0;
//
//    private AD mAdapter;
//    private PagingListener<AD> pagingListener;// 分页的listener
//
//    private LayoutInflater mInflater;
//    private Context mContext;
//
//
//    public interface PagingListener<AD> {
//        /**
//         * 分页的监听，返回当前adapter和当前请求的页数(页数从0开始)
//         *
//         * @param adapter
//         * @param page    下一页，page会自动计数
//         */
//        void onNextPageRequest(AD adapter, int page);
//    }
//
//    abstract void onAdapterNotifyDataSetChanged();
//    abstract int getAdapterCount();
//
//    public AdapterPagingHelper(Context context, AD adapter) {
//        this.mContext = context;
//        this.mAdapter = adapter;
//        this.mInflater = LayoutInflater.from(mContext);
//    }
//
//
//    public int getViewTypeCount() {
//        return 4;
//    }
//
//    public int getItemViewType(int adapterCount, int position) {
//        if (position < adapterCount - 1 || pagingListener == null) {
//            return BaseAdapterViewType.VIEW_TYPE_CONTENT;
//        } else {
//            switch (mLoadingStatus) {
//                case STATUS_LOADING:
//                    return BaseAdapterViewType.VIEW_TYPE_LOADING;
//                case STATUS_LOADING_NEED_TAP:
//                    return BaseAdapterViewType.VIEW_TYPE_LOADING_TAP_NEXT;
//                case STATUS_LOADING_END:
//                    return BaseAdapterViewType.VIEW_TYPE_LOADING_COMPLETE;
//                default:
//                    return BaseAdapterViewType.VIEW_TYPE_LOADING;
//            }
//        }
//    }
//
////    public View drawView(View convertView, int position) {
////        int type = getItemViewType(getAdapterCount(), position);
////        switch (type) {
////            case BaseAdapterViewType.VIEW_TYPE_CONTENT:
////                return super.getView(position, convertView, parent);
////            case BaseAdapterViewType.VIEW_TYPE_LOADING:
////                if (convertView == null) {
////                    convertView = onLoadingViewCrate(type);
////                }
////                adapterLoadNextPage(page);
////                return convertView;
////            case BaseAdapterViewType.VIEW_TYPE_LOADING_TAP_NEXT:
////                if (convertView == null) {
////                    convertView = onLoadingViewCrate(type);
////                }
////                convertView.setOnClickListener(new View.OnClickListener() {
////
////                    @Override
////                    public void onClick(View v) {
////                        mLoadingStatus = STATUS_LOADING;
////                        onAdapterNotifyDataSetChanged();
////                    }
////                });
////                return convertView;
////            case BaseAdapterViewType.VIEW_TYPE_LOADING_COMPLETE:
////                if (convertView == null) {
////                    if (getAdapterCount() > 5) {// 总数据如果小于5条，就不显示已加载全部的view
////                        convertView = onLoadingViewCrate(type);
////                    } else {
////                        convertView = new LinearLayout(mContext);
////                    }
////                }
////                return convertView;
////            default:
////                return super.getView(position, convertView, parent);
////        }
////    }
//
//    /**
//     * 创建loadingview的样式
//     *
//     * @param viewType VIEW_TYPE_LOADING, VIEW_TYPE_LOADING_COMPLETE,
//     *                 VIEW_TYPE_LOADING_TAP_NEXT
//     * @return
//     */
//    protected View onLoadingViewCrate(int viewType) {
//        switch (viewType) {
//            case BaseAdapterViewType.VIEW_TYPE_LOADING:
//                return mInflater.inflate(R.layout.adapter_loading_layout, null);
//            case BaseAdapterViewType.VIEW_TYPE_LOADING_COMPLETE:
//                return mInflater.inflate(R.layout.adapter_loading_complate_layout, null);
//            case BaseAdapterViewType.VIEW_TYPE_LOADING_TAP_NEXT:
//                return mInflater.inflate(R.layout.adapter_loading_tap_next_layout, null);
//            default:
//                return mInflater.inflate(R.layout.adapter_loading_layout, null);
//        }
//    }
//
//    public synchronized void adapterLoadNextPage(final int page) {
//        if (!isLoadingNextPage) {
//            this.isLoadingNextPage = true;
//            // 添加数据的时候增加300ms延迟是为了解决一个空指针的bug
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    AdapterPagingHelper.this.pagingListener.onNextPageRequest(mAdapter, page);
//                }
//
//            }, LOADING_DELAY_TIME);
//        }
//
//    }
//
//    @Override
//    public void mayHaveNextPage() {
//        page++;
//        isLoadingNextPage = false;
//        mLoadingStatus = STATUS_LOADING;
//        onAdapterNotifyDataSetChanged();
//    }
//
//    @Override
//    public void noMorePage() {
//        isLoadingNextPage = false;
//        mLoadingStatus = STATUS_LOADING_END;
//        onAdapterNotifyDataSetChanged();
//    }
//
//    /**
//     * 点击按钮加载下一页代替之前滑动到底部就自动加载
//     */
//    public void tapNextPage() {
//        isLoadingNextPage = false;
//        mLoadingStatus = STATUS_LOADING_NEED_TAP;
//        onAdapterNotifyDataSetChanged();
//    }
//
//    /**
//     * 设置初始加载页面的页码
//     *
//     * @param startPage
//     */
//    public void setStartPage(int startPage) {
//        this.page = startPage;
//        this.startPage = startPage;
//    }
//
//    public int getPage() {
//        return this.page;
//    }
//
//    public int getStartPage() {
//        return this.startPage;
//    }
//
//    public void setPagingListener(PagingListener listener) {
//        this.pagingListener = listener;
//    }
//
//    public PagingListener getPagingListener() {
//        return pagingListener;
//    }
//}
