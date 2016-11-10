//package com.liuyh.library.frame.adapter.adapter.recycler;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * RecyclerAdapter的基类
// * Created by liuyuhang on 16/5/12.
// */
//public abstract class BaseRecyclerFrameAdapter<T> extends RecyclerView.Adapter<BaseRecyclerFrameAdapter.BaseViewHolder> {
//
//    private Context context;
//    private LayoutInflater inflater;
//
//    public List<T> list;
//
//    protected abstract void onViewAttach(BaseViewHolder viewHolder, T item, int position);
//
//    protected abstract View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent);
//
//    public BaseRecyclerFrameAdapter(Context context) {
//        this.context = context;
//        this.inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
//        View view = onViewCreate(position, inflater, viewGroup);
//        return new BaseViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(BaseRecyclerFrameAdapter.BaseViewHolder viewHolder, int position) {
//        onViewAttach(viewHolder, list.get(position), position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return null == list ? 0 : list.size();
//    }
//
//    public void resetData(List<T> data) {
//        if (null == this.list) {
//            this.list = new ArrayList<T>();
//        } else {
//            this.list.clear();
//        }
//        addData(data);
//    }
//
//    public void addData(List<T> data) {
//        if (null == this.list) {
//            this.list = new ArrayList<T>();
//        }
//        this.list.addAll(data);
//
//        notifyDataSetChanged();
//    }
//
//    public void removeData(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public Context getContext(){
//        return context;
//    }
//
//    public class BaseViewHolder extends RecyclerView.ViewHolder {
//        private View convertView;
//
//        public BaseViewHolder(View itemView) {
//            super(itemView);
//            this.convertView = itemView;
//        }
//
//        public <T extends View> T findViewById(int id) {
//            return ViewHolder.get(convertView, id);
//        }
//    }
//}
